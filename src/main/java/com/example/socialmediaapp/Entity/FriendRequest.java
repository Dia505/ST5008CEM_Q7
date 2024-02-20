package com.example.socialmediaapp.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "friend_request")
// Creation of table to store following status between users, mimicking graph data structure
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private String status;
}
