package co.dearu.practice.controller;

import co.dearu.practice.model.ChatMessage;
import co.dearu.practice.model.ChatRoom;
import co.dearu.practice.model.MyRoom;
import co.dearu.practice.service.ChatService;
import org.apache.catalina.session.StandardSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ChatController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

//    ChatRoomList View
    @GetMapping("/chat/chatRoomList/{id}")
    @ResponseBody
    public ModelAndView chatRoomList(@PathVariable int id, HttpSession session) {
        logger.debug("/chat/chatRoomList");
        List<ChatRoom> resultList = chatService.getChatRoomList();
        ModelAndView mav = new ModelAndView("chat/chatRoomList");
        mav.addObject("title", "chatRoomList");
        mav.addObject("user_id",id);
        mav.addObject("email",session.getAttribute("email"));
        mav.addObject("name",session.getAttribute("name"));
        //mav.addObject("sessionId",session.getAttribute("sessionId"));
        mav.addObject("chatRoomList",resultList);
        return mav;
    }

//    ChatRoom View
    @GetMapping("/chat/chatRoom/{room_id}")
    @ResponseBody
    public ModelAndView chatRoom(@PathVariable int room_id, HttpSession session) {
        logger.debug("/chat/chatRoom");
        MyRoom myRoom = new MyRoom();
        myRoom.setRoom_id(room_id);
        myRoom.setUser_id((int)session.getAttribute("id"));
        List<MyRoom> userList = chatService.getRoomUser(myRoom);
        ModelAndView mav = new ModelAndView("chat/chatRoom");
        mav.addObject("title", "chatRoom");
        mav.addObject("userList",userList);
        mav.addObject("room_id",room_id);
        mav.addObject("user_id",session.getAttribute("id"));
        mav.addObject("email",session.getAttribute("email"));
        mav.addObject("name",session.getAttribute("name"));
        return mav;
    }

    @GetMapping("/chat/getRoomUser")
    @ResponseBody
    public String getRoomUser(MyRoom myRoom){
        String resultList = chatService.getRoomUserSv(myRoom);
        return resultList;
    }

//    makeChatRoom
    @PutMapping("/chat/makeChatRoom")
    @ResponseBody
    public String makeRoom(ChatRoom chatRoom, HttpSession session){
        chatRoom.setCreate_user_id((int)session.getAttribute("id"));
        String result = chatService.makeChatRoom(chatRoom);
        chatService.makeMyRoom(chatRoom);
        return result;
    }

//    addMyRoom
    @PutMapping("/chat/addMyRoom")
    @ResponseBody
    public String addMyRoom(MyRoom myRoom){
        System.out.println("ChatController.addMyRoom");
        String resultList = chatService.addMyRoom(myRoom);
        System.out.println("ChatController.addMyRoom.resultList");
        return resultList;
    }

//    deleteMyRoom
    @DeleteMapping("/chat/deleteMyRoom")
    @ResponseBody
    public String deleteMyRoom(MyRoom myRoom){
        String resultList = chatService.deleteMyRoom(myRoom);
        return resultList;
    }


//    storeMessage
    @PutMapping("/chat/storeMessage")
    @ResponseBody
    public String storeMessage(ChatMessage chatMessage){
        String resultList = chatService.storeMessage(chatMessage);
        return resultList;
    }

//    getChatMessage
    @GetMapping("/chat/getChatMessage")
    @ResponseBody
    public String getChatMessage(int room_id){
        String resultList = chatService.getChatMessage(room_id);
        return resultList;
    }

//    deleteChatMessage
    @PostMapping("/chat/deleteMessage")
    @ResponseBody
    public String deleteMessage(int msg_idx){
        String resultList = chatService.deleteMessage(msg_idx);
        return resultList;
    }

//    restoreMessage
    @PostMapping("/chat/restoreMessage")
    @ResponseBody
    public String restoreMsg(int msg_idx){
        String resultList = chatService.restoreMessage(msg_idx);
        return resultList;
    }

//    getNav
    @GetMapping("/chat/getNav")
    @ResponseBody
    public ModelAndView getNav(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("common/chatNav");
        return mav;
    }
}