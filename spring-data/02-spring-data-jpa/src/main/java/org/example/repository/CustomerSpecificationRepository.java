package org.example.repository;

import org.example.pojo.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerSpecificationRepository
		extends PagingAndSortingRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

}
