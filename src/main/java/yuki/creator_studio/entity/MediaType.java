package yuki.creator_studio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
		name = "m_media_types",
		indexes = @Index(name = "ix_m_media_types_is_deleted", columnList = "is_deleted"),
		uniqueConstraints = @UniqueConstraint(name = "uk_m_media_types_media_type_name", columnNames = "media_type_name")
)
public class MediaType extends AbstractAuditableEntity {

	@Column(name = "media_type_name", length = 50, nullable = false)
	private String mediaTypeName;

	@Column(name = "description", length = 100)
	private String description;

}
