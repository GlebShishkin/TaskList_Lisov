package com.example.tasklist.web.dto.user;

import com.example.tasklist.web.dto.validation.OnCreate;
import com.example.tasklist.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "User DTO")
public class UserDto {
    @Schema(
            description = "User id",
            example = "1"
    )
    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;
    @Schema(
            description = "User name",
            example = "John Doe"
    )
    @NotNull(message = "name must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name length must be smaller then 255 symbols.", groups = {OnUpdate.class, OnCreate.class})
    private String name;
    @Schema(
            description = "User email",
            example = "johndoe@gmail.com"
    )
    @NotNull(message = "Username must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Username length must be smaller then 255 symbols.", groups = {OnUpdate.class, OnCreate.class})
    private String username;
    @Schema(
            description = "User encrypted password"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String password;
    @Schema(
            description = "User password confirmation"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password Confirmation must be not null", groups = {OnCreate.class})
    private String passwordConfirmation;
}
