package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.models.Company;

public interface CompanyRepository extends CrudRepository<Company, String> {

}
