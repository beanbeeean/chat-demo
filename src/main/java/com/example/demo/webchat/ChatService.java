package com.example.demo.webchat;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.demo.db.Lyric;
import com.example.demo.dto.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class ChatService {

    private final AmazonDynamoDBClient amazonDynamoDBClient;

    @Autowired
    public ChatService(AmazonDynamoDBClient amazonDynamoDBClient) {
        this.amazonDynamoDBClient = amazonDynamoDBClient;
    }

    public ChatRoom createChatRoom(String roomName, String userMaxCount , ArrayList<Map<String, String>> userList){


        try{
            ChatRoom chatRoom = new ChatRoom().create(roomName, Integer.parseInt(userMaxCount), userList); // 채팅룸 이름으로 채팅 룸 생성 후

            // Save Lyric To DynamoDB
            DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
            mapper.save(chatRoom);
            return chatRoom;

        } catch(Exception e){
            System.out.println("e:: " + e);;
            return null;
        }

    }


}
