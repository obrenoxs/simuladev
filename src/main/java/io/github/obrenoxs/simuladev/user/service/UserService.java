package io.github.obrenoxs.simuladev.user.service;

import io.github.obrenoxs.simuladev.user.dto.request.UserRequest;
import io.github.obrenoxs.simuladev.user.dto.response.UserResponse;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.exception.EmailAlreadyExistsException;
import io.github.obrenoxs.simuladev.user.mapper.UserMapper;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponse create(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = userMapper.toEntity(request, encodedPassword);

        user = userRepository.save(user);

        return userMapper.toResponse(user);
    }
}
