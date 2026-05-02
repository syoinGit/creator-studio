package yuki.creator_studio.service;

import java.nio.file.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuki.creator_studio.entity.MediaAsset;
import yuki.creator_studio.entity.UploadJob;
import yuki.creator_studio.repository.MediaAssetRepository;

@Service
public class MediaAssetRegistrationService {
  private final UploadJobService uploadJobService;
  private final MediaAssetRepository mediaAssetRepository;
  private final FileStorageService fileStorageService;

  public MediaAssetRegistrationService(
      UploadJobService uploadJobService,
      MediaAssetRepository mediaAssetRepository,
      FileStorageService fileStorageService
  ) {
    this.uploadJobService = uploadJobService;
    this.mediaAssetRepository = mediaAssetRepository;
    this.fileStorageService = fileStorageService;
  }

  public void registerMediaAsset(String jobUuid) {
    String s3Path = null;
    Path tempPath = null;

    try {
      uploadJobService.updateStatus(jobUuid, UploadStatusCode.PROCESSING);

      UploadJob job = uploadJobService.findJob(jobUuid);
      tempPath = Path.of(job.getStoragePath());

      MediaAsset mediaAsset = createMediaAsset(job);
      saveMediaAsset(mediaAsset);

      s3Path = fileStorageService.uploadToObjectStorage(
          tempPath,
          mediaAsset.getUuid(),
          mediaAsset.getSavedFileName()
      );

      mediaAsset.setStoragePath(s3Path);
      saveMediaAsset(mediaAsset);

      uploadJobService.updateStatus(jobUuid, UploadStatusCode.COMPLETED);

    } catch (Exception e) {
      if (s3Path != null) {
        fileStorageService.deleteStoredObjectUri(s3Path);
      }

      uploadJobService.updateStatus(jobUuid, UploadStatusCode.FAILED);

      throw e;
    } finally {
      cleanupTempDirectory(tempPath);
    }
  }

  @Transactional
  public void saveMediaAsset(MediaAsset mediaAsset) {
    mediaAssetRepository.saveAndFlush(mediaAsset);
  }

  private void cleanupTempDirectory(Path tempPath) {
    if (tempPath != null) {
      fileStorageService.deleteTempDirectory(tempPath);
    }
  }

  private MediaAsset createMediaAsset(UploadJob job) {
    MediaAsset mediaAsset = new MediaAsset();

    mediaAsset.setOriginalFileName(job.getFileName());
    mediaAsset.setSavedFileName(job.getFileName());
    mediaAsset.setMimeType("application/octet-stream");
    mediaAsset.setStoragePath(job.getStoragePath());
    mediaAsset.setFileSize(0L);
    mediaAsset.setMemo("");

    // TODO: リクエストDTOから受け取る
    mediaAsset.setProjectId(1L);
    mediaAsset.setMediaTypeId(1L);

    mediaAsset.setCreatedUserId(SystemActor.SYSTEM_USER_ID);
    mediaAsset.setUpdatedUserId(SystemActor.SYSTEM_USER_ID);

    return mediaAsset;
  }
}