package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {

    @Test
    void User_로_응답을_생성할_수_있다() {
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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);
        //then
        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("jinsy731");
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(1L);
        assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.PENDING);

    }
}