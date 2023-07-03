package com.example.demo.controller;

import com.example.demo.model.dto.PostUpdateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 게시글_번호로_게시글을_조회할_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("hello world"))
                .andExpect(jsonPath("$.writer.id").value(1))
                .andExpect(jsonPath("$.writer.email").value("jinsy731@gmail.com"))
                .andExpect(jsonPath("$.writer.nickname").value("jinsy731"));
    }

    @Test
    void 사용자가_존재하지_않는_게시글을_조회하려고_할_경우_에러가_발생한다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/posts/123131"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Posts에서 ID 123131를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_게시글을_수정할_수_있다() throws Exception {
        //given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("update post")
                .build();
        //when
        //then
        mockMvc.perform(put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postUpdateDto))
            )
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("update post"))
                .andExpect(jsonPath("$.writer.id").value(1))
                .andExpect(jsonPath("$.writer.email").value("jinsy731@gmail.com"))
                .andExpect(jsonPath("$.writer.nickname").value("jinsy731"));
    }

}
