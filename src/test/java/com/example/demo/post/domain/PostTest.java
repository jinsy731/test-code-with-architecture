package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    
    @Test
    void PostCreate_로_게시물을_만들_수_있다() {
        //given
        User writer = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .lastLoginAt(1L)
                .build();

        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello world")
                .build();

        //when
        Post post = Post.from(postCreate, writer);
        //then
        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getWriter()).usingRecursiveComparison().isEqualTo(writer);
//        assertThat(post.getCreatedAt()).isEqualTo("...");
    }
}