package org.example.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
// 無參構造函數一定要有，否則查詢會有問題
@NoArgsConstructor
@Entity
@Table(name = "tb_message")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String info;

	public Message(String info) {
		this.info = info;
	}

	public Message(String info, Customer customer) {
		this.info = info;
		this.customer = customer;
	}

	// 多對一
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	// @JoinColumn(name = "customer_id") 可以不加，因為 1 的那方已經聲明。但加了也不會怎樣，總之都只會對應到一個外鍵
	private Customer customer;

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", info='" + info + '\'' +
				", customerId=" + customer.getCustId() +
				'}';
	}
}
