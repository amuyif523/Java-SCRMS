package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.util.Map;

/**
 * Represents a physical room that can host courses and events.
 */
public class Room {

    private final String roomId;
    private String name;
    private int capacity;
    private RoomType type;

    public Room(String roomId, String name, int capacity, RoomType type) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
    }

    public static Room create(String name, int capacity, RoomType type) {
        return new Room(IdGenerator.newId("ROM"), name, capacity, type);
    }

    public String getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String toJSON() {
        return "{" + "\"roomId\":" + JsonUtils.quote(roomId) + ","
                + "\"name\":" + JsonUtils.quote(name) + ","
                + "\"capacity\":" + capacity + ","
                + "\"type\":" + JsonUtils.quote(type.name()) + "}";
    }

    public static Room fromJSON(String json) {
        Map<String, String> map = JsonUtils.parseJsonObject(json);
        String id = JsonUtils.unquote(map.get("roomId"));
        String name = JsonUtils.unquote(map.get("name"));
        int capacity = Integer.parseInt(map.get("capacity"));
        RoomType type = RoomType.valueOf(JsonUtils.unquote(map.get("type")));
        return new Room(id, name, capacity, type);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", type=" + type +
                '}';
    }
}
