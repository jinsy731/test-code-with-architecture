package com.example.demo.controller;

import com.example.demo.model.dto.UserCreateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JavaMailSender mailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    void 사용자는_회원가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        //given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .build();
        BDDMockito.doNothing().when(mailSender).send(BDDMockito.any(SimpleMailMessage.class));
        //when
        //then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("jinsy731@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("jinsy731"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
