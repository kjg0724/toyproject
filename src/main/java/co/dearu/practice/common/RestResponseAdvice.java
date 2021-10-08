package co.dearu.practice.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice(basePackages = "co.dearu.practice.controller")
public class RestResponseAdvice implements ResponseBodyAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Response res = new Response(ResponseCode.RESULT_SUCCESS, body);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("result", res.getResult());
            jsonObject.put("data",res.getData());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
