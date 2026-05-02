package yuki.creator_studio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuki.creator_studio.controller.dto.UserRegisterRequest;
import yuki.creator_studio.controller.dto.UserResponse;
import yuki.creator_studio.entity.User;
import yuki.creator_studio.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public UserResponse registerUser(UserRegisterRequest request) {
    String normalizedEmail = normalizeEmail(request.email());

    if (userRepository.existsByEmailAndDeletedFalse(normalizedEmail)) {
      // TODO: Move this to a dedicated exception handled by a GlobalExceptionHandler.
      throw new RuntimeException("Email already exists: " + normalizedEmail);
    }

    User user = new User();
    user.setName(request.name());
    user.setEmail(normalizedEmail);
    user.setCreatedUserId("system");
    user.setUpdatedUserId("system");

    // TODO: Extend m_users for Entra ID linkage (subject / oid / tenantId / preferredUsername).
    // TODO: Current implementation supports local registration only.
    // TODO: Revisit whether email or Entra oid should be the long-term unique key.

    User savedUser = userRepository.save(user);
    return new UserResponse(savedUser.getUuid(), savedUser.getName(), savedUser.getEmail());
  }

  private String normalizeEmail(String email) {
    if (email == null) {
      return null;
    }
    return email.trim().toLowerCase();
  }
}
