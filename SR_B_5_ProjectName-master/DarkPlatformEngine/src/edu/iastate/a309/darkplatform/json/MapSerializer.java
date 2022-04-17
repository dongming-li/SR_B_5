package edu.iastate.a309.darkplatform.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.iastate.a309.darkplatform.entity.Entity;
import edu.iastate.a309.darkplatform.map.Map;

import java.io.IOException;
import java.util.List;

public class MapSerializer extends StdSerializer<Map> {
    public MapSerializer() {
        this(null);
    }

    public MapSerializer(Class<Map> t) {
        super(t);
    }

    public void serialize(Map map, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("name", map.getName());
        jgen.writeObjectField("spawnPoint", map.spawnPoint);
        jgen.writeObjectField("numRooms", map.numRooms);
        jgen.writeObjectField("currentRoom", map.currentRoom);
        jgen.writeArrayFieldStart("clientSideEnts");
        jgen.writeStartObject();
        for (List<Entity> roomEnts : map.getClientSideEnts()) {
            jgen.writeNumberField("roomId", map.currentRoom);
            jgen.writeObjectField("roomEnts", roomEnts);
        }
        jgen.writeEndObject();
        jgen.writeEndArray();
        jgen.writeArrayFieldStart("serverSideEnts");
        jgen.writeStartObject();
        for (List<Entity> roomEnts : map.getServerSideEnts()) {
            jgen.writeNumberField("roomId", map.currentRoom + 1);
            jgen.writeObjectField("roomEnts", roomEnts);
        }
        jgen.writeEndObject();
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

}
