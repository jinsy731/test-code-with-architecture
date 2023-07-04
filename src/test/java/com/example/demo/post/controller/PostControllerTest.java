package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class PostControllerTest {
    @Test
    void 게시글_번호로_게시글을_조회할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
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
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello world")
                .writer(writer)
                .createdAt(10L)
                .modifiedAt(20L)
                .build());
        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getById(1L);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("hello world");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(10L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(20L);
        assertThat(result.getBody().getWriter()).usingRecursiveComparison().isEqualTo(UserResponse.from(writer));
    }

    @Test
    void 사용자가_존재하지_않는_게시글을_조회하려고_할_경우_에러가_발생한다() {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.postController.getById(1L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시글을_수정할_수_있다() {
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
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello world")
                .writer(writer)
                .createdAt(10L)
                .modifiedAt(20L)
                .build());
        PostUpdate postUpdate = PostUpdate.builder()
                .content("update post")
                .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.update(1L, postUpdate);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("update post");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(10L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(100L);
        assertThat(result.getBody().getWriter()).usingRecursiveComparison().isEqualTo(UserResponse.from(writer));
    }

}
