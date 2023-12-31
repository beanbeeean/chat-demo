package com.example.demo.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Stomp 를 통해 pub/sub 를 사용하면 구독자 관리가 알아서 된다!!
// 따라서 따로 세션 관리를 하는 코드를 작성할 필도 없고,
// 메시지를 다른 세션의 클라이언트에게 발송하는 것도 구현 필요가 없다!
@Data
@DynamoDBTable(tableName="Libooks-Chat")
//@Getter
//@AllArgsConstructor
public class ChatRoom {

    @DynamoDBHashKey
    private String roomId; // 채팅방 아이디

    @DynamoDBAttribute
    private String roomName; // 채팅방 이름

    @DynamoDBAttribute
    private long userCount; // 채팅방 인원수

//    @DynamoDBAttribute
//    private ArrayList<Map<String, String>> userList;

    @DynamoDBAttribute
    private int userMaxCount;

    @DynamoDBAttribute
    private ArrayList<Map<String, String>> chat;

    public ChatRoom create(String roomName, int userMaxCount){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        chatRoom.userCount = 1;
        chatRoom.userMaxCount = userMaxCount;

        ArrayList<Map<String, String>> chat = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("user", "ADMIN");
        map.put("msg", "채팅방이 개설되었습니다.");
        map.put("time", LocalTime.now().toString());
        chat.add(map);
        chatRoom.chat = chat;
//        chatRoom.userList = userList;

        return chatRoom;
    }

}