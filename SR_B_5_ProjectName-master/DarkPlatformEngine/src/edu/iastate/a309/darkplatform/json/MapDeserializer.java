package edu.iastate.a309.darkplatform.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.iastate.a309.darkplatform.entity.*;
import edu.iastate.a309.darkplatform.map.Map;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapDeserializer extends StdDeserializer<Map> {
    public MapDeserializer() {
        this(null);
    }

    public MapDeserializer(Class<Map> t) {
        super(t);
    }

    public Map deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String name = (node.get("name").textValue());
        Vector2f spawnPoint = new Vector2f();
        spawnPoint.x = node.get("spawnPoint").get("x").intValue();
        spawnPoint.y = node.get("spawnPoint").get("y").intValue();
        int numRooms = node.get("numRooms").intValue();
        int currentRoom = node.get("currentRoom").intValue();
        List<ArrayList<Entity>> serverSideEnts = getRoomEnts(node, "serverSideEnts");
        List<ArrayList<Entity>> clientSideEnts = getRoomEnts(node, "clientSideEnts");
        return new Map(serverSideEnts, clientSideEnts, spawnPoint, name, numRooms, currentRoom);
    }

    private List<ArrayList<Entity>> getRoomEnts(JsonNode node, String loc) {
        List<ArrayList<Entity>> ents = new ArrayList<ArrayList<Entity>>();
        ArrayList<Entity> ent = new ArrayList<Entity>();
        for (JsonNode sjn : node.get(loc)) {
            for (JsonNode sjnr : sjn.get("roomEnts"))
                ent.add(getEntFromType(sjnr));
            ents.add(ent);
        }
        return ents;
    }

    private Entity getEntFromType(JsonNode node){
        Entity entity;
        Vector2f v = getVecFromNode(node);
        int type = node.get("type").intValue();
        if (type == 99)
            entity = new Player(v, node.get("respawning").booleanValue());
        else if (type == 1)
            entity = new Platform(v, node.get("direction").intValue());
        else if (type == 2)
            entity = new RollingSawblade(v, true);
        else if(type == 3)
            entity = new BlackHole(v);
        else if (type == 5) {
            entity = new Spike(v, node.get("direction").intValue(), true);
        }else if (type == 6) {
            entity = new Spawner(v, getEntFromType(node.get("entToSpawn")), node.get("spawnDelay").intValue());
        } else
            entity = new Entity(v);
        entity.setType(type);
        entity.setId(node.get("id").intValue());
        return entity;
    }

    private Vector2f getVecFromNode(JsonNode node){
        return new Vector2f(node.get("position").get("x").intValue(), node.get("position").get("y").intValue());
    }
}
