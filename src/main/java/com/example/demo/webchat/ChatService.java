package com.example.demo.webchat;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.demo.db.Lyric;
import com.example.demo.dto.ChatDto;
import com.example.demo.dto.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final AmazonDynamoDBClient amazonDynamoDBClient;

    @Autowired
    IChatMapper iChatMapper;

    @Autowired
    public ChatService(AmazonDynamoDBClient amazonDynamoDBClient) {
        this.amazonDynamoDBClient = amazonDynamoDBClient;
    }

    public ChatRoom createChatRoom(String roomName, String userMaxCount, String userName){
        try{
            ChatRoom chatRoom = new ChatRoom().create(roomName, Integer.parseInt(userMaxCount)); // 채팅룸 이름으로 채팅 룸 생성 후

            // Save Lyric To DynamoDB
//            Map<String, String> user = userList.get(0);
//            String userName = user.get("userName");
            DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
//            System.out.println("RommID : "+  chatRoom.getRoomId());
            int result = iChatMapper.insertChatRoomForUser(userName, chatRoom.getRoomId());
            if(result > 0){
                mapper.save(chatRoom);
                return chatRoom;
            }else{
                return null;
            }

        } catch(Exception e){
            System.out.println("e:: " + e);;
            return null;
        }
    }


    // 유저가 포함된 채팅방 조회
    public List<ChatRoom> findRoomByUserMail(String userName){

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);

        List<String> roomIds = iChatMapper.getUsersChatRooms(userName);
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (String id: roomIds) {
            ChatRoom chatRoom = mapper.load(ChatRoom.class, id);
            chatRooms.add(chatRoom);
        }

//        System.out.println("chatRooms + "+ chatRooms);

        return chatRooms;
    }

    public ChatRoom findRoomByRoomId(String roomId){
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
        ChatRoom chatRoom = mapper.load(ChatRoom.class, roomId);
        return chatRoom;
    }

    // 채팅방 인원+1
    public int plusUserCnt(String roomId, String u_mail){

        int result = iChatMapper.isAlreadyJoined(roomId, u_mail);
        if(result < 1){
            DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
            ChatRoom chatRoom = mapper.load(ChatRoom.class, roomId);
            if(chatRoom.getUserMaxCount() == chatRoom.getUserCount()){
                result = -1;
                return result;
            }else{
                int insertSQL = iChatMapper.insertChatRoomForUser(u_mail,roomId);
                if(insertSQL > 0){
                    chatRoom.setUserCount(chatRoom.getUserCount()+1);
                    mapper.save(chatRoom);
                }
            }


        }
        return result;
    }

    public void saveChatList(ChatDto chatDto){
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
        ChatRoom chatRoom = mapper.load(ChatRoom.class, chatDto.getRoomId());
        ArrayList<Map<String,String>> oriMap = chatRoom.getChat();
        Map<String, String> map = new HashMap<>();
        map.put("user", chatDto.getSender());
        map.put("msg", chatDto.getMessage());
        map.put("time", LocalDateTime.now().toString());

        oriMap.add(map);
        chatRoom.setChat(oriMap);

        mapper.save(chatRoom);
    }

    // 전체 조회(임시)
    public List<ChatRoom> findRoomAllRoom() {
//
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);

        List<ChatRoom> chatRooms = mapper.scan(ChatRoom.class, new DynamoDBScanExpression());
        System.out.println("chatRooms :: "+ chatRooms);
        return chatRooms;
    }

//    채팅방 인원-1 & 나가기 따로 만들기
    public int minusUserCnt(String roomId, String u_mail){

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
        ChatRoom chatRoom = mapper.load(ChatRoom.class, roomId);
        int result = iChatMapper.deleteChatRoomForUser(u_mail,roomId);
        if(result > 0){
            chatRoom.setUserCount(chatRoom.getUserCount()-1);
            mapper.save(chatRoom);
        }
        return result;
    }
}
