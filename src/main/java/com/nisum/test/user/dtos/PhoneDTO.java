package com.nisum.test.user.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneDTO {
    @NotBlank(message = "id is mandatory")
    @JsonProperty("id")
    private Long id;
    @NotBlank(message = "Password is mandatory")
    @JsonProperty("number")
    private String number;
    @NotBlank(message = "CityCode is mandatory")
    @JsonProperty("cityCode")
    private String cityCode;
    @NotBlank(message = "CountryCode is mandatory")
    @JsonProperty("countryCode")
    private String countryCode;
}
