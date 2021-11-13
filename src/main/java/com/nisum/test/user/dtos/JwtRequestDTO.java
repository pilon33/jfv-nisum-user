package com.nisum.test.user.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JwtRequestDTO {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;


}
