package yuki.creator_studio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractAuditableEntity {

  // ===== 識別 =====
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "uuid", length = 36, nullable = false, unique = true, updatable = false)
  private String uuid;

  // ===== 監査 =====
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "created_user_id", length = 36, nullable = false, updatable = false)
  private String createdUserId;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Column(name = "updated_user_id", length = 36, nullable = false)
  private String updatedUserId;

  // ===== 論理削除 =====
  @Column(name = "is_deleted", nullable = false)
  private boolean deleted;

  @PrePersist
  protected void beforePersist() {
    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
    }

    if (createdAt == null) {
      createdAt = now;
    }

    if (updatedAt == null) {
      updatedAt = now;
    }

    if (createdUserId == null) {
      createdUserId = "system";
    }

    if (updatedUserId == null) {
      updatedUserId = createdUserId;
    }
    deleted = false;
  }

  @PreUpdate
  protected void beforeUpdate() {
    updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    if (updatedUserId == null) {
      updatedUserId = "system";
    }
  }
}