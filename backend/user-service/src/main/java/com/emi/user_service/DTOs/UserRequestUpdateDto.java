package com.emi.user_service.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequestUpdateDto(
        @Schema(
                description = "Full name of the user",
                example = "Himanshu Kushwaha",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Name cannot be empty")
        String name,

        @Schema(
        description = "Profile image URL",
        example = "https://cdn.library.com/users/profile123.png"
        )
        String profileImgUrl,


        @Schema(
                description = "10 digit mobile number",
                example = "9876543210",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be a valid 10 digit number")
        Long phone
		) {

}
