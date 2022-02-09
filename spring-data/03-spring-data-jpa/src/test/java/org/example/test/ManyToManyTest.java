package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.pojo.Role;
import org.example.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ManyToManyTest {
	@Autowired
	CustomerRepository customerRepository;

	@Test
	public void testC() {
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("超級管理員"));
		roles.add(new Role("商品管理員"));

		Customer customer = new Customer();
		customer.setCustName("周瑜");
		// 如果設置了關聯數據但沒設置關聯操作 (cascade) 就會報錯
		// 所以說如果不設關聯操作，那就不要去加關聯數據做保存
		customer.setRoles(roles);

		customerRepository.save(customer);
	}
}
