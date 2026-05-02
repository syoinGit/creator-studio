package yuki.creator_studio.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MediaAssetAsyncService {

  private final MediaAssetRegistrationService mediaAssetRegistrationService;

  public MediaAssetAsyncService(MediaAssetRegistrationService mediaAssetRegistrationService) {
    this.mediaAssetRegistrationService = mediaAssetRegistrationService;
  }

  @Async("mediaAssetTaskExecutor")
  public void registerMediaAssetAsync(String jobId) {
    mediaAssetRegistrationService.registerMediaAsset(jobId);
  }
}
