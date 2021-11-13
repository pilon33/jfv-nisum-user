package com.nisum.test.user.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
  @JsonProperty("name")
  @NotBlank(message = "Name is mandatory")
  private String name;

  @JsonProperty("email")
  @NotBlank(message = "Email is mandatory")
  private String email;

  @NotBlank(message = "Password is mandatory")
  @JsonProperty("password")
  private String password;

  @Valid
  @NotEmpty(message = "Phones are mandatory")
  @JsonProperty("phones")
  private List<PhoneDTO> phones;


}
