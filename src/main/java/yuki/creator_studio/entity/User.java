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
@Table(name = "m_users", indexes = @Index(name = "ix_m_users_is_deleted", columnList = "is_deleted"))
public class User extends AbstractAuditableEntity {

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Column(name = "email", length = 255)
	private String email;
}
