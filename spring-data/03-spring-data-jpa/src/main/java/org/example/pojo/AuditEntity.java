package org.example.pojo;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity implements Serializable {

	/** 建立者 */
	@CreatedBy
	private String createBy;

	/** 修改者 */
	@LastModifiedBy
	private String modifiedBy;

	/** 建立時間 */
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	/** 修改時間 */
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateModified;
}
