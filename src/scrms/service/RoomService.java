package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.Room;
import scrms.model.RoomType;
import scrms.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages rooms on campus and exposes CRUD operations.
 */
public class RoomService implements CrudService<Room> {

    private final DataStore<Room> dataStore;
    private final List<Room> rooms;

    public RoomService() {
        this.dataStore = new DataStore<>("rooms.json", Room::fromJSON, Room::toJSON);
        this.rooms = new ArrayList<>(dataStore.load());
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms);
    }

    @Override
    public Room findById(String id) {
        return rooms.stream().filter(r -> r.getRoomId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Room create(Room room) {
        ValidationUtils.requireText(room.getName(), "Room name is required");
        ValidationUtils.requirePositiveNumber(room.getCapacity(), "Room capacity must be positive");
        rooms.add(room);
        persist();
        return room;
    }

    public Room create(String name, int capacity, RoomType type) {
        return create(Room.create(name, capacity, type));
    }

    @Override
    public Room update(Room room) {
        Room existing = findById(room.getRoomId());
        if (existing == null) {
            throw new ResourceNotFoundException("Room not found: " + room.getRoomId());
        }
        existing.setName(room.getName());
        existing.setCapacity(room.getCapacity());
        existing.setType(room.getType());
        persist();
        return existing;
    }

    @Override
    public void delete(String id) {
        Room room = findById(id);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found: " + id);
        }
        rooms.remove(room);
        persist();
    }

    public void reload() {
        rooms.clear();
        rooms.addAll(dataStore.load());
    }

    public void flush() {
        persist();
    }

    private void persist() {
        dataStore.save(rooms);
    }
}
