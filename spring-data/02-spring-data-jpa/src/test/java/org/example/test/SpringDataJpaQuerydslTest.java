package org.example.test;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.pojo.QCustomer;
import org.example.repository.CustomerQuerydslRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaQuerydslTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerQuerydslRepository customerRepository;

	@Test
	public void test01() {
		QCustomer customer = QCustomer.customer;

		// 通過 ID 查找
		BooleanExpression eq = customer.custId.eq(1L); // 寫法上比 Specification 簡潔許多
		// BooleanExpression 繼承 findOne 參數型別 Predicate
		System.out.println(customerRepository.findOne(eq));
	}

	/**
	 * 查詢客戶
	 * 範圍 (IN)
	 * ID > 大於
	 * 地址 精確匹配
	 */
	@Test
	public void test02() {
		QCustomer customer = QCustomer.customer;

		BooleanExpression and = customer.custName.in("劉備", "公孫瓚")
							.and(customer.custId.gt(0L))
							.and(customer.custAddress.eq("BEIPING"));

		Iterable<Customer> customers = customerRepository.findAll(and);

		System.out.println(customers);
	}
}
