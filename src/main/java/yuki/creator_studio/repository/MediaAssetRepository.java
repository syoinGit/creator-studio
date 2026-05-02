package yuki.creator_studio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import yuki.creator_studio.entity.MediaAsset;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
  List<MediaAsset> findByDeletedFalse();

}
