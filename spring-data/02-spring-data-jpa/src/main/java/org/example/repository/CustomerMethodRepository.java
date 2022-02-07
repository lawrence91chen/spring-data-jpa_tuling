package org.example.repository;

import org.example.pojo.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CustomerMethodRepository extends PagingAndSortingRepository<Customer, Long> {

	List<Customer> findByCustName(String custName);

	boolean existsByCustName(String custName);

	@Transactional
//	@Modifying // 好像可以不用加
	int deleteByCustId(Long id);

	List<Customer> findByCustNameLike(String custName);
}
