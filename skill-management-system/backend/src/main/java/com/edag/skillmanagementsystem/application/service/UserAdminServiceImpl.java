package com.edag.skillmanagementsystem.application.service;

import com.edag.skillmanagementsystem.domain.model.user.User;
import com.edag.skillmanagementsystem.domain.port.inbound.UserAdminService;
import com.edag.skillmanagementsystem.domain.port.outbound.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserAdminService interface.
 *
 * <p>This service provides administrative user management capabilities including paginated user
 * listings and search functionality. All operations are intended for administrators only.
 *
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl implements UserAdminService {

  private final UserRepository userRepository;

  @Override
  public Page<User> getAllUsers(Pageable pageable) {
    log.debug("Retrieving all users with pagination: {}", pageable);

    Page<User> users = userRepository.findAll(pageable);

    log.debug(
        "Retrieved {} users (page {} of {})",
        users.getNumberOfElements(),
        users.getNumber(),
        users.getTotalPages());

    return users;
  }

  @Override
  public Page<User> searchUsers(String searchTerm, Pageable pageable) {
    log.debug("Searching users with term '{}' and pagination: {}", searchTerm, pageable);

    Page<User> users = userRepository.searchByUsernameOrEmail(searchTerm, pageable);

    log.debug(
        "Found {} users matching '{}' (page {} of {})",
        users.getNumberOfElements(),
        searchTerm,
        users.getNumber(),
        users.getTotalPages());

    return users;
  }
}
