package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        Post post = Post.from(postCreate, writer, new TestClockHolder(100L));
        //then
        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getWriter()).usingRecursiveComparison().isEqualTo(writer);
        assertThat(post.getCreatedAt()).isEqualTo(100L);
    }

    @Test
    void PostUpdate로_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("update post")
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

        Post post = Post.builder()
                .id(1L)
                .writer(writer)
                .content("hello world")
                .createdAt(1L)
                .modifiedAt(1L)
                .build();
        //when
        post = post.update(postUpdate, new TestClockHolder(100L));
        //then
        assertThat(post.getId()).isEqualTo(1L);
        assertThat(post.getWriter()).usingRecursiveComparison().isEqualTo(writer);
        assertThat(post.getContent()).isEqualTo("update post");
        assertThat(post.getCreatedAt()).isEqualTo(1L);
        assertThat(post.getModifiedAt()).isEqualTo(100L);
    }
}