package com.example.socialmediaapp.Entity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private String postImage;
    private String title;
    private String hashtag;
    private Integer likes;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Post(String title, String hashtag, User user) {
        this.title = title;
        this.hashtag = hashtag;
        this.likes = 0;
        this.user = user;
    }
}
