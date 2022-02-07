package org.example.repository;

import org.example.pojo.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface CustomerQbeRepository extends PagingAndSortingRepository<Customer, Long>, QueryByExampleExecutor<Customer> {

}
