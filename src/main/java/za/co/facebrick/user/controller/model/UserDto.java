package za.co.facebrick.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class UserDto {

    @NotNull(message = "ID Must have a value")
    private Long id;

    @NotBlank(message = "Firstname cannot be blank or just whitespaces")
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank or just whitespaces")
    private String lastName;

    @Email(message = "Please enter a valid email address")
    private String email;

}
