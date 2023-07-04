package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_개인정보를_제외한_정보를_전달받을_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jinsy731@gmail.com")
                        .nickname("jinsy731")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaa-aaa")
                        .lastLoginAt(10L)
                        .build());


        //when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jinsy731");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(10L);
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }
    
    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api를_호출할_때_404_응답을_받는다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.userController.getUserById(1);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jinsy731@gmail.com")
                        .nickname("jinsy731")
                        .address("Seoul")
                        .status(UserStatus.PENDING)
                        .certificationCode("aaa-aaa")
                        .lastLoginAt(10L)
                        .build());


        //when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaa-aaa");

        //then
        User user = testContainer.userRepository.getById(1L);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자가_일치하지_않는_인증코드로_인증요청할_경우_권한없음_에러를_발생시킨다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jinsy731@gmail.com")
                        .nickname("jinsy731")
                        .address("Seoul")
                        .status(UserStatus.PENDING)
                        .certificationCode("aaa-aaa")
                        .lastLoginAt(10L)
                        .build());


        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(1L, "aaa-bbb");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보룰_불러올_때_개인정보인_주소도_갖고올_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jinsy731@gmail.com")
                        .nickname("jinsy731")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaa-aaa")
                        .lastLoginAt(10L)
                        .build());


        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("jinsy731@gmail.com");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jinsy731");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jinsy731@gmail.com")
                        .nickname("jinsy731")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaa-aaa")
                        .lastLoginAt(10L)
                        .build());

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jinsy731-2")
                .address("Jeju")
                .build();

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("jinsy731@gmail.com", userUpdate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("jinsy731@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jinsy731-2");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(10L);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getAddress()).isEqualTo("Jeju");
    }
}
