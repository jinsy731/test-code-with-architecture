package com.example.demo.post.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PostCreateControllerTest {

    @Test
    void 사용자는_게시글을_등록할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .build();
        User writer = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .lastLoginAt(1L)
                .build();
        testContainer.userRepository.save(writer);

        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .content("hello world")
                .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.create(postCreate);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("hello world");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getModifiedAt()).isNull();
        assertThat(result.getBody().getWriter()).usingRecursiveComparison().isEqualTo(UserResponse.from(writer));
    }
}
