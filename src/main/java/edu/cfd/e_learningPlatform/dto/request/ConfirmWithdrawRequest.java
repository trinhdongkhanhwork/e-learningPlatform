package edu.cfd.e_learningPlatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmWithdrawRequest {
    @NotBlank(message = "User ID is required")
    String userId;

    @NotBlank(message = "OTP is required")
    String otp;

    @NotNull(message = "Withdraw ID is required")
    Long withdrawId;
}