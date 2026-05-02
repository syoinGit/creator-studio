package yuki.creator_studio.service;

import java.util.List;
import org.springframework.stereotype.Service;
import yuki.creator_studio.entity.MediaAsset;
import yuki.creator_studio.repository.MediaAssetRepository;

@Service
public class MediaAssetService {

  private final MediaAssetRepository repository;

  public MediaAssetService(MediaAssetRepository repository) {
    this.repository = repository;
  }

  public List<MediaAsset> findAll() {
    return repository.findByDeletedFalse();
  }
}