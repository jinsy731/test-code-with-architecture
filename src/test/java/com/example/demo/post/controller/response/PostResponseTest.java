package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    void Post_로_응답을_생성할_수_있다() {
        //given
        User writer = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aaa")
                .lastLoginAt(1L)
                .build();
        Post post = Post.builder()
                .id(1L)
                .content("hello world")
                .writer(writer)
                .createdAt(10L)
                .modifiedAt(20L)
                .build();
        //when
        PostResponse postResponse = PostResponse.from(post);
        //then
        assertThat(postResponse.getId()).isEqualTo(1L);
        assertThat(postResponse.getContent()).isEqualTo("hello world");
        assertThat(postResponse.getWriter()).usingRecursiveComparison().isEqualTo(UserResponse.from(writer));
        assertThat(postResponse.getCreatedAt()).isEqualTo(10L);
        assertThat(postResponse.getModifiedAt()).isEqualTo(20L);
    }
}