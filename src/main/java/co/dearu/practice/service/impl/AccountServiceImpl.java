package co.dearu.practice.service.impl;

import co.dearu.practice.common.CommonService;
import co.dearu.practice.common.Response;
import co.dearu.practice.model.SignInResult;
import co.dearu.practice.model.User;
import co.dearu.practice.repository.AccountDao;
import co.dearu.practice.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountDao AccountMapper;

    @Autowired
    private CommonService commonService;

    @Override
    public List<User> getUserList() {
        return AccountMapper.getUserList();
    }

    @Override
    public String getSignInResult(String email, String pwd, String name, HttpSession session) {
        User user = AccountMapper.getUser(email);
        JSONArray jsonArray = new JSONArray();
        try{
            JSONObject jsonObject;
            if(user == null){
                jsonObject = new JSONObject();
                jsonObject.put("resultCode",-2);
                jsonArray.put(jsonObject);
            }
            else {
                if (commonService.matches(pwd, user.getPassword()) == true && email.equals(user.getEmail())) {
                    jsonObject = new JSONObject();
                    session.setAttribute("id", user.getId());
                    session.setAttribute("email", user.getEmail());
                    session.setAttribute("name", user.getName());
                    //session.setAttribute("sessionId",session.getId());
                    jsonObject.put("id", user.getId());
                    jsonObject.put("email", user.getEmail());
                    jsonObject.put("name", user.getName());
                    jsonObject.put("resultCode", 0);
                    jsonArray.put(jsonObject);
                } else {
                    jsonObject = new JSONObject();
                    jsonObject.put("resultCode", -1);
                    jsonArray.put(jsonObject);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return jsonArray.toString();
    }

    @Override
    public String addUser(String email, String pwd, String name) {
        String ePwd = commonService.encode(pwd);
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject;
            if (AccountMapper.getUserCount(email) >= 1) {
                jsonObject = new JSONObject();
                jsonObject.put("resultCode",-1);
                jsonArray.put(jsonObject);
            } else{
                AccountMapper.addUser(email, ePwd, name);
                jsonObject = new JSONObject();
                jsonObject.put("resultCode",0);
                jsonArray.put(jsonObject);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public String deleteUser(int id, String pwd) {
        int resultNum;
        User user = AccountMapper.getUserById(id);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        if(commonService.matches(pwd,user.getPassword())){
            resultNum = AccountMapper.deleteUser(user.getId());
            try{
                if(resultNum != 0){
                    jsonObject = new JSONObject();
                    jsonObject.put("resultCode",0);
                    jsonArray.put(jsonObject);
                }
                else{
                    jsonObject = new JSONObject();
                    jsonObject.put("resultCode",-1);
                    jsonArray.put(jsonObject);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            try{
                jsonObject = new JSONObject();
                jsonObject.put("resultCode",-2);
                jsonArray.put(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }
    @Override
    public int getUserCount(String email) {
        return AccountMapper.getUserCount(email);
    }

    @Override
    public User updateUser(String email, String pwd) {
        //AccountMapper.updateUser(email, pwd);
        return null;
    }
}
