package yuki.creator_studio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yuki.creator_studio.entity.UploadStatus;

public interface UploadStatusRepository extends JpaRepository<UploadStatus, Long> {
  Optional<UploadStatus> findByStatusCode(String statusCode);
}
