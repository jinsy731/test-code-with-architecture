package com.example.demo.controller;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_특정_유저의_개인정보를_제외한_정보를_전달받을_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("jinsy731@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("jinsy731"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
    
    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api를_호출할_때_404_응답을_받는다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 3를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화할_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/2/verify")
                .queryParam("certificationCode", "aa-aaa-a-aaa"))
                .andExpect(status().isFound());

        UserEntity userEntity = userRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자가_일치하지_않는_인증코드로_인증요청할_경우_권한없음_에러를_발생시킨다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/2/verify")
                        .queryParam("certificationCode", "aa-aaa123-a-aaa"))
                .andExpect(status().isForbidden());
    }

    @Test
    void 사용자는_내_정보룰_불러올_때_개인정보인_주소도_갖고올_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/me")
                .header("EMAIL", "jinsy731@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("jinsy731@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("jinsy731"))
                .andExpect(jsonPath("$.address").value("Seoul"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jinsy731-2")
                .address("Jeju")
                .build();
        //when
        //then
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "jinsy731@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("jinsy731@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("jinsy731-2"))
                .andExpect(jsonPath("$.address").value("Jeju"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
