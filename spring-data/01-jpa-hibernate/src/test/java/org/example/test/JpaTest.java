package org.example.test;

import org.example.pojo.Customer;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaTest {
	// 類似 Hibernate SessionFactory
	EntityManagerFactory factory;

	@Before
	public void init() {
		factory = Persistence.createEntityManagerFactory("hibernateJPA");

		// 可簡單地依據 persistence-unit name 來切換實現類，但不用深入研究，因為 openjpa 不太會用到
//		factory = Persistence.createEntityManagerFactory("openjpa");
	}

	@Test
	public void testC() {
		// 類似 Hibernate Session
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Customer customer = new Customer();
		customer.setCustName("李四");

		entityManager.persist(customer);

		transaction.commit();
	}

	@Test
	public void testR() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		// 立即查詢
		Customer customer = entityManager.find(Customer.class, 1L);
		// 延遲查詢 lazy
//		Customer customer = entityManager.getReference(Customer.class, 1L);
		System.out.println("======== JPA Read Test ========");
		System.out.println(customer);

		transaction.commit();
	}

	@Test
	public void testU() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Customer customer = new Customer();
		customer.setCustId(3L);
		customer.setCustName("張三");

		// 具有 save or update 的效果
		/**
		 * 有指定主鍵 -> 更新: 先查詢看是否有變化，有變化則更新，反之不更新
		 * 未指定主鍵 -> 插入
		 */
		// 有給 ID -> SELECT THEN UPDATE; 沒給 ID -> INSERT
		// JPA 未提供單獨的 update 方法，想硬性規定只做 update 要自己寫 JPQL
		entityManager.merge(customer);

		transaction.commit();
	}

	@Test
	public void testUpdateByJpql() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		String jpql = "UPDATE Customer set custName=:name where custId=:id";

		entityManager.createQuery(jpql)
				.setParameter("name", "王五")
				.setParameter("id", 3L)
				.executeUpdate();


		transaction.commit();
	}

	@Test
	public void testUpdateByNativeSql() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		String sql = "UPDATE cst_customer set cust_name=:name where cust_id=:id";

		entityManager.createNativeQuery(sql)
				.setParameter("name", "張三")
				.setParameter("id", 3L)
				.executeUpdate();

		transaction.commit();
	}

	@Test
	public void testD() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		// java.lang.IllegalArgumentException: Removing a detached instance org.example.pojo.Customer#5
//		Customer customer = new Customer();
//		customer.setCustId(5L);
//		entityManager.remove(customer);

		// 不想先查再刪的話，要寫 JPQL
		Customer customer = entityManager.find(Customer.class, 5L);
		entityManager.remove(customer);

		transaction.commit();
	}

	@Test
	public void testEntityState() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Customer customer = new Customer(); //  臨時狀態(瞬時狀態)
		customer.setCustId(5L); // 游離狀態
		customer = entityManager.find(Customer.class, 5L); // 持久狀態
		entityManager.remove(customer); // 刪除狀態(銷毀狀態)

		transaction.commit();
	}

	@Test
	public void testEntityState2() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Customer customer = entityManager.find(Customer.class, 1L); // 持久狀態
		// 持久狀態進行修改會同步數據庫
		customer.setCustName("徐庶徐庶徐庶");

		// 持久狀態下即使未呼叫 merge 之類的 API，一旦事務提交，仍然會執行 UPDATE
		transaction.commit();
	}

	@Test
	public void testEntityState3() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		// 注意: 實際上只會執行一條 SQL SELECT，和直觀理解 (SELECT -> DELETE -> INSERT) 不同!
		Customer customer = entityManager.find(Customer.class, 1L); // 持久狀態
		entityManager.remove(customer);
		// 對 removed 狀態的實體執行 persist 會回歸 managed 狀態
		entityManager.persist(customer);
		transaction.commit();
	}

	@Test
	public void testCacheI() {
		EntityManager entityManager = factory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		// 只會執行一條 SQL SELECT
		Customer customer = entityManager.find(Customer.class, 1L);
		Customer customer2 = entityManager.find(Customer.class, 1L);

		transaction.commit();


		EntityManager entityManager2 = factory.createEntityManager();

		transaction.begin();

		// 會執行另外一條 SQL SELECT
		Customer customer3 = entityManager2.find(Customer.class, 1L);

		transaction.commit();
	}
}
