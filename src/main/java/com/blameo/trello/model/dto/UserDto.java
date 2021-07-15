package com.blameo.trello.model.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    public String email;
    public String password;
    public String rePassword;
}
