package org.example.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity // 作為 hibernate 實體類
@Table(name = "tb_customer") // 映射的表名
public class Customer {

	/**
	 * @Id: 聲明主鍵的配置
	 * @GeneratedValue: 配置主鍵的生成策略
	 * 		strategy
	 * 			GenerationType.IDENTITY: 自增, mysql
	 * 				- 底層數據庫必須支持自動增長 (底層數據庫支持的自動增長方式, 對 id 自增)
	 *			GenerationType.SEQUENCE: 序列, oracle
	 *				- 底層數據庫必須支持序列
	 *			GenerationType.TABLE: jpa 提供的一種機制，通過一張數據庫表的形式幫助我們完成主鍵...?
	 *			GenerationType.AUTO: 由程序自動的幫助我們選擇生成策略
	 * @Column: 配置屬性和字段的映射關係
	 * 		name: 數據庫表中字段的名稱
	 *
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	/** 客戶的主鍵。 */
	private Long custId;

	@Column(name = "cust_name")
	/** 客戶名稱。 */
	private String custName;

	@Column(name = "cust_address")
	/** 客戶地址。 */
	private String custAddress;

	/**
	 * cascade 設置關聯操作
	 * 	ALL: 所有持久化操作
	 * 	PERSIST: 只有插入才會執行關聯操作
	 * 	MERGE: 只有修改才會執行關聯操作
	 * 	REMOVE: 只有刪除才會執行關聯操作
	 *
	 * fetch 設置是否懶加載
	 * 	EAGER: 默認。立即加載(查詢)
	 * 	LAZY: 懶加載，直到用到對象 (Account) 才會進行查詢。 因為不是所有關聯對象都需要用到 (一個主表可能有多個子表，可以某一些設 LAZY)
	 *
	 * orphanRemoval 關聯移除(orphan=孤兒)。通常在修改的時候會用到，一旦關聯的數據設置 null 或改為其他數據時，想刪除關聯數據，可以設置為 true。默認 false
	 *
	 * optional 限制關聯對象可否為 null。默認 true。
	 *
	 * mappedBy 將外鍵約束交給另一方維護。 (通常在雙向關聯中，會放棄一方的外鍵約束)
	 * 	值 = 另一方關聯屬性名
	 *
	 * 	(否則會變成 TABLE 互有對方外鍵)
	 */
	// 單向關聯 一對一
	@OneToOne(
			mappedBy = "customer",
			cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	// 設置外鍵的字段名
	// (TODO: 設置 mappedBy = "customer" 後，此註解應該就沒用了?)
	@JoinColumn(name = "account_id")
	private Account account;
}
