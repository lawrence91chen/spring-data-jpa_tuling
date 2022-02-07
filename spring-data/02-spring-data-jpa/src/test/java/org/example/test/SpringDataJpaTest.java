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

// 基於 Junit4 spring 單元測試，否則無法使用 Spring DI (@Autowired) 進行測試
//@ContextConfiguration(locations = "/spring.xml")
//@ContextConfiguration("/spring.xml")
@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaTest {
	@Autowired
	CustomerRepository customerRepository;

	@Test
	public void testR() {
		// JDK8 的新特性，用來防止空指針
		Optional<Customer> byId = customerRepository.findById(1L);
		System.out.println(byId.get());
	}

	@Test
	public void testC() {
		Customer customer = new Customer();
		customer.setCustName("小六");
		customerRepository.save(customer);
	}

	@Test
	public void testU() {
		Customer customer = new Customer();
		customer.setCustId(3L);
		customer.setCustName("小三");
		// 相當於 JPA merge 方法
		customerRepository.save(customer);
	}

	@Test
	public void testD() {
		Customer customer = new Customer();
		customer.setCustId(3L);
		// spring data JPA 底層會先查再刪，(因為查完就會是持久狀態)所以不會有不能刪 detached instance 的問題
		customerRepository.delete(customer);
	}
}
