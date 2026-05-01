package yuki.creator_studio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
		name = "t_upload_jobs",
		indexes = {
				@Index(name = "ix_t_upload_jobs_is_deleted", columnList = "is_deleted"),
				@Index(name = "ix_t_upload_jobs_upload_status_id", columnList = "upload_status_id")
		}
)
public class UploadJob extends AbstractAuditableEntity {

	@Column(name = "file_name", length = 100, nullable = false)
	private String fileName;

	@Column(name = "storage_path", length = 1000, nullable = false)
	private String storagePath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_status_id", nullable = false)
	private UploadStatus uploadStatus;
}
