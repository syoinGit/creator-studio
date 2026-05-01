package yuki.creator_studio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_media_assets", indexes = @Index(name = "ix_t_media_assets_is_deleted", columnList = "is_deleted"))
public class MediaAsset extends AbstractAuditableEntity {

	@Column(name = "project_id", nullable = false)
	private Long projectId;

	@Column(name = "media_type_id", nullable = false)
	private Long mediaTypeId;

	@Column(name = "original_file_name", length = 100, nullable = false)
	private String originalFileName;

	@Column(name = "saved_file_name", length = 100, nullable = false)
	private String savedFileName;

	@Column(name = "mime_type", length = 100)
	private String mimeType;

	@Column(name = "storage_path", length = 1000, nullable = false)
	private String storagePath;

	@Column(name = "file_size", nullable = false)
	private Long fileSize;

	@Column(name = "memo", length = 1000)
	private String memo;

	}
