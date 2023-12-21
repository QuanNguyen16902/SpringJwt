package com.spring.springJwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Setter
@AllArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @Column(name = "job_salary")
    private Long salary;

    @Column(name = "job_name")
    private String name;

    @Column(name = "job_level")
    private String level;

    @Column(name = "job_experience")
    private String experience;

    @Column(name = "job_address")
    private String address;
//, cascade = {CascadeType.MERGE}
//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
//    @JoinTable(name = "job_company",
//            joinColumns = @JoinColumn(name = "job_id"),
//            inverseJoinColumns = @JoinColumn(name = "company_id")
//    )
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "jobs"
    )
    @JsonIgnore
    private Set<Company> companies = new HashSet<>();

    public void addCompany(Company company){
        this.companies.add(company);
        company.getJobs().add(this);
    }
    public long getId(){
        return id;
    }
    public Job(){

    }
    public Job(Long id, Long salary, String name, String level, String experience, String address) {
        this.id = id;
        this.salary = salary;
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", experience='" + experience + '\'' +
                ", address='" + address + '\'' +
                ", companies=" + companies +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Job job)) return false;
//        return Objects.equals(id, job.id) && Objects.equals(salary, job.salary) && Objects.equals(name, job.name) && Objects.equals(level, job.level) && Objects.equals(experience, job.experience) && Objects.equals(address, job.address) && Objects.equals(companies, job.companies);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, salary, name, level, experience, address, companies);
//    }

    public Long getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getExperience() {
        return experience;
    }

    public String getAddress() {
        return address;
    }

    public Set<Company> getCompanies() {
        return companies;
    }
}
