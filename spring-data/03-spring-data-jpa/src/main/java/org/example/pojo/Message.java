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
}
