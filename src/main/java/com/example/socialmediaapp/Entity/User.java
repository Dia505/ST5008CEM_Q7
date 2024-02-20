package com.example.socialmediaapp.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_user_email", columnNames = "email")
})

public class User {

    @Id
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "users_seq_gen", strategy = GenerationType.SEQUENCE)
    private Integer userId;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private boolean followingMatrix[][];

//    @PostLoad
//    private void initializeFollowingMatrix() {
//        this.followingMatrix = new boolean[100][100];
//    }

//    public void follow(User receiver) {
//        int receiverIndex = getIndexInFollowingMatrix(receiver);
//        if (receiverIndex != -1) {
//            followingMatrix[userId][receiverIndex] = true;
//        }
//    }
//
//    private int getIndexInFollowingMatrix(User user) {
//        return user.getUserId();
//    }
}
