package co.dearu.practice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int result = ResponseCode.RESULT_SUCCESS;
    private Object data = null;
}
