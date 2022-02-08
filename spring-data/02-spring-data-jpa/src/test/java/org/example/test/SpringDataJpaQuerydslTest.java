package org.example.test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.pojo.QCustomer;
import org.example.repository.CustomerQuerydslRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaQuerydslTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerQuerydslRepository customerRepository;

	/**
	 * 線程安全問題:
	 *
	 * - Spring data JPA 有線程安全問題且沒有特別處理
	 * - 用 @Autowired 注入 EntityManager會有線程安全問題
	 * - 要保證線程安全須加上 @PersistenceContext 來解決，Spring data JPA 會為每個線程綁定各自的 EntityManager
	 * - 老師也只在 EntityManager 用過 @PersistenceContext，其他地方能否使用或是否適用就不確定了
	 *
	 * P.S.
	 * 一個 Bean 默認是單例 (Singleton) 的
	 * 對 Singleton Bean 內聲明的公共成員(變量)同時進行讀寫會引發 thread-safe 問題
	 * Spring data JPA EntityManager is NOT thread safe
	 * https://stackoverflow.com/questions/24643863/is-entitymanager-really-thread-safe
	 */
	@PersistenceContext
	EntityManager entityManager;

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

	/**
	 * 查詢客戶
	 * 範圍 (IN)
	 * ID > 大於
	 * 地址 精確匹配
	 */
	@Test
	public void test03() {
		// 模擬前端傳入的動態條件參數
		Customer param = new Customer();
		param.setCustAddress("BEIPING");
		param.setCustId(0L);
		param.setCustName("劉備,公孫瓚");

		QCustomer customer = QCustomer.customer;
		// 初始條件 類似 1=1 永遠都成立
		// P.S. 我自己覺得很怪，實際上應該不會這樣寫。但這邊只是 demo 效果，先不管 (有人說可以用 BooleanBuilder)
		BooleanExpression expression = customer.isNotNull().or(customer.isNull());
		expression = param.getCustId() > -1 ? expression.and(customer.custId.gt(param.getCustId())) : expression;
		expression = StringUtils.isNotEmpty(param.getCustName()) ?
				expression.and(customer.custName.in(param.getCustName().split(","))): expression;
		expression = StringUtils.isNotEmpty(param.getCustAddress()) ?
				expression.and(customer.custAddress.eq(param.getCustAddress())) : expression;

		Iterable<Customer> customers = customerRepository.findAll(expression);

		System.out.println(customers);
	}

	/**
	 * 自定義列查詢(只查若干欄位)、分組
	 * 需要使用原生態的方式
	 *
	 * 用原生態方式進行的查詢就與 Repository 無關了
	 * 通過 Repository 進行查詢，列、表都是固定的
	 * 想要動態的改變查詢的列/表，就得用原生的方式
	 */
	@Test
	public void test04() {
		// JPAQueryFactory 就像寫 SQL 一樣寫程式，因此還是比 Specification 可讀性好上不少
		JPAQueryFactory factory = new JPAQueryFactory(entityManager);
		QCustomer customer = QCustomer.customer;


		// 整個過程都是面向 Querydsl 對應的 API
		// 返回類型 Tuple 是因為沒查全部欄位，如果下 select(customer) 就會是 Customer
		/**
		 * SELECT id, cust_name
		 * FROM tb_customer
		 * WHERE id = 1
		 */
		// 構建基於 Querydsl 的查詢
		JPAQuery<Tuple> tupleJPAQuery = factory.select(customer.custId, customer.custName)
				.from(customer)
				.where(customer.custId.eq(1L))
				.orderBy(customer.custId.desc());
		// 執行查詢
		List<Tuple> tuples = tupleJPAQuery.fetch();
		// 處理返回數據
		for (Tuple tuple : tuples) {
			System.out.println(tuple.get(customer.custId));
			System.out.println(tuple.get(customer.custName));
		}

		JPAQuery<Long> longJPAQuery = factory.select(customer.custId.sum())
				.from(customer);
		System.out.println(longJPAQuery.fetch());
	}
}
