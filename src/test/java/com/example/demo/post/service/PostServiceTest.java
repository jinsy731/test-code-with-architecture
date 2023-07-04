package com.example.demo.post.service;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
public class PostServiceTest {

    private PostServiceImpl postService;

    @BeforeEach
    void init() {
        FakePostRepository postRepository = new FakePostRepository();
        FakeUserRepository userRepository = new FakeUserRepository();
        postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(new TestClockHolder(100L))
                .build();

        User user = userRepository.save(User.builder()
                .id(1L)
                .email("jinsy731@gmail.com")
                .nickname("jinsy731")
                .certificationCode("aa-aaa-a-a")
                .status(ACTIVE)
                .lastLoginAt(0L)
                .build());

        userRepository.save(User.builder()
                .id(2L)
                .email("jinsy732@gmail.com")
                .nickname("jinsy732")
                .certificationCode("aa-aaa-a-aaa")
                .status(PENDING)
                .lastLoginAt(0L)
                .build());

        postRepository.save(Post.builder()
                .id(1L)
                .writer(user)
                .content("hello world")
                .createdAt(16785306739558L)
                .modifiedAt(0L)
                .build());
    }
    @Test
    void getById_로_게시물을_찾아올_수_있다() {
        //given
        //when
        Post result = postService.getById(1);
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
        Post result = postService.create(postCreate);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("new post");
        assertThat(result.getWriter().getId()).isEqualTo(1);
        assertThat(result.getCreatedAt()).isEqualTo(100L);
    }

    @Test
    void postUpdateDto_를_사용하여_게시글을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("update post")
                .build();
        //when
        postService.update(1, postUpdate);
        //then
        Post post = postService.getById(1);
        assertThat(post.getContent()).isEqualTo("update post");
        assertThat(post.getModifiedAt()).isEqualTo(100L);
    }
}