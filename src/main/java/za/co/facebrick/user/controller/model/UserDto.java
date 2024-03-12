package za.co.facebrick.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class UserDto {

    @Null
    private Long id;

    @NotBlank(message = "Firstname cannot be blank or just whitespaces")
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank or just whitespaces")
    private String lastName;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email cannot be blank or just whitespaces")
    private String email;

}
