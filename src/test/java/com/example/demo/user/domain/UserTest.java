package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
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
        User user = User.from(userCreate, new TestUuidHolder("aaa-aaa"));
        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(user.getNickname()).isEqualTo("jinsy731");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaa-aaa");
    }
    
    @Test
    void UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .lastLoginAt(100L)
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Jeju")
                .nickname("jinsy731-2")
                .build();
        //when
        user = user.update(userUpdate);
        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(user.getCertificationCode()).isEqualTo("aaa-aaa");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getAddress()).isEqualTo("Jeju");
        assertThat(user.getNickname()).isEqualTo("jinsy731-2");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
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
        user = user.login(new TestClockHolder(100L));
        //then
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 인증코드로_인증하면_상태가_ACTIVE로_변경되어야_한다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .lastLoginAt(10L)
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