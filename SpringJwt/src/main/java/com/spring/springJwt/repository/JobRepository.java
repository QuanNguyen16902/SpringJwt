package com.spring.springJwt.repository;

import com.spring.springJwt.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Boolean existsByName(String Name);
    List<Job> findJobsByCompaniesCompanyId(Long companyId);
}
