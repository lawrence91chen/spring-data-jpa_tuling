package org.example.pojo;

import lombok.*;

import javax.persistence.*;

//@Getter // 生成所有屬性的 getter 方法
//@Setter // 生成所有屬性的 setter 方法
//@RequiredArgsConstructor // 生成有 final 屬性的 constructor。如果無任何屬性加 final，生成 NoArgsConstructor
//@EqualsAndHashCode // 重寫 equals 和 hashcode 方法，一個類會自動繼承 Object，繼承後就可以 override 這兩個方法。 https://blog.csdn.net/weixin_41888813/article/details/81530272
//@ToString
@Data
@Entity
@Table(name = "tb_account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;

	@OneToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
