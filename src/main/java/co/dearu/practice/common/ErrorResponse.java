package co.dearu.practice.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int result = ResponseCode.RESULT_ERR;
    private int errorCode = 0;
    private String errorMessage = "";
}
