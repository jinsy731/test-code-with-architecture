package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void User로_응답을_생성할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .lastLoginAt(1L)
                .build();
        //when
        UserResponse userResponse = UserResponse.from(user);
        //then
        assertThat(userResponse.getId()).isEqualTo(1L);
        assertThat(userResponse.getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(userResponse.getNickname()).isEqualTo("jinsy731");
        assertThat(userResponse.getLastLoginAt()).isEqualTo(1L);
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.PENDING);
    }
}