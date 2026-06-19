package com.food.delivery.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @JsonProperty("name")
    private String name;
    @Email
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}
