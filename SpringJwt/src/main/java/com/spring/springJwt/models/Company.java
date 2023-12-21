package com.spring.springJwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_logo" ,length = 64)
    private String logo;

    @Column(name = "company_email")
    private String email;

    @Column(name = "company_address")
    private String address;

    @Column(name = "company_phone")
    private String phone;

    @Column(name = "company_description")
    private String description;

    public Company(Long companyId, String companyName, String logo, String email, String address, String phone, String description) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.logo = logo;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.description = description;
    }

    @Transient
    public String getLogoImgPath(){
        if(logo == null) return null;
        return "/logo/" + companyId + "/" + logo;
    }
//
    public void addJob(Job job){
        this.jobs.add(job);
        job.getCompanies().add(this);
    }
    public void removeJob(long jobId){
        Job job = this.jobs.stream().filter(t -> t.getId() == jobId).findFirst().orElse(null);
        if(job != null){
            this.jobs.remove(job);
            job.getCompanies().remove(this);
        }
    }
//    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "companies", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
//    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "job_company",
            joinColumns = { @JoinColumn(name = "company_id")},
            inverseJoinColumns = { @JoinColumn(name = "job_id")}
    )
    private Set<Job> jobs = new HashSet<>();
}
