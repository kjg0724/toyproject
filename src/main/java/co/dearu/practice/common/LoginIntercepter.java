//로그인 실패시 처리

package co.dearu.practice.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginIntercepter implements HandlerInterceptor {


    public List loginList = Arrays.asList("/chat/**");
    public List exceptList = Arrays.asList("/chat/getNav");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String email;
        //int id;
        if (session != null) {
            email = (String) session.getAttribute("email");
            //id = (int)session.getAttribute("id");
            if (email != null) {
                //response.sendRedirect("/chat/chatRoomList/"+id);
                return true;
            }else {
                response.sendRedirect("/account/signIn");
                return false;
            }
        }
        else {
            response.sendRedirect("/account/signIn");
            return false;
        }
    }
}
