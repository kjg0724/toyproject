package co.dearu.practice.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatMessage {
    private int msg_idx;
    private int room_id;
    private int user_id;
    private String msg;
    private Timestamp last_msg_dt;
    private Boolean del_yn;
}
