package za.co.facebrick.user.controller.model;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class RestUser {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}
