package com.spring.springJwt.controller;

import com.spring.springJwt.Exception.ResourceNotFoundException;
import com.spring.springJwt.models.Company;
import com.spring.springJwt.models.Job;
import com.spring.springJwt.models.Job;
import com.spring.springJwt.payload.response.MessageResponse;
import com.spring.springJwt.repository.CompanyRepository;
import com.spring.springJwt.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
// for Angular Client (withCredentials)
// @CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/test")
public class JobController {
    @Autowired
   private JobRepository jobRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @GetMapping("/jobs/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id){
        Job job = jobRepository.findById(id).get();
        return new ResponseEntity<>(job, HttpStatus.OK);
    }
    @GetMapping("/jobs")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Job>> getAll(){
        List<Job> jobList = new ArrayList<Job>();
        jobRepository.findAll().forEach(jobList::add);
        return new ResponseEntity<>(jobList, HttpStatus.OK);
    }
    @GetMapping("/companies/{companyId}/jobs")
    public ResponseEntity<List<Job>> getAllJobByCompanyId(@PathVariable(value = "companyId") Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Not found Company with id: " + companyId);
        }

        List<Job> jobs = jobRepository.findJobsByCompaniesCompanyId(companyId);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @PostMapping("/jobs/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createJob(@RequestBody Job job) {
      if(jobRepository.existsByName(job.getName())){

          return ResponseEntity.badRequest().body(new MessageResponse("Job name had already used"));
      }
        Job job1 = jobRepository.save(
                new Job(job.getId(), job.getSalary(), job.getName(), job.getLevel(), job.getExperience(), job.getAddress(), job.getCompanies()));

        return new ResponseEntity<>(job1, HttpStatus.CREATED);
    }

    @PostMapping("/companies/{companyId}/jobs")
    public ResponseEntity<Job> addJob(@PathVariable(value = "companyId") Long companyId, @RequestBody Job jobRequest) {
            Job job = companyRepository.findById(companyId).map(company -> {

                company.addJob(jobRequest);
                return jobRepository.save(jobRequest);
            }).orElseThrow(() -> new ResourceNotFoundException("Not found Company with id = " + companyId));
            Job newJob = jobRepository.save(
                    new Job(jobRequest.getId(), jobRequest.getSalary(), jobRequest.getName(), jobRequest.getLevel(), jobRequest.getExperience(), jobRequest.getAddress()));

            return new ResponseEntity<>(newJob, HttpStatus.CREATED);
        }

    @DeleteMapping("/companies/{companyId}/jobs/{jobId}")
    public ResponseEntity<HttpStatus> deleteJobFromCompany(@PathVariable(value = "companyId") Long companyId, @PathVariable(value = "jobId") Long jobId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("Not found Company with id = " + companyId));

        company.removeJob(jobId);
        companyRepository.save(company);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping(value = "/jobs/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Job> updateJob(@PathVariable("id") Long id, @RequestBody Job job) throws IOException {
        Job jobUpdate =jobRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Not found Job with id = " + id));
        jobUpdate.setName(job.getName());
        jobUpdate.setAddress(job.getAddress());
        jobUpdate.setLevel(job.getLevel());
        jobUpdate.setSalary(job.getSalary());
        jobUpdate.setExperience(job.getExperience());
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        jobUpdate.setLogo(filename);
//        Job saveJob = jobRepository.save(jobUpdate);
//        String uploadDir = "D:\\VueJsProject\\vue-3-authentication-jwt\\src\\assets\\images_job\\" + Job.getJobId();
//        FileUploadUtil.saveFile(uploadDir, filename, file);

        return new ResponseEntity<>(jobRepository.save(jobUpdate),HttpStatus.OK);
    }
    @DeleteMapping("/jobs/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteJob(@PathVariable("id") long id){
        jobRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
