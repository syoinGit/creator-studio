package yuki.creator_studio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "creator.storage.s3")
public class S3StorageProperties {

  /**
   * When false (default in dev/test), uploads use a deterministic placeholder URI and skips AWS calls.
   */
  private boolean enabled = false;

  /**
   * Usually {@code ${AWS_BUCKET_NAME}} — may be either a bare bucket name or {@code arn:aws:s3:::bucket}.
   */
  private String bucketName = "";

  /** Usually {@code ${AWS_REGION}} (e.g. ap-northeast-1). */
  private String region = "ap-northeast-1";

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public boolean isConfigured() {
    String bucket = resolveEffectiveBucketName();
    return bucket != null && !bucket.isBlank();
  }

  /** Returns the logical bucket segment (never an ARN payload). */
  public String resolveEffectiveBucketName() {
    if (bucketName == null) {
      return "";
    }
    String raw = bucketName.trim();
    if (raw.startsWith("arn:aws:s3:::")) {
      return raw.substring("arn:aws:s3:::".length()).trim();
    }
    return raw;
  }
}
