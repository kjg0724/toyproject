package co.dearu.practice.service;

import co.dearu.practice.model.SignInResult;
import co.dearu.practice.model.User;
import org.springframework.boot.configurationprocessor.json.JSONObject;


import javax.servlet.http.HttpSession;
import java.util.List;

public interface AccountService {
    List<User> getUserList();
    String getSignInResult(String email, String pwd, String name, HttpSession session);
    String addUser(String email, String pwd, String name);
    String deleteUser(int id, String pwd);
    int getUserCount(String email);
    User updateUser(String email, String pwd);
}
