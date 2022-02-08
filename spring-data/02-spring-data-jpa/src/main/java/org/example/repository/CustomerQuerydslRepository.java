package org.example.repository;

import org.example.pojo.Customer;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerQuerydslRepository
		extends PagingAndSortingRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {

}
