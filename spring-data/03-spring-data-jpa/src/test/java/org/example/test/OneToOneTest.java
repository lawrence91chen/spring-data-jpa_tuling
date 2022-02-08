package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Account;
import org.example.pojo.Customer;
import org.example.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OneToOneTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerRepository customerRepository;

	@Test
	public void testC() {
		// 初始化數據
		Account account = new Account();
		account.setUsername("xushu");

		Customer customer = new Customer();
		customer.setCustName("徐庶");
		customer.setAccount(account);

		// 只需要保存維護關係的一方
		customerRepository.save(customer);
	}

	@Test
	public void testR() {
		// 查詢不需要設置 cascade。 說明默認就會進行關聯查詢 (left outer join)
		System.out.println(customerRepository.findById(12L));
	}

	@Test
	/**
	 * 為什麼懶加載要配置事務:
	 * 	當通過 repository 調用完查詢方法，session 就會立即關閉，一旦 session 關閉就無法再查
	 * 	加了事務後，就能讓 session 直到事務方法 testLazyR() 執行完畢後才會關閉
	 */
	@Transactional(readOnly = true)
	public void testLazyR() {
		Optional<Customer> customer = customerRepository.findById(12L);  // 只先查 Customer，session 關閉 (未加事務前)
		System.out.println("==== LAZY TEST ====");
		System.out.println(customer.get()); // 調用 toString 時使用到 Account 的 toString。 若 session 關閉就不能再查
	}

	@Test
	public void testD() {
		customerRepository.deleteById(12L);
	}

	@Test
	public void testU() {
		Customer customer = new Customer();
		customer.setCustId(13L);
		customer.setCustName("徐庶1");
		// account will be deleted by orphanRemoval = true
		customer.setAccount(null);

		customerRepository.save(customer);
	}
}
