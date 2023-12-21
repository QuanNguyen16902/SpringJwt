package com.spring.springJwt.controller;

import com.spring.springJwt.models.Company;
import com.spring.springJwt.models.User;
import com.spring.springJwt.payload.response.MessageResponse;
import com.spring.springJwt.repository.CompanyRepository;
import com.spring.springJwt.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;
    @GetMapping("/company/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id){
        Company company = companyRepository.findById(id).get();
        return new ResponseEntity<>(company, HttpStatus.OK);
    }
    @GetMapping("/company")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Company>> getAll(){
        List<Company> companyList = new ArrayList<Company>();
        companyRepository.findAll().forEach(companyList::add);
        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }
//    @PostMapping(value = "/addCompany")
//    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    public ResponseEntity<Company> addCompany(@Valid @ModelAttribute Company company, @RequestParam("logoCompany") MultipartFile multipartFile) {
//        try {
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            Company _company = new Company(company.getCompanyId(), company.getCompanyName(),
//                company.getLogo(), company.getEmail(), company.getAddress(), company.getPhone(), company.getDescription());
//          _company.setLogo(fileName);
//            Company saved = companyRepository.save(_company);
//
//            String uploadDir = "./images_company/" + saved.getCompanyId();
//
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//            InputStream inputStream = multipartFile.getInputStream();
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            return new ResponseEntity<>(saved, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PostMapping(value = "/company/add" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> createCompany(@ModelAttribute Company company, @RequestParam("file") MultipartFile file) throws IOException {
        if(companyRepository.existsByCompanyName(company.getCompanyName())){

            return ResponseEntity.badRequest().body(new MessageResponse("Company name had already used"));
        }
        if(companyRepository.existsByPhone(company.getPhone())){

            return ResponseEntity.badRequest().body(new MessageResponse("Phone had already used"));
        }
        if(companyRepository.existsByAddress(company.getAddress())){

            return ResponseEntity.badRequest().body(new MessageResponse("Address name had already used"));
        }

        Company company1 = companyRepository.save(new Company(company.getCompanyId(), company.getCompanyName(),
                company.getLogo(), company.getEmail(), company.getAddress(), company.getPhone(), company.getDescription()));

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        company1.setLogo(filename);
        Company saveCompany = companyRepository.save(company1); //"./logo/"
        String uploadDir = "D:\\VueJsProject\\vue-3-authentication-jwt\\src\\assets\\images_company\\" + saveCompany.getCompanyId();
        FileUploadUtil.saveFile(uploadDir, filename, file);

        return new ResponseEntity<>(saveCompany, HttpStatus.CREATED);

    }

    @PutMapping(value = "/company/{id}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Company> updateCompany(@PathVariable("id") Long id, @ModelAttribute Company company, @RequestParam("file") MultipartFile file) throws IOException {
        Company companyUpdate =companyRepository.findById(id).get();
        companyUpdate.setCompanyName(company.getCompanyName());
        companyUpdate.setAddress(company.getAddress());
        companyUpdate.setEmail(company.getEmail());
        companyUpdate.setPhone(company.getPhone());
        companyUpdate.setDescription(company.getDescription());

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        companyUpdate.setLogo(filename);
        Company saveCompany = companyRepository.save(companyUpdate);
        String uploadDir = "D:\\VueJsProject\\vue-3-authentication-jwt\\src\\assets\\images_company\\" + saveCompany.getCompanyId();
        FileUploadUtil.saveFile(uploadDir, filename, file);

        return new ResponseEntity<>(saveCompany,HttpStatus.OK);
    }
    @DeleteMapping("/company/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteCompany(@PathVariable("id") long id){
        companyRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
