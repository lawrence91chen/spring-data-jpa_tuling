package org.example.test;

import org.example.pojo.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class HibernateTest {

	// Session 工廠: 數據庫會話(與 Servlet Session 不同)，是代碼持久化操作數據庫的一個橋樑
	private SessionFactory sf;

	@Before
	public void init() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("/hibernate.cfg.xml").build();

		// 根據服務註冊類創建一個元數據資源集，同時構建元數據並生成應用一般唯一的 Session 工廠
		sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}

	@Test
	public void testC() {
		// 通過 session 進行持久化操作
		try(Session session = sf.openSession();) {
			Transaction transaction = session.beginTransaction();

			Customer customer = new Customer();
			customer.setCustName("徐庶");

			session.save(customer);

			transaction.commit();
		}
	}

	@Test
	public void testR() {
		// 通過 session 進行持久化操作
		try(Session session = sf.openSession();) {
			Transaction transaction = session.beginTransaction();

//			Customer customer = session.find(Customer.class, 1L);

			// lazy load: 用到的時候才真的查
			Customer customer = session.load(Customer.class, 1L);
			System.out.println("======== load test ========");
			System.out.println(customer);

			transaction.commit();
		}
	}

	@Test
	public void testU() {
		// 通過 session 進行持久化操作
		try(Session session = sf.openSession();) {
			Transaction transaction = session.beginTransaction();

			Customer customer = new Customer();
//			customer.setCustId(1L);
			customer.setCustName("徐庶");

//			session.update(customer);
			session.saveOrUpdate(customer);

			transaction.commit();
		}
	}

	@Test
	public void testD() {
		// 通過 session 進行持久化操作
		try(Session session = sf.openSession();) {
			Transaction transaction = session.beginTransaction();

			Customer customer = new Customer();
			customer.setCustId(2L);

			session.remove(customer);

			transaction.commit();
		}
	}

	@Test
	public void testHql() {
		// 通過 session 進行持久化操作
		try(Session session = sf.openSession();) {
			Transaction transaction = session.beginTransaction();

//			String hql = "FROM Customer";
//			List<Customer> resultList = session.createQuery(hql, Customer.class).getResultList();

			String hql = "FROM Customer where custId=:id";
			List<Customer> resultList = session.createQuery(hql, Customer.class)
					.setParameter("id", 1L)
					.getResultList();

			System.out.println(resultList);

			transaction.commit();
		}
	}
}
