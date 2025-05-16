package com.dao.nbti.user.application.service;



import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.user.application.dto.UserCreateRequest;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    public void createUser(UserCreateRequest userCreateRequest) {
        log.info(userCreateRequest.toString());
        User user = modelMapper.map(userCreateRequest, User.class);
        if(userRepository.findByAccountId(user.getAccountId()).isPresent()){
            throw new UserException(ErrorCode.LOGIN_ID_ALREADY_EXISTS);
        }
        user.setEncodedPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        userRepository.save(user);
    }
}
