package co.dearu.practice.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MyRoom {
    private int room_id;
    private int user_id;
    private String email;
    private Timestamp join_dt;
}
