package com.example.socialmediaapp.Repository;

import com.example.socialmediaapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUserByFullName(String fullName);

    @Query(value = "select * from users where email=?1", nativeQuery = true)
    Optional<User> getUserByEmail(String email);
}
