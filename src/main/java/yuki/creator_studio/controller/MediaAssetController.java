package yuki.creator_studio.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuki.creator_studio.entity.MediaAsset;
import yuki.creator_studio.service.MediaAssetService;

@RestController
@RequestMapping("/media-assets")
public class MediaAssetController {

  private final MediaAssetService service;

  public MediaAssetController(MediaAssetService service) {
    this.service = service;
  }

  @GetMapping
  public List<MediaAsset> getAll() {
    return service.findAll();
  }
}
