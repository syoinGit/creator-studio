package yuki.creator_studio.service;

public enum UploadStatusCode {
  QUEUED("10"),
  PROCESSING("20"),
  COMPLETED("30"),
  FAILED("40");

  private final String code;

  UploadStatusCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
