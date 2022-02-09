package org.example.repository;

import org.example.pojo.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

	// 省略 SELECT 子句 = 查全部欄位
//	@Query("FROM Customer where custName=?1")
//	List<Customer> findCustomersByCustName(String custName);
	// 具名參數需要綁定 @Param
	@Query("FROM Customer where custName=:custName")
	List<Customer> findCustomersByCustName(@Param("custName") String custName);

	// JPQL 執行增刪改時必須搭配事務，通常會配置在 Service 層，此處僅為演示
	@Transactional
	// 通知 Spring Data JPA 這個方法是 增刪改 的操作
	@Modifying
	// 修改，返回受影響的記錄數
	@Query("UPDATE Customer c set c.custName=:custName where c.custId=:id")
	int updateCustomerById(@Param("custName") String custName, @Param("id") Long id);

	@Transactional
	@Modifying
	@Query("DELETE FROM Customer c where c.custId=?1")
	int deleteCustomer(Long id);

	// JPQL 本身不支持新增，但 Hibernate 的實現支持一種偽新增 (INSERT INTO SELECT ...)
	@Transactional
	@Modifying
	@Query("INSERT INTO Customer(custName) SELECT c.custName FROM Customer c where c.custId=?1")
	int insertCustomerBySelect(Long id);

	@Query(value = "SELECT * FROM tb_customer where cust_name=:custName", nativeQuery = true)
	List<Customer> findCustomersByCustNameNative(@Param("custName") String custName);
}
