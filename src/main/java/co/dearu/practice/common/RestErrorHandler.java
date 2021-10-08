package co.dearu.practice.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Order(ErrorHandlerBase.RestErrorHandlerOrder)
@ControllerAdvice(basePackages = "co.dearu.practice.controller")
public class RestErrorHandler extends ErrorHandlerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handle(Exception ex) {

        logger.error(stackTraceToString(ex));

        return new ErrorResponse(ResponseCode.RESULT_ERR, -1, ex.toString());
    }
}
