package com.spring.springJwt.repository;

import com.spring.springJwt.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Boolean existsByCompanyName(String companyName);
    Boolean existsByAddress(String address);
    Boolean existsByPhone(String phone);

//    List<Company> findCompaniesByJobId(Long jobId);
}
