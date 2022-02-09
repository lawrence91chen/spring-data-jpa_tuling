package org.example.app;

import org.example.config.SpringDataJpaConfig;
import org.example.proxy.MyJpaRepository;
import org.example.repository.CustomerRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class MainStart {
	public static void main(String[] args) throws ClassNotFoundException {
		// 下面這段講解被剪掉了，只能照著先打
		// Spring 上下文 spring 容器
		AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(SpringDataJpaConfig.class);

//		CustomerRepository repository = ioc.getBean(CustomerRepository.class);
//
//		System.out.println(repository.getClass()); // jdk 動態: class com.sun.proxy.$Proxy74
//
//		Optional<Customer> customer = repository.findById(26L);
//		System.out.println(customer.get());

		// 自行創建 jdk 動態代理實例
		// 上面是註解掉的則是從 Spring 容器中取得的，下面是我們自己創建的

		// 獲得 EntityManager
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
				= ioc.getBean(LocalContainerEntityManagerFactoryBean.class);
		// 創建 EntityManager 的實現類。(也就是 Hibernate，但 Spring 底層會依據我們配置的數據源自動找到，所以傳 null 就好)
		EntityManager entityManager = entityManagerFactoryBean.createNativeEntityManager(null);
		// 獲得當前接口的 POJO 類
		// getGenericInterfaces() 拿到當前接口的父接口 -> PagingAndSortingRepository
		ParameterizedType parameterizedType = (ParameterizedType) CustomerRepository.class.getGenericInterfaces()[0];
		// 拿到接口的泛型 -> <Customer, Long>
		Type type = parameterizedType.getActualTypeArguments()[0];
		Class clazz = Class.forName(type.getTypeName());

		CustomerRepository repository = (CustomerRepository) Proxy.newProxyInstance(
				CustomerRepository.class.getClassLoader(), // 類加載器
				new Class[]{CustomerRepository.class}, // 動態代理的接口
				new MyJpaRepository(entityManager, clazz)// 調用持久化方法具體實現的代碼邏輯
		);

		System.out.println(repository.findById(1L));
	}
}
