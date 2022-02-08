package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.repository.CustomerQbeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QbeTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerQbeRepository customerRepository;

	/**
	 * 依客戶 名稱與地址 進行動態查詢
	 */
	@Test
	public void test01() {
		// 查詢條件 (一般實務上從前端傳遞)
		Customer customer = new Customer();
		customer.setCustName("徐庶");
		customer.setCustAddress("許昌");
		// 通過 Example 構建查詢條件
		Example<Customer> example = Example.of(customer);

		Iterable<Customer> customers = customerRepository.findAll(example);
		System.out.println(customers);
	}

	/**
	 * 通過匹配器進行條件的限制
	 */
	@Test
	public void test02() {
		// 查詢條件
		Customer customer = new Customer();
		customer.setCustName("趙雲");
		customer.setCustAddress("PING");

		// 通過匹配器對條件行為進行設置
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnorePaths("custName") // 設置忽略的屬性: 忽略 custName 欄位
//				.withIgnoreCase("custAddress") // 設置忽略大小寫: 若未指定參數，則所有欄位都會忽略大小寫 (實際 SQL 會套用 lower 函數)
//				.withStringMatcher(ExampleMatcher.StringMatcher.ENDING); // 對 `所有條件` 的字串進行結尾匹配(會轉成 LIKE 進行模糊查詢)

				// 使用 withMatcher 後上方 withIgnoreCase 就會失效，需要在 withMatcher 中單獨另外設定
//				.withMatcher("custAddress", m -> m.endsWith());
//				.withMatcher("custAddress", ExampleMatcher.GenericPropertyMatcher::endsWith); // 針對單個條件進行限制
				.withMatcher("custAddress", ExampleMatcher.GenericPropertyMatchers.endsWith().ignoreCase());

		// 通過 Example 構建查詢條件
		Example<Customer> example = Example.of(customer, matcher);

		Iterable<Customer> customers = customerRepository.findAll(example);
		System.out.println(customers);
	}
}
