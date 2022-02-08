package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class JpqlTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerRepository customerRepository;

	@Test
	public void testR() {
		List<Customer> customers = customerRepository.findCustomersByCustName("小六");
		System.out.println(customers);
	}

	@Test
	public void testU() {
		int count = customerRepository.updateCustomerById("王五", 2L);
		System.out.println(count);
	}

	@Test
	public void testD() {
		int count = customerRepository.deleteCustomer(5L);
		System.out.println(count);
	}

	@Test
	public void testC() {
		int count = customerRepository.insertCustomerBySelect(1L);
		System.out.println(count);
	}

	@Test
	public void testNativeSql() {
		List<Customer> customers = customerRepository.findCustomersByCustNameNative("小六");
		System.out.println(customers);
	}
}
