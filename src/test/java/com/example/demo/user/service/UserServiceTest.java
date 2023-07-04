package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;

public class UserServiceTest {

    // Test Fixture
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository userRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaa-aaa"))
                .clockHolder(new TestClockHolder(100L))
                .userRepository(userRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        userRepository.save(User.builder()
                        .id(1L)
                        .email("jinsy731@gmail.com")
                        .nickname("jinsy731")
                        .certificationCode("aa-aaa-a-a")
                        .status(ACTIVE)
                        .lastLoginAt(0L)
                .build());

        userRepository.save(User.builder()
                        .id(2L)
                        .email("jinsy732@gmail.com")
                        .nickname("jinsy732")
                        .certificationCode("aa-aaa-a-aaa")
                        .status(PENDING)
                        .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "jinsy731@gmail.com";
        //when
        User result = userService.getByEmail(email);
        //then
        assertThat(result.getNickname()).isEqualTo("jinsy731");
    }

    @Test
    void getByEmail_은_PENDING_상태인_유저는_찾아올_수_없다() {
        //given
        String email = "jinsy732@gmail.com";
        //when
        //then
        assertThatThrownBy(() -> {
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);


    }@Test
    void getById_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        //when
        User result = userService.getById(1);
        //then
        assertThat(result.getNickname()).isEqualTo("jinsy731");
    }

    @Test
    void getById_은_PENDING_상태인_유저는_찾아올_수_없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("jinsy733@gmail.com")
                .address("Seoul")
                .nickname("jinsy733")
                .build();
        //when
        User result = userService.create(userCreate);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaa-aaa");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Incheon")
                .nickname("jinsy731-1")
                .build();
        //when
        userService.update(1, userUpdate);
        //then
        User user = userService.getById(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress()).isEqualTo("Incheon");
        assertThat(user.getNickname()).isEqualTo("jinsy731-1");
    }
    
    @Test
    void login_하면_마지막_로그인_시간이_변경된다() {
        //given
        //when
        userService.login(1);
        //then
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isGreaterThan(0L);
        // 의존성이 숨겨져있어 로그인 시간 검증 불가.. -> 후에 리팩토링
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
        //given
        //when
        userService.verifyEmail(1, "aa-aaa-a-a");
        //then
        User user = userService.getById(1);
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증코드로_인증을_시도하면_에러를_던진다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(1, "aa-aaa-a-aqqq");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}