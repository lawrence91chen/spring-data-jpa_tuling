package org.example.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity // 作為 hibernate 實體類
@Table(name = "cst_customer") // 映射的表名
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
	@Column(name = "cust_id")
	/** 客戶的主鍵。 */
	private Long custId;

	@Column(name = "cust_name")
	/** 客戶名稱。 */
	private String custName;

	@Column(name = "cust_address")
	/** 客戶地址。 */
	private String custAddress;
}
