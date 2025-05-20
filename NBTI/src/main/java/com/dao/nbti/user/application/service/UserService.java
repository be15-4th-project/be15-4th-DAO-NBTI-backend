package com.dao.nbti.user.application.service;



import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.user.application.dto.IdDuplicateResponse;
import com.dao.nbti.user.application.dto.UserCreateRequest;
import com.dao.nbti.user.application.dto.UserInfoResponse;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserCreateRequest userCreateRequest) {
        User user = modelMapper.map(userCreateRequest, User.class);
        if(userRepository.findByAccountId(user.getAccountId()).isPresent()){
            throw new UserException(ErrorCode.LOGIN_ID_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        user.delete();
    }

    @Transactional
    public IdDuplicateResponse checkAccountId(String accountId) {
        boolean isDuplicate = userRepository.findByAccountId(accountId).isPresent();

        return IdDuplicateResponse.builder()
                .isDuplicate(isDuplicate)
                .build();
    }

    public UserInfoResponse getUserInfo(String username) {
        int userId = Integer.parseInt(username);
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );

        UserInfoResponse response = modelMapper.map(user, UserInfoResponse.class);
        return response;
    }
}
