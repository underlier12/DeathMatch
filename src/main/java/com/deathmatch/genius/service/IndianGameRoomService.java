package com.deathmatch.genius.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deathmatch.genius.domain.IndianGameRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@RequiredArgsConstructor
public class IndianGameRoomService {
	
    private Map<String, IndianGameRoom> indianRooms = new LinkedHashMap<>();
    
    public List<String> findAllId() {
		return new LinkedList<>(indianRooms.keySet());
	}
 
    public List<IndianGameRoom> findAllRoom() {
        return new ArrayList<>(indianRooms.values());
    }
 
    public IndianGameRoom findRoomById(String roomId) {
        return indianRooms.get(roomId);
    }
 
    public IndianGameRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        IndianGameRoom indianRoom = IndianGameRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        indianRooms.put(randomId, indianRoom);
        log.info("create Room");
        return indianRoom;
    }
    
    public void destroyRoom(String roomId) {
    	indianRooms.remove(roomId);
    }
    
    public int countRoom() {
    	return indianRooms.size();
    }

}
