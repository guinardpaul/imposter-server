package com.guinardpouard.imposteur.domain.repository;

import com.guinardpouard.imposteur.domain.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findById(String roomId);

    void save(Room room);

    void delete(String roomId);

    List<Room> findAll();

    void deleteAll();
}
