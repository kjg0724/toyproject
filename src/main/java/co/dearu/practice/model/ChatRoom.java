package co.dearu.practice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class ChatRoom {
    private int room_id;
    private String room_nm;
    private String last_msg;
    private Timestamp last_updt_dt;
    private int create_user_id;
    private int user_count;
    private String email;
}
