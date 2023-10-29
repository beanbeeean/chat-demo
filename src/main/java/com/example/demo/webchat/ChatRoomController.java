package com.example.demo.webchat;


import com.example.demo.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class ChatRoomController {

    // ChatRepository Bean 가져오기
    @Autowired
    private ChatRepository chatRepository;


    @Autowired
    private ChatService chatService;

    // 채팅 리스트 화면(연결)
    // / 로 요청이 들어오면 전체 채팅룸 리스트를 담아서 return
//    @GetMapping("/")
//    @ResponseBody
//    public Map<String,Object> goChatRoom(){
//        Map<String,Object> returnMap = new HashMap<>();
//
//        returnMap.put("list", chatRepository.findAllRoom());
////        model.addAttribute("user", "hey");
//        log.info("SHOW ALL ChatList {}", chatRepository.findAllRoom());
//        return returnMap;
//    }

    // 채팅방 생성(연결)
    @PostMapping("/chat/createroom")
    @ResponseBody
    public Map<String,Object> createRoom(@RequestBody Map<String,String> roomInfo, RedirectAttributes rttr) {
        System.out.println("newName :: "+ roomInfo.get("newName"));
        System.out.println("maxCount :: "+ roomInfo.get("userMaxCount"));

        ArrayList<Map<String, String>> userList = new ArrayList<>();
        Map<String, String> userData = new HashMap<>();
        userData.put("userName", roomInfo.get("userName"));
        userData.put("joinDate",  LocalDate.now().toString());
        userList.add(userData);

        ChatRoom room = chatService.createChatRoom(roomInfo.get("newName"), roomInfo.get("userMaxCount"), userList);
        log.info("CREATE Chat Room {}", room);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("roomName",room);
//        rttr.addFlashAttribute("roomName", room);
        return returnMap;
    }

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
//    @GetMapping("/chat/room")
//    public String roomDetail(Model model, String roomId){
//
//        log.info("roomId {}", roomId);
//        model.addAttribute("room", chatRepository.findRoomById(roomId));
//        return "chatroom";
//    }

}