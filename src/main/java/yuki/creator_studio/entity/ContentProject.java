package yuki.creator_studio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_content_projects")
public class ContentProject extends AbstractAuditableEntity {

	@Column(name = "project_name", length = 50, nullable = false)
	private String projectName;

	@Column(name = "project_summary", columnDefinition = "text")
	private String projectSummary;

	}
