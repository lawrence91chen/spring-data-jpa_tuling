package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.pojo.Message;
import org.example.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OneToManyTest {
	@Autowired
	CustomerRepository customerRepository;

	@Test
	public void testC() {
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("你好"));
		messages.add(new Message("在嗎?"));

		Customer customer = new Customer();
		customer.setCustName("諸葛");
		customer.setMessages(messages);

		// Message 端未設置關聯情況下 會有 3 條 insert + 2 條 update
		// 這是單純設置一對多下的正常情況
		customerRepository.save(customer);
	}

	@Test
	@Transactional(readOnly = true) // 沒加會報錯，表示默認情況下 一對多是 懶加載
	public void testR() {
		// 懶加載過程:
		// findById 只查詢 Customer 和設置立即加載的其他關連
		Optional<Customer> customer = customerRepository.findById(17L);
		System.out.println("==== TEST ====");
		// 由於輸出會自動調用 customer.toString()，方法內部用到懶加載的 messages，此時才進行查詢
		System.out.println(customer.get());
	}

	@Test
	public void testD() {
		// TODO: select、select、update、delete、delete、delete
		customerRepository.deleteById(17L);
	}

	@Test
	public void testU() {
		Customer customer = new Customer();
		customer.setCustName("諸葛亮");
		// save 後就會自動產生 version=0
		customerRepository.save(customer);
	}

	@Test
	@Transactional
	@Commit
	public void testU02() {
		Optional<Customer> customer = customerRepository.findById(26L);
		customer.get().setCustName("諸葛孔明");
		// 不須調用 save 也會更新 (因為直接修改持久化數據)

		// version 更新為 1
		// 併發修改 demo 上較麻煩先不演示
	}
}
