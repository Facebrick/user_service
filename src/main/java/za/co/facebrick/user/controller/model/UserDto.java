package za.co.facebrick.user.controller.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}
