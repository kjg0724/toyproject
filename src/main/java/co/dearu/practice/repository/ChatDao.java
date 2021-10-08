package co.dearu.practice.repository;

import co.dearu.practice.model.ChatMessage;
import co.dearu.practice.model.ChatRoom;
import co.dearu.practice.model.MyRoom;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ChatDao extends MySqlRepositoryBase {
    List<ChatRoom> getChatRoomList();
    int makeRoom(ChatRoom chatRoom);
    void makeMyRoom(ChatRoom chatRoom);
    int addMyRoom(MyRoom myRoom);
    int storeMessage(ChatMessage chatMessage);
    List<ChatMessage> getChatMessage(int room_id);
    int deleteMyRoom(MyRoom myRoom);
    void updateChatRoom(int room_id);
    void deleteChatRoom();
    List<MyRoom> getRoomUser(MyRoom myRoom);
    int deleteMessage(int msg_idx);
    int restoreMessage(int msg_idx);
    String getMessage(int msg_idx);
}
