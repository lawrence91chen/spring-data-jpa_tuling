package org.example.test;

import org.apache.commons.lang3.StringUtils;
import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.repository.CustomerSpecificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpecificationTest {
	/** JDK 動態代理的實例。 */
	@Autowired
	CustomerSpecificationRepository customerRepository;

	@Test
	public void test01() {
		// 撰寫匿名類實現 Specification<T> 中的 toPredicate。 也可以改成 lambda expression
		List<Customer> customers = customerRepository.findAll(new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				// Root: 相當於 FROM Customer，通過 root 來獲取 `列`
				// CriteriaBuilder: 相當於 WHERE，用來設置各種條件 (>、<、IN、...)
				// CriteriaQuery: 用來組合 (ORDER BY、WHERE)

				// 1. 通過 Root 拿到需要設置條件的字段
				// 2. 通過 CriteriaBuilder 設置不同類型條件
				// 3. 組合條件

				return null;
			}
		});

		System.out.println(customers);
	}

	/**
	 * 查詢客戶
	 * 範圍 (IN)
	 * ID > 大於
	 * 地址 精確匹配
	 */
	@Test
	public void test02() {
		// 撰寫匿名類實現 Specification<T> 中的 toPredicate。 也可以改成 lambda expression
		List<Customer> customers = customerRepository.findAll(new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				// root: 相當於 FROM Customer，通過 root 來獲取 `列` 的字段
				Path<Object> custId = root.get("custId");
				Path<Object> custName = root.get("custName");
				Path<Object> custAddress = root.get("custAddress");

				// criteriaBuilder: 相當於 WHERE，用來設置各種條件 (>、<、IN、...)
				// 參數1: 為哪個字段設置條件。 Path<?> 本身即繼承 Expression<?>
				// 參數2: 條件設置的值
				Predicate predicate = criteriaBuilder.equal(custAddress, "BEIPING");

				// query: 用來組合 (ORDER BY、WHERE)

				return predicate;
			}
		});

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
		// 撰寫匿名類實現 Specification<T> 中的 toPredicate。 也可以改成 lambda expression
		List<Customer> customers = customerRepository.findAll(new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				// root: 相當於 FROM Customer，通過 root 來獲取 `列` 的字段
				// 泛型聲明為屬性對應的類型
				Path<Long> custId = root.get("custId");
				Path<String> custName = root.get("custName");
				Path<String> custAddress = root.get("custAddress");

				// criteriaBuilder: 相當於 WHERE，用來設置各種條件 (>、<、IN、...)
				// 參數1: 為哪個字段設置條件。 Path<?> 本身即繼承 Expression<?>
				// 參數2: 條件設置的值
				Predicate pCustAddress = criteriaBuilder.equal(custAddress, "BEIPING");
				Predicate pCustId = criteriaBuilder.greaterThan(custId, 0L);
				// P.S. 是不是看起來有點過於複雜了?
				// 畢竟 Spring Data JPA 本身的定位不是為了複雜查詢而生
				// 所以我們需要因應不同業務場景來挑選對應的技術
				// 一般複雜業務場景的系統目前還是選 MyBatis，實現起來較簡單
				// 如果系統微服務之類是拆分過、較簡單的表，就不太會有太複雜的功能
				// 另外一般微服務架構下的購物網站，動態條件查詢背後也會用搜尋引擎如 Elasticsearch。
				// (如果你的程式出現過多這樣的複雜查詢的場景，說明你的技術選型錯了!)
				CriteriaBuilder.In<String> in = criteriaBuilder.in(custName);
				in.value("公孫瓚").value("劉備");

				Predicate and = criteriaBuilder.and(pCustAddress, pCustId, in);

				// query: 用來組合 (ORDER BY、WHERE)

				return and;
			}
		});

		System.out.println(customers);
	}

	/**
	 * 查詢客戶
	 * 範圍 (IN)
	 * ID > 大於
	 * 地址 精確匹配
	 */
	@Test
	public void test04() {
		// 模擬前端傳入的動態條件參數
		Customer param = new Customer();
		param.setCustAddress("BEIPING");
		param.setCustId(0L);
		param.setCustName("劉備,公孫瓚");

		// 撰寫匿名類實現 Specification<T> 中的 toPredicate。 也可以改成 lambda expression
		List<Customer> customers = customerRepository.findAll(new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				// root: 相當於 FROM Customer，通過 root 來獲取 `列` 的字段
				// 泛型聲明為屬性對應的類型
				Path<Long> custId = root.get("custId");
				Path<String> custName = root.get("custName");
				Path<String> custAddress = root.get("custAddress");

				// criteriaBuilder: 相當於 WHERE，用來設置各種條件 (>、<、IN、...)
				// 參數1: 為哪個字段設置條件。 Path<?> 本身即繼承 Expression<?>
				// 參數2: 條件設置的值

				// 設置可變長度的動態條件列表
				ArrayList<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotEmpty(param.getCustAddress())) {
					predicates.add(criteriaBuilder.equal(custAddress, "BEIPING"));
				}

				if (param.getCustId() > -1) {
					predicates.add(criteriaBuilder.greaterThan(custId, 0L));
				}

				if (StringUtils.isNotEmpty(param.getCustName())) {
					CriteriaBuilder.In<String> in = criteriaBuilder.in(custName);
					in.value("公孫瓚").value("劉備");
					predicates.add(in);
				}

				Predicate and = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

				// query: 用來組合 (ORDER BY、WHERE)

				return and;
			}
		});

		System.out.println(customers);
	}

	/**
	 * 查詢客戶
	 * 範圍 (IN)
	 * ID > 大於
	 * 地址 精確匹配
	 */
	@Test
	public void test05() {
		// 模擬前端傳入的動態條件參數
		Customer param = new Customer();
//		param.setCustAddress("BEIPING");
		param.setCustId(0L);
		param.setCustName("劉備,公孫瓚");

		// 撰寫匿名類實現 Specification<T> 中的 toPredicate。 也可以改成 lambda expression
		List<Customer> customers = customerRepository.findAll(new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				// root: 相當於 FROM Customer，通過 root 來獲取 `列` 的字段
				// 泛型聲明為屬性對應的類型
				Path<Long> custId = root.get("custId");
				Path<String> custName = root.get("custName");
				Path<String> custAddress = root.get("custAddress");

				// criteriaBuilder: 相當於 WHERE，用來設置各種條件 (>、<、IN、...)
				// 參數1: 為哪個字段設置條件。 Path<?> 本身即繼承 Expression<?>
				// 參數2: 條件設置的值

				// 設置可變長度的動態條件列表
				ArrayList<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotEmpty(param.getCustAddress())) {
					predicates.add(criteriaBuilder.equal(custAddress, "BEIPING"));
				}

				if (param.getCustId() > -1) {
					predicates.add(criteriaBuilder.greaterThan(custId, 0L));
				}

				if (StringUtils.isNotEmpty(param.getCustName())) {
					CriteriaBuilder.In<String> in = criteriaBuilder.in(custName);
					in.value("公孫瓚").value("劉備");
					predicates.add(in);
				}

				Predicate and = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

				// query: 用來組合 (ORDER BY、WHERE)
				Order desc = criteriaBuilder.desc(custId);

				return query.where(and).orderBy(desc).getRestriction();
			}
		});

		System.out.println(customers);
	}
}
