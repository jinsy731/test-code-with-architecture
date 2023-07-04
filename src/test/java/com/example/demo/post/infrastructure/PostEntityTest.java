package com.example.demo.post.infrastructure;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostEntityTest {

    @Test
    void Post로_PostEntity를_생성할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .lastLoginAt(100L)
                .certificationCode("aaa-aaa")
                .status(UserStatus.ACTIVE)
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("hello world")
                .writer(user)
                .createdAt(10L)
                .modifiedAt(100L)
                .build();
        //when
        PostEntity postEntity = PostEntity.fromDomain(post);
        //then
        assertThat(postEntity.getId()).isEqualTo(post.getId());
        assertThat(postEntity.getContent()).isEqualTo(post.getContent());
        assertThat(postEntity.getCreatedAt()).isEqualTo(post.getCreatedAt());
        assertThat(postEntity.getModifiedAt()).isEqualTo(post.getModifiedAt());
        assertThat(postEntity.getWriter()).usingRecursiveComparison().isEqualTo(UserEntity.fromDomain(post.getWriter()));
    }

    @Test
    void PostEntity로_Post를_생성할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .address("Seoul")
                .lastLoginAt(100L)
                .certificationCode("aaa-aaa")
                .status(UserStatus.ACTIVE)
                .build();

        PostEntity postEntity = new PostEntity();
        postEntity.setId(1L);
        postEntity.setContent("hello world");
        postEntity.setWriter(UserEntity.fromDomain(user));
        postEntity.setCreatedAt(10L);
        postEntity.setModifiedAt(100L);
        //when
        Post post = postEntity.toDomain();
        //then
        assertThat(postEntity.getId()).isEqualTo(post.getId());
        assertThat(postEntity.getContent()).isEqualTo(post.getContent());
        assertThat(postEntity.getCreatedAt()).isEqualTo(post.getCreatedAt());
        assertThat(postEntity.getModifiedAt()).isEqualTo(post.getModifiedAt());
        assertThat(postEntity.getWriter()).usingRecursiveComparison().isEqualTo(UserEntity.fromDomain(post.getWriter()));
    }
}