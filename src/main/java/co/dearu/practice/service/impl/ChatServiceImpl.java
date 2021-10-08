package co.dearu.practice.service.impl;

import co.dearu.practice.model.ChatMessage;
import co.dearu.practice.model.ChatRoom;
import co.dearu.practice.model.MyRoom;
import co.dearu.practice.repository.ChatDao;
import co.dearu.practice.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChatDao ChatMapper;

    @Override
    public List<ChatRoom> getChatRoomList() {
        List<ChatRoom> resultList = ChatMapper.getChatRoomList();
        return resultList;
    }

    @Override
    public String makeChatRoom(ChatRoom chatRoom) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        int resultNum = ChatMapper.makeRoom(chatRoom);
        try{
            if(resultNum != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("resultCode", 0);
                jsonArray.put(jsonObject);
            }
            else{
                jsonObject = new JSONObject();
                jsonObject.put("resultCode",-1);
                jsonArray.put(jsonObject);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public void makeMyRoom(ChatRoom chatRoom) {
        ChatMapper.makeMyRoom(chatRoom);
    }

    @Override
    public String addMyRoom(MyRoom myRoom) {
        int resultNum = ChatMapper.addMyRoom(myRoom);
        System.out.println("ChatServiceImpl.addMyRoom.resultNum : " + resultNum);

        ChatMapper.updateChatRoom(myRoom.getRoom_id());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try{
            jsonObject = new JSONObject();
            jsonObject.put("msg",resultNum + " row updated");
            jsonArray.put(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public List<MyRoom> getRoomUser(MyRoom myRoom) {
        List<MyRoom> resultList = ChatMapper.getRoomUser(myRoom);
        return resultList;
    }

    @Override
    public String getRoomUserSv(MyRoom myRoom) {
        List<MyRoom> resultList = ChatMapper.getRoomUser(myRoom);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try{
            if(resultList != null){
                for(int i=0; i<resultList.size(); i++){
                    jsonObject = new JSONObject();
                    jsonObject.put("resultCode",0);
                    jsonObject.put("room_id",resultList.get(i).getRoom_id());
                    jsonObject.put("user_id",resultList.get(i).getUser_id());
                    jsonObject.put("email",resultList.get(i).getEmail());
                    jsonArray.put(jsonObject);
                }
            }
            else{
                jsonObject = new JSONObject();
                jsonObject.put("resultCode", -1);
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public String deleteMessage(int msg_idx) {
        int resultNum = ChatMapper.deleteMessage(msg_idx);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try{
            if(resultNum != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("resultCode", 0);
                jsonArray.put(jsonObject);
            }else{
                jsonObject = new JSONObject();
                jsonObject.put("resultCode", -1);
                jsonObject.put("msg","delete fail");
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public String restoreMessage(int msg_idx) {
        int resultNum = ChatMapper.restoreMessage(msg_idx);
        String msg = ChatMapper.getMessage(msg_idx);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try{
            if(resultNum != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("resultCode", 0);
                jsonObject.put("msg",msg);
                jsonArray.put(jsonObject);
            }else{
                jsonObject = new JSONObject();
                jsonObject.put("resultCode", -1);
                jsonObject.put("msg","restore fail");
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public String storeMessage(ChatMessage chatMessage) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date dateTime = new Date();
//        String time = sdf.format(dateTime);
//        chatMessage.setLast_msg_dt(time);
//        DB에서 기본값을 now()로 직접 기록
        int resultNum = ChatMapper.storeMessage(chatMessage);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
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
        return jsonArray.toString();
    }

    @Override
    public String getChatMessage(int room_id) {
        List<ChatMessage> resultList = ChatMapper.getChatMessage(room_id);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try{
            if(resultList != null){
                for(int i=0; i< resultList.size(); i++){
                    jsonObject = new JSONObject();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String last_msg_dt = simpleDateFormat.format(resultList.get(i).getLast_msg_dt());
                    jsonObject.put("resultCode",0);
                    jsonObject.put("msg_idx",resultList.get(i).getMsg_idx());
                    jsonObject.put("room_id",resultList.get(i).getRoom_id());
                    jsonObject.put("user_id",resultList.get(i).getUser_id());
                    jsonObject.put("msg",resultList.get(i).getMsg());
                    jsonObject.put("last_msg_dt",last_msg_dt);
                    jsonObject.put("del_yn",resultList.get(i).getDel_yn());
                    jsonArray.put(jsonObject);
                }
            }
            else{
                jsonObject = new JSONObject();
                jsonObject.put("resultCode",-1);
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public String deleteMyRoom(MyRoom myRoom) {
        int resultNum = ChatMapper.deleteMyRoom(myRoom);
        ChatMapper.updateChatRoom(myRoom.getRoom_id());
        ChatMapper.deleteChatRoom();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try{
            if(resultNum !=0){
                jsonObject = new JSONObject();
                jsonObject.put("msg",resultNum+" row deleted");
                jsonArray.put(jsonObject);
            }
            else{
                jsonObject = new JSONObject();
                jsonObject.put("msg","Cannot delete Room");
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
}
