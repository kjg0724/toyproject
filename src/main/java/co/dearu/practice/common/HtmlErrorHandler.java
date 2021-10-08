package co.dearu.practice.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Order(ErrorHandlerBase.HtmlErrorHandlerOrder)
@ControllerAdvice(basePackages = "co.dearu.practice.controller")
public class HtmlErrorHandler extends ErrorHandlerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public Object handle(Exception ex, Model model, HttpServletRequest request) {

        logger.error(stackTraceToString(ex));

        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("message", ex.getMessage());

        return "error";
    }
}
