package com.aws.awsproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionRequest {
    @NotBlank
    private String sessionString;
}