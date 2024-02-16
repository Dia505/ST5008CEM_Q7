package com.example.socialmediaapp.Dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userId;
    @NotNull
    private String fullName;
    @NotNull
    private String address;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
