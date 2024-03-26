package org.example.app;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class MainStart2 {
	public static void main(String[] args) throws ClassNotFoundException {
		// Spring 上下文 spring 容器 ---> IOC 加載過程: 創建所有的 Bean 包括 Repository 的 Bean
		AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(SpringDataJpaConfig.class);

		CustomerRepository repository = ioc.getBean(CustomerRepository.class);

		System.out.println(repository.getClass()); // jdk 動態: class com.sun.proxy.$Proxy74

		Optional<Customer> customer = repository.findById(26L);
		System.out.println(customer.get());

	}
}
