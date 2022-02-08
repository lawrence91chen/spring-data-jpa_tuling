package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.pojo.Message;
import org.example.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ManyToOneTest {
	@Autowired
	MessageRepository messageRepository;

	/**
	 * 多對一 插入
	 * 得出: 當插入 "多" 的數據時，使用多對一的關聯關係是更加合理
	 */
	@Test
	public void testC() {
		// 一
		Customer customer = new Customer();
		customer.setCustName("司馬懿");

		// 多
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("你好", customer));
		messages.add(new Message("在嗎?", customer));

		// 不會多執行兩條 UPDATE。
		messageRepository.saveAll(messages);
	}

	/**
	 * 	多對一: 根據客戶 ID 查詢對應所有訊息
	 * 	(一般來說從一的一方查比較合理) 要從多的一方查就得自定義持久化查詢了
	 * 	得出: 通過 "一" 進行條件查詢，在 一對多 中實現是更合理的
	 */
	@Test
	@Transactional(readOnly = true) // 沒加會報錯，表示默認情況下 一對多是 懶加載
	public void testR() {
		Customer customer = new Customer();
		customer.setCustId(18L);
		customer.setCustName("ooxx");  // 可以從 SQL 發現 設置 custName 也沒用

		List<Message> messages = messageRepository.findByCustomer(customer);
		// java.lang.StackOverflowError 死循環，
		// 因為 sysout 隱式調用了物件 toString() 導致 Customer 和 Message 的 toString 方法互相無窮調用
		// 可以重寫某一方的 toString 即可
		// 使用 lombok 則可加注 exclude 屬性
		// https://stackoverflow.com/questions/54570757/stack-overflow-on-hibernate-caused-by-one-to-many-relationship
		System.out.println(messages);

		// TODO: 產生了 3 條 select
	}

	@Test
	public void testD() {
		// Message 中 @ManyToOne 設置 CascadeType.REMOVE 的話，多的一方全部刪光後 一也會被刪除  (好像不能隨便亂用)
		Customer customer = new Customer();
		customer.setCustId(18L);
		List<Message> messages = messageRepository.findByCustomer(customer);
		messageRepository.deleteAll(messages);

		// TODO: select * 6 + update * 1 + delete * 3
	}
}
