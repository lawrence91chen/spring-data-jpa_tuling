package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaPagingAndSortingTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerRepository customerRepository;

	@Test
	public void testPaging() {
		Page<Customer> all = customerRepository.findAll(PageRequest.of(0, 2));
		System.out.println(all);
		System.out.println(all.getTotalPages());
		System.out.println(all.getTotalElements());
		System.out.println(all.getContent());
	}

	@Test
	public void testSort() {
		// 參數是 entity 屬性名稱
//		Sort sort = Sort.by("custId").descending();
		// type-safe (避免屬性名改了，這邊沒改)
		TypedSort<Customer> customerTypedSort = Sort.sort(Customer.class);
		Sort sort = customerTypedSort.by(Customer::getCustId).descending();
		Iterable<Customer> all = customerRepository.findAll(sort);
		System.out.println(all);
	}
}
