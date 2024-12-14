package com.spring.dozen.auth.presentation.dto;

import com.spring.dozen.auth.application.dto.UserUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank
        @Size(min = 8, max = 15, message = "비밀번호은 최소 8자 이상, 15자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "비밀번호는 영어 대소문자, 숫자, 특수문자를 포함해야 합니다.")
        String password,

        @NotBlank
        String slackId
) {
    public UserUpdate toServiceDto() {
        return new UserUpdate(password, slackId);
    }
}
