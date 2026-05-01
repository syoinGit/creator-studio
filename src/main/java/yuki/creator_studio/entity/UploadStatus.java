package yuki.creator_studio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
		name = "m_upload_statuses",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_m_upload_statuses_status_code",
				columnNames = "status_code"
		)
)
public class UploadStatus extends AbstractAuditableEntity {

	@Column(name = "status_code", length = 20, nullable = false)
	private String statusCode;

	@Column(name = "status_name", length = 50, nullable = false)
	private String statusName;

	@Column(name = "description", length = 255)
	private String description;
}