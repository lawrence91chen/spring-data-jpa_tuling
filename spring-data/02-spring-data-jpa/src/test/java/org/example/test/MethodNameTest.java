package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.repository.CustomerMethodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MethodNameTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerMethodRepository customerRepository;

	@Test
	public void test01() {
		List<Customer> customers = customerRepository.findByCustName("張三");
		System.out.println(customers);
	}

	@Test
	public void test02() {
		boolean exists = customerRepository.existsByCustName("張三");
		System.out.println(exists);
	}

	@Test
	public void test03() {
		System.out.println(customerRepository.deleteByCustId(6L));
	}

	@Test
	public void test04() {
		// 需要自行拼接模糊查詢 %
		List<Customer> customers = customerRepository.findByCustNameLike("%三");
		System.out.println(customers);
	}
}
