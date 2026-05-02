package yuki.creator_studio.service;

import java.nio.file.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yuki.creator_studio.entity.UploadJob;
import yuki.creator_studio.entity.UploadStatus;
import yuki.creator_studio.exception.ResourceNotFoundException;
import yuki.creator_studio.repository.UploadJobRepository;
import yuki.creator_studio.repository.UploadStatusRepository;

@Service
public class UploadJobService {
  private static final String PENDING_STORAGE_PATH = "__PENDING__";

  private final UploadJobRepository uploadJobRepository;
  private final UploadStatusRepository uploadStatusRepository;
  private final FileStorageService fileStorageService;

  public UploadJobService(
      UploadJobRepository uploadJobRepository,
      UploadStatusRepository uploadStatusRepository,
      FileStorageService fileStorageService
  ) {
    this.uploadJobRepository = uploadJobRepository;
    this.uploadStatusRepository = uploadStatusRepository;
    this.fileStorageService = fileStorageService;
  }

  @Transactional
  public String createUploadJob(MultipartFile file) {
    UploadJob job = new UploadJob();

    job.setFileName(file.getOriginalFilename());
    job.setStoragePath(PENDING_STORAGE_PATH);
    job.setUploadStatus(getStatus(UploadStatusCode.QUEUED));
    job.setCreatedUserId(SystemActor.SYSTEM_USER_ID);
    job.setUpdatedUserId(SystemActor.SYSTEM_USER_ID);

    uploadJobRepository.saveAndFlush(job);

    Path tempPath = fileStorageService.generateTempFilePath(
        job.getFileName(),
        job.getUuid()
    );

    job.setStoragePath(tempPath.toString());
    fileStorageService.saveToTemp(file, tempPath);

    return job.getUuid();
  }

  @Transactional
  public UploadJob findJob(String jobUuid) {
    return uploadJobRepository.findByUuidAndDeletedFalse(jobUuid)
        .orElseThrow(() -> new ResourceNotFoundException("UploadJob not found: " + jobUuid));
  }

  @Transactional
  public void updateStatus(String jobUuid, UploadStatusCode statusCode) {
    UploadJob job = findJob(jobUuid);
    job.setUploadStatus(getStatus(statusCode));
    job.setUpdatedUserId(SystemActor.SYSTEM_USER_ID);
  }

  private UploadStatus getStatus(UploadStatusCode statusCode) {
    return uploadStatusRepository.findByStatusCode(statusCode.getCode())
        .orElseThrow(() -> new ResourceNotFoundException("UploadStatus not found: " + statusCode.getCode()));
  }
}