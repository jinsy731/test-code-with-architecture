package com.example.demo.post.infrastructure;

import com.example.demo.post.domain.Post;
import com.example.demo.user.infrastructure.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "modified_at")
    private Long modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity writer;

    public static PostEntity fromDomain(Post post) {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(post.getId());
        postEntity.setContent(post.getContent());
        postEntity.setWriter(UserEntity.fromDomain(post.getWriter()));
        postEntity.setCreatedAt(post.getCreatedAt());
        postEntity.setModifiedAt(post.getModifiedAt());
        return postEntity;
    }

    public Post toDomain() {
        return Post.builder()
                .id(id)
                .content(content)
                .writer(writer.toDomain())
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }
}