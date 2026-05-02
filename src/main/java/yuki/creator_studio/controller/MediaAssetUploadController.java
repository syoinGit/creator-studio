package yuki.creator_studio.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yuki.creator_studio.controller.dto.UploadJobResponse;
import yuki.creator_studio.service.MediaAssetAsyncService;
import yuki.creator_studio.service.UploadJobService;

@RestController
@RequestMapping("/media-assets")
public class MediaAssetUploadController {

  private final UploadJobService uploadJobService;
  private final MediaAssetAsyncService mediaAssetAsyncService;

  public MediaAssetUploadController(
      UploadJobService uploadJobService,
      MediaAssetAsyncService mediaAssetAsyncService
  ) {
    this.uploadJobService = uploadJobService;
    this.mediaAssetAsyncService = mediaAssetAsyncService;
  }

  @PostMapping("/upload")
  public UploadJobResponse upload(@RequestParam("file") MultipartFile file) {
    String jobId = uploadJobService.createUploadJob(file);
    mediaAssetAsyncService.registerMediaAssetAsync(jobId);
    return new UploadJobResponse(jobId);
  }
}