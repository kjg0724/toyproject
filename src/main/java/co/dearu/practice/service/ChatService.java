package co.dearu.practice.service;

import co.dearu.practice.model.ChatMessage;
import co.dearu.practice.model.ChatRoom;
import co.dearu.practice.model.MyRoom;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface ChatService {
    List<ChatRoom> getChatRoomList();
    String makeChatRoom(ChatRoom chatRoom);
    void makeMyRoom(ChatRoom chatRoom);
    String storeMessage(ChatMessage chatMessage);
    String getChatMessage(int room_id);
    String deleteMyRoom(MyRoom myRoom);
    String addMyRoom(MyRoom myRoom);
    List<MyRoom> getRoomUser(MyRoom myRoom);
    String getRoomUserSv(MyRoom myRoom);
    String deleteMessage(int msg_idx);
    String restoreMessage(int msg_idx);
}
