package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaTest {
	/**
	 * JDK 動態代理的實例。
	 */
	@Autowired
	CustomerRepository customerRepository;

	@Test
	public void testR() {
		Optional<Customer> byId = customerRepository.findById(1L);
		System.out.println(byId.get());
	}

}
