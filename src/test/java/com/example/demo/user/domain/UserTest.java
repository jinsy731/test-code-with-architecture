package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void UserCreate_객체로_생성할_수_있고_초기_상태는_PENDING이어야_한다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .build();
        //when
        User result = User.from(userCreate);
        //then
        assertThat(result.getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(result.getNickname()).isEqualTo("jinsy731");
        assertThat(result.getAddress()).isEqualTo("Seoul");
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("...");
    }
    
    @Test
    void UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        //given
        User user = User.builder()
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Jeju")
                .nickname("jinsy731-2")
                .build();
        //when
        user = user.update(userUpdate);
        //then
        assertThat(user.getAddress()).isEqualTo("Jeju");
        assertThat(user.getNickname()).isEqualTo("jinsy731-2");
    }
    
    @Test
    void 로그인하면_lastLoginAt이_수정되어야_한다() {
        //given
        User user = User.builder()
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .build();
        //when
        user = user.login();
        //then
        assertThat(user.getLastLoginAt()).isGreaterThan(0);
    }

    @Test
    void 인증코드로_인증하면_상태가_ACTIVE로_변경되어야_한다() {
        //given
        User user = User.builder()
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .build();
        //when
        user = user.certificate("aaa-aaa");
        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 잘못된_인증코드로_인증하면_에러를_발생시킨다() {
        //given
        User user = User.builder()
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .build();
        //when
        //then
        assertThatThrownBy(() -> {
            user.certificate("!#@!#@!#");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}