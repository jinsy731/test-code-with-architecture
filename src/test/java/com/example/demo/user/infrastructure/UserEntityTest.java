package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void User로_UserEntity를_생성할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .lastLoginAt(100L)
                .certificationCode("aaa-aaa")
                .status(UserStatus.ACTIVE)
                .build();
        //when
        UserEntity userEntity = UserEntity.fromDomain(user);
        //then
        assertThat(userEntity.getId()).isEqualTo(user.getId());
        assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
        assertThat(userEntity.getNickname()).isEqualTo(user.getNickname());
        assertThat(userEntity.getAddress()).isEqualTo(user.getAddress());
        assertThat(userEntity.getLastLoginAt()).isEqualTo(user.getLastLoginAt());
        assertThat(userEntity.getCertificationCode()).isEqualTo(user.getCertificationCode());
        assertThat(userEntity.getStatus()).isEqualTo(user.getStatus());
    }

    @Test
    void UserEntity_로_User를_생성할_수_있다() {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("jinsy731@gmail.com");
        userEntity.setNickname("jinsy731");
        userEntity.setAddress("Seoul");
        userEntity.setLastLoginAt(1L);
        userEntity.setCertificationCode("aaa-aaa");
        userEntity.setStatus(UserStatus.ACTIVE);
        //when
        User user = userEntity.toDomain();
        //then
        assertThat(userEntity.getId()).isEqualTo(user.getId());
        assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
        assertThat(userEntity.getNickname()).isEqualTo(user.getNickname());
        assertThat(userEntity.getAddress()).isEqualTo(user.getAddress());
        assertThat(userEntity.getLastLoginAt()).isEqualTo(user.getLastLoginAt());
        assertThat(userEntity.getCertificationCode()).isEqualTo(user.getCertificationCode());
        assertThat(userEntity.getStatus()).isEqualTo(user.getStatus());
    }
}