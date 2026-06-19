package com.food.delivery.service.ServiceImpl;

import com.food.delivery.repository.UserRepository;
import com.food.delivery.security.UserEntity;
import com.food.delivery.security.UserRequest;
import com.food.delivery.security.UserResponse;
import com.food.delivery.service.AuthenticationFacade;
import com.food.delivery.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity newUser = convertToEntity(request);
        userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    @Override
    public String findByUserId() {
       String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
       UserEntity loggedInUser =  userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
       return loggedInUser.getId();
    }

    private UserEntity convertToEntity(UserRequest userRequest){
        return  UserEntity.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .build();
    }

    private UserResponse convertToResponse(UserEntity registerUser){
        return UserResponse.builder()
                .id(registerUser.getId())
                .email(registerUser.getEmail())
                .name(registerUser.getName())
                .build();
    }
}
