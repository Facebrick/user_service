package za.co.facebrick.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {

    private String failureReason;

    private List<String> failedInput;


}
