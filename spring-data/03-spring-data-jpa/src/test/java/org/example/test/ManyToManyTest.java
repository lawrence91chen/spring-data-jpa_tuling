package org.example.test;

import org.example.config.SpringDataJpaConfig;
import org.example.pojo.Customer;
import org.example.pojo.Role;
import org.example.repository.CustomerRepository;
import org.example.repository.RoleRepository;
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
public class ManyToManyTest {
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	RoleRepository roleRepository;

	@Test
	public void testC() {
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("超級管理員"));
		roles.add(new Role("商品管理員"));

		Customer customer = new Customer();
		customer.setCustName("周瑜");
		// 如果設置了關聯數據但沒設置關聯操作 (cascade) 就會報錯
		// 所以說如果不設關聯操作，那就不要去加關聯數據做保存
		customer.setRoles(roles);

		customerRepository.save(customer);
	}

	/**
	 * 1. 如果保存的關聯數據，希望使用已有的，就需要從數據庫查出來 (持久狀態)。
	 *    否則會顯示 `游離(Detached)狀態不能持久化` 錯誤訊息
	 * 2. 如果一個業務方法有多個持久化操作，記得加上 @Transactional，否則不能共用一個 session
	 * 3. 在單元測試中用到了 @Transactional， 如果有增刪改的操作要另外加 @Commit
	 *    (單元測試會認為你的事物方法 @Transactional 只是測試而已，它不會為你提交事務，所以要單獨加上 @Commmit)
	 *
	 *    org.springframework.test.annotation @Commit 是 spring 為了測試專門寫的
	 *    因為一般項目開發中會撰寫大量測試程式，但我們不一定會想要頻繁的將測試過程 真的寫到 DB，只是想知道當前功能是否正常
	 */
	@Test
	@Transactional
	@Commit // 單元測試加了 @Transactional 後就不 commit 了 (先前範例用到事務都是查詢，且只有加在單元測試類上才有此特性，之前加在 Repository 上的就不會有)。
	public void testC02() {
		List<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findById(1L).get());
		roles.add(roleRepository.findById(2L).get());

		Customer customer = new Customer();
		customer.setCustName("諸葛");
		customer.setRoles(roles);

		customerRepository.save(customer);
	}

	@Test
	@Transactional(readOnly = true)
	public void testR() {
		System.out.println(customerRepository.findById(21L));
	}

	/**
	 * 1. 注意加上 @Transactional、@Commit
	 * 2. 多對多其實不適合刪除，因為經常出現其他數據會引用另一端。此時刪除就會: ConstraintViolationException
	 *    要級聯刪除就要保證沒有額外其他另一端數據關聯
	 */
	@Test
	@Transactional
	@Commit
	public void testD() {
		// Customer 中 List<Role> roles @ManyToMany(cascade = CascadeType.ALL) 就不能刪除
		// 但如果 cascade = CascadeType.PERSIST 則可以刪
		// 不能刪原因是如果要連帶刪 Role 是不太合理的，因為可能會有其他人關聯同一個 Role
		// 官網說明: 多對多刪除不適合級聯
		// https://docs.jboss.org/hibernate/stable/orm/userguide/html_single/Hibernate_User_Guide.html#associations-many-to-many
		Optional<Customer> customer = customerRepository.findById(24L);
		customerRepository.delete(customer.get());
	}
}
