package yuki.creator_studio.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import yuki.creator_studio.config.S3StorageProperties;
import yuki.creator_studio.exception.StorageOperationException;

@Service
public class FileStorageService {

  private static final Path TEMP_ROOT = Path.of("temp");

  private final S3StorageProperties s3Properties;
  private final S3Client s3ClientOrNull;

  public FileStorageService(
      S3StorageProperties s3Properties,
      ObjectProvider<S3Client> s3ClientProvider
  ) {
    this.s3Properties = s3Properties;
    this.s3ClientOrNull = s3ClientProvider.getIfAvailable();
  }

  public Path generateTempFilePath(String fileName, String jobUuid) {
    return TEMP_ROOT.resolve(jobUuid).resolve(baseFileName(fileName));
  }

  public void saveToTemp(MultipartFile file, Path path) {
    try {
      Files.createDirectories(path.getParent());
      file.transferTo(path);
    } catch (IOException e) {
      throw new StorageOperationException("Failed to save temporary upload file", e);
    }
  }

  public void deleteTempDirectory(Path tempPath) {
    try {
      Path targetDir = tempPath.getParent();
      if (targetDir != null && Files.exists(targetDir)) {
        try (var paths = Files.walk(targetDir)) {
          paths.sorted((a, b) -> b.compareTo(a))
              .forEach(p -> {
                try {
                  Files.deleteIfExists(p);
                } catch (IOException e) {
                  throw new StorageOperationException("Failed to delete temporary path: " + p, e);
                }
              });
        }
      }
    } catch (IOException e) {
      throw new StorageOperationException("Failed to delete temporary upload directory", e);
    }
  }

  public String uploadToObjectStorage(Path tempPath, String mediaAssetUuid, String fileName) {
    if (!s3Active()) {
      return placeholderObjectUri(mediaAssetUuid, baseFileName(fileName));
    }
    String bucket = s3Properties.resolveEffectiveBucketName();
    String key = objectKey(mediaAssetUuid, baseFileName(fileName));
    String contentType = probeContentType(tempPath);

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(contentType)
        .build();

    try {
      s3ClientOrNull.putObject(request, RequestBody.fromFile(tempPath));
    } catch (RuntimeException e) {
      throw new StorageOperationException("Failed to upload object to S3: s3://" + bucket + "/" + key, e);
    }
    return "s3://" + bucket + "/" + key;
  }

  /**
   * Best-effort delete for rollback. Accepts URIs produced by {@link #uploadToObjectStorage} or placeholder URIs (no-op).
   */
  public void deleteStoredObjectUri(String uri) {
    if (uri == null || uri.isBlank()) {
      return;
    }
    if (!uri.startsWith("s3://")) {
      return;
    }
    if (!s3Active()) {
      return;
    }

    ParsedS3Uri parsed = ParsedS3Uri.parse(uri);
    if (parsed == null) {
      return;
    }
    try {
      s3ClientOrNull.deleteObject(DeleteObjectRequest.builder()
          .bucket(parsed.bucket())
          .key(parsed.key())
          .build());
    } catch (RuntimeException e) {
      throw new StorageOperationException("Failed to delete object from S3: " + uri, e);
    }
  }

  private boolean s3Active() {
    return s3Properties.isEnabled()
        && s3Properties.isConfigured()
        && s3ClientOrNull != null;
  }

  private static String placeholderObjectUri(String mediaAssetUuid, String safeFileName) {
    return "s3://placeholder/media-assets/" + mediaAssetUuid + "/" + safeFileName;
  }

  private static String objectKey(String mediaAssetUuid, String safeFileName) {
    return "media-assets/" + mediaAssetUuid + "/" + safeFileName;
  }

  private static String baseFileName(String fileName) {
    if (fileName == null || fileName.isBlank()) {
      return "upload.bin";
    }
    String base = Paths.get(fileName).getFileName().toString();
    if (base.isBlank()) {
      return "upload.bin";
    }
    return base.replaceAll("[\\\\/:]+", "_");
  }

  private static String probeContentType(Path path) {
    try {
      String probed = Files.probeContentType(path);
      return (probed == null || probed.isBlank()) ? "application/octet-stream" : probed;
    } catch (IOException e) {
      return "application/octet-stream";
    }
  }

  private record ParsedS3Uri(String bucket, String key) {

    static ParsedS3Uri parse(String s3Uri) {
      String withoutScheme = s3Uri.substring("s3://".length());
      int slash = withoutScheme.indexOf('/');
      if (slash <= 0 || slash >= withoutScheme.length() - 1) {
        return null;
      }
      String bucket = withoutScheme.substring(0, slash);
      String key = withoutScheme.substring(slash + 1);
      return new ParsedS3Uri(bucket, key);
    }
  }
}
