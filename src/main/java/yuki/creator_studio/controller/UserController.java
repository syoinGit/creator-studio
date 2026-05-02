package yuki.creator_studio.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuki.creator_studio.controller.dto.UserRegisterRequest;
import yuki.creator_studio.controller.dto.UserResponse;
import yuki.creator_studio.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public UserResponse registerUser(@RequestBody UserRegisterRequest request) {
    return userService.registerUser(request);
  }
}
