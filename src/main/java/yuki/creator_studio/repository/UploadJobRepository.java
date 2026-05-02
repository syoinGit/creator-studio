package yuki.creator_studio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yuki.creator_studio.entity.UploadJob;

public interface UploadJobRepository extends JpaRepository<UploadJob, Long> {
  Optional<UploadJob> findByUuidAndDeletedFalse(String uuid);
}
