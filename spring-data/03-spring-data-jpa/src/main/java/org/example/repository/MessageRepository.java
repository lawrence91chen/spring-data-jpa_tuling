package org.example.repository;

import org.example.pojo.Customer;
import org.example.pojo.Message;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
	/**
	 *  根據客戶 ID 查詢所有訊息
	 *  通過`規定方法名`實現關聯查詢，需要通過關聯屬性來進行匹配
	 *  但是只能通過 ID 來進行匹配
	 */
	List<Message> findByCustomer(Customer customer);
}
