package yuki.creator_studio.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties(S3StorageProperties.class)
public class AwsS3Configuration {

  @Bean
  @ConditionalOnProperty(prefix = "creator.storage.s3", name = "enabled", havingValue = "true")
  public S3Client s3Client(S3StorageProperties props) {
    if (!props.isConfigured()) {
      throw new IllegalStateException(
          "creator.storage.s3.enabled is true but creator.storage.s3.bucket-name/AWS_BUCKET_NAME is empty");
    }
    return S3Client.builder()
        .region(Region.of(props.getRegion()))
        .credentialsProvider(DefaultCredentialsProvider.builder().build())
        .build();
  }
}
