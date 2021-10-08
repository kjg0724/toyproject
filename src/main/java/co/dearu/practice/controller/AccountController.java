package co.dearu.practice.controller;

import co.dearu.practice.common.CommonService;
import co.dearu.practice.model.User;
import co.dearu.practice.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class  AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

//    SignUp View
    @GetMapping("/account/signUp")
    @ResponseBody
    public ModelAndView signUpView() {
        logger.debug("/account/signUp");

        ModelAndView mav = new ModelAndView("account/signUp");
        mav.addObject("title", "signUp");
        return mav;
    }

//    SignIn View
    @GetMapping("/account/signIn")
    @ResponseBody
    public ModelAndView signInView() {
        logger.debug("/account/signIn");

        ModelAndView mav = new ModelAndView("account/signIn");
        mav.addObject("title", "signIn");
        return mav;
    }

//    DropOut View
    @GetMapping("/account/dropOut/{id}")
    @ResponseBody
    public ModelAndView dropOut(@PathVariable int id){
        ModelAndView mav = new ModelAndView("account/dropOut");
        mav.addObject("user_id",id);
        return mav;
    }

//    doSignOut
    @GetMapping("/account/doSignOut")
    @ResponseBody
    public ModelAndView doSignOut(HttpSession session){
        session.invalidate();
        ModelAndView mav = new ModelAndView("account/signIn");
        return mav;
    }

//    doSignUp
    @PutMapping("/account/doSignUp")
    @ResponseBody
    public String addUser(User user){
        String signUpResult = accountService.addUser(user.getEmail(), user.getPassword(), user.getName());
        return signUpResult;
    }

//    doSignIn
    // PostMapping
    @PostMapping("/account/doSignIn")
    @ResponseBody
    public String doSignIn(User user, HttpSession session) {
        String signInResult = accountService.getSignInResult(user.getEmail(), user.getPassword(), user.getName(), session);
        return signInResult;
    }

//    doDropOut
    @DeleteMapping("/account/doDropOut")
    @ResponseBody
    public String doDropOut(int user_id, String pwd, HttpSession session){
        session.invalidate();
        String dropOutResult = accountService.deleteUser(user_id,pwd);
        return dropOutResult;
    }

//    getUserList
    @GetMapping("/account/userList")
    @ResponseBody
    public Map<String, List<User>> getUserList() {
        List<User> userList = accountService.getUserList();
        Map<String,List<User>> userMap = new HashMap<String,List<User>>();
        userMap.put("result", userList);
        return userMap;
    }
}
