package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static com.example.demo.model.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "jinsy731@gmail.com";
        //when
        UserEntity result = userService.getByEmail(email);
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
        UserEntity result = userService.getById(1);
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
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("jinsy733@gmail.com")
                .address("Seoul")
                .nickname("jinsy733")
                .build();
        //when
        UserEntity result = userService.create(userCreateDto);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("...");
    }
}