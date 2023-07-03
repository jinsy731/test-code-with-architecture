package com.example.demo.service;

import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostService;
import com.example.demo.post.infrastructure.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostServiceTest {

    @Autowired
    private PostService postService;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getById_로_게시물을_찾아올_수_있다() {
        //given
        //when
        PostEntity result = postService.getById(1);
        //then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo("hello world");
        assertThat(result.getWriter().getEmail()).isEqualTo("jinsy731@gmail.com");
    }

    @Test
    void postCreateDto_를_사용하여_게시글을_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .content("new post")
                .writerId(1)
                .build();
        //when
        PostEntity result = postService.create(postCreate);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("new post");
        assertThat(result.getWriter().getId()).isEqualTo(1);
        assertThat(result.getCreatedAt()).isGreaterThan(0);
    }

    @Test
    void postUpdateDto_를_사용하여_게시글을_생성할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("update post")
                .build();
        //when
        postService.update(1, postUpdate);
        //then
        PostEntity postEntity = postService.getById(1);
        assertThat(postEntity.getContent()).isEqualTo("update post");
        assertThat(postEntity.getModifiedAt()).isGreaterThan(0);
    }
}