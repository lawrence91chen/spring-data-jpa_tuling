package org.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration // 標記當前類為配置類: 表示這個 Class = XML 配置文件
@EnableJpaRepositories(basePackages = "org.example.repository") // 啟用 spring data JPA = <jpa:repositories ...，如果該配置類沒有在最頂層需要指定 basePackages
/**
 *     <!--  事務基於註解: 啟動註解方式的聲明式事務  -->
 *     <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
 */
@EnableTransactionManagement // 開啟事務
public class SpringDataJpaConfig {

	/**
	 *     <!--  數據源  -->
	 *     <bean class="com.alibaba.druid.pool.DruidDataSource" name="dataSource">
	 *         <property name="url" value="jdbc:mysql://localhost/spring-data-jpa_tuling"/>
	 *         <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
	 *         <property name="username" value="root"/>
	 *         <property name="password" value="P@ssw0rd"/>
	 *     </bean>
	 */
	@Bean
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl("jdbc:mysql://localhost/spring-data-jpa_tuling");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername("root");
		dataSource.setPassword("P@ssw0rd");

		return dataSource;
	}

	/**
	 *     <!-- EntityManagerFactory -->
	 *     <bean name="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
	 *         <!-- 設定 JPA 的實現類 -->
	 *         <property name="jpaVendorAdapter">
	 *             <!-- Hibernate 實現 -->
	 *             <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
	 *                 <!-- 生成數據庫表 -->
	 *                 <property name="generateDdl" value="true"></property>
	 *                 <property name="showSql" value="true"></property>
	 *             </bean>
	 *         </property>
	 *         <!-- 設置掃描實體類的包: 那些實體對象需要進行 ORM 映射 -->
	 *         <property name="packagesToScan" value="org.example.pojo"></property>
	 *         <property name="dataSource" ref="dataSource"></property>
	 *     </bean>
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("org.example.pojo");
		factory.setDataSource(dataSource());

		return factory;
	}

	/**
	 *     <!--  聲明式事務  -->
	 *     <bean class="org.springframework.orm.jpa.JpaTransactionManager" name="transactionManager">
	 *         <property name="entityManagerFactory" ref="entityManagerFactory"></property>
	 *     </bean>
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);

		return txManager;
	}
}
