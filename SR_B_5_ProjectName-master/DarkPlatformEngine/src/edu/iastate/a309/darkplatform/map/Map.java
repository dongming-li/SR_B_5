package edu.iastate.a309.darkplatform.map;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.iastate.a309.darkplatform.display.Frame;
import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.entity.*;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.gravity.GravityField;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.json.MapDeserializer;
import edu.iastate.a309.darkplatform.json.MapSerializer;
import edu.iastate.a309.darkplatform.utility.Vector2f;
import org.springframework.boot.jackson.JsonComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonComponent
@JsonSerialize(using = MapSerializer.class)
@JsonDeserialize(using = MapDeserializer.class)
public class Map {
    private List<ArrayList<Entity>> serverSideEnts;
    private List<ArrayList<Entity>> clientSideEnts;

    private List<Entity> entsToRemove;
    private List<Entity> entsToAdd;

    @JsonIgnore
    public GravityField gravity;

    public Vector2f spawnPoint;

    private String name;
    @JsonIgnore
    private static final int width = 60;
    @JsonIgnore
    private static final int height = 33;

    public int numRooms;
    public int currentRoom;

    @JsonIgnore
    private Sprite background;

    public Map() {


    }

    public Map(List<ArrayList<Entity>> serverSideEnts, List<ArrayList<Entity>> clientSideEnts,
               Vector2f spawnPoint, String name, int numRooms, int currentRoom) {
        this.serverSideEnts = serverSideEnts;
        System.out.println(this.serverSideEnts.size());
        this.clientSideEnts = clientSideEnts;
        this.spawnPoint = spawnPoint;
        this.name = name;
        this.numRooms = numRooms;
        this.currentRoom = currentRoom;
        gravity = new GravityField();
        for (Entity e : clientSideEnts.get(currentRoom - 1)) {
            if (e instanceof BlackHole) {
                gravity.addBlackHole(new Vector2f(e.getPosition().x + 8, e.getPosition().y + 8));
            }
        }
        background = new Sprite(ArtManager.mapBackground, 0, 0, 1920, 1080);
        entsToRemove = new ArrayList<Entity>();
        entsToAdd = new ArrayList<Entity>();
    }

    public Map(String name) {
        this.name = name;
        gravity = new GravityField();
        try {
            loadMapFromFile(name + ".dpm");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Entity e : clientSideEnts.get(currentRoom - 1)) {
            if (e instanceof BlackHole) {
                gravity.addBlackHole(new Vector2f(e.getPosition().x + 8, e.getPosition().y + 8));
            }
        }
        background = new Sprite(ArtManager.mapBackground, 0, 0, 1920, 1080);
        entsToRemove = new ArrayList<Entity>();
        entsToAdd = new ArrayList<Entity>();
    }

    public void update(InputHandler input) {
        for (Entity e : clientSideEnts.get(currentRoom - 1)) {
            e.update(input);
            if(GlobalFields.EDIT_MODE && e.isCloned()){
                entsToRemove.add(e);
            }
        }
        for (Entity e : entsToRemove) {
            clientSideEnts.get(currentRoom - 1).remove(e);
        }
        for(Entity e : entsToAdd){
            clientSideEnts.get(currentRoom - 1).add(e);
        }
        entsToAdd.clear();
        entsToRemove.clear();
    }

    public void render(Screen screen) {
        int horizontalBGs = (int) Math.ceil((float) Frame.width / (float) background.width);
        int verticalBGs = (int) Math.ceil((float) Frame.height / (float) background.height);
        for (int i = 0; i < horizontalBGs; i++) {
            for (int k = 0; k < verticalBGs; k++) {
                screen.render(background, new Vector2f(Frame.camera.offset.x + background.width * i, Frame.camera.offset.y + background.height * k));
            }
        }
        for (Entity e : clientSideEnts.get(currentRoom - 1)) {
            if(e == null) continue;
            e.render(screen);
        }
    }

    private void loadMapFromFile(String path) throws IOException {
        String[] pathSplit = path.split(".");
        if (pathSplit.length == 2 && !pathSplit[1].matches("dpm")) {
            throw new IOException("");
        }

        BufferedReader reader = new BufferedReader(new FileReader(path));

        String line = reader.readLine();

        int i = 0;
        while (line != null) {
            if (i == 0) {
                //first line of a dpm file contains the numRooms variable
                numRooms = Integer.parseInt(line);

                serverSideEnts = new ArrayList<ArrayList<Entity>>();
                clientSideEnts = new ArrayList<ArrayList<Entity>>();

                for (int j = 0; j < numRooms; j++) {
                    serverSideEnts.add(new ArrayList<Entity>());
                    clientSideEnts.add(new ArrayList<Entity>());
                }

                i++;

            } else if (i == 1) {
                String[] spawn = line.split(":");
                spawnPoint = new Vector2f(Float.parseFloat(spawn[0]), Float.parseFloat(spawn[1]));
                i++;
            } else {
                //Type:x:y:room

                String[] entInfo = line.split(":");

                if (entInfo[0].matches("Platform")) {
                    if (entInfo.length == 4) {
                        clientSideEnts.get(Integer.parseInt(entInfo[3]) - 1).add(new Platform(new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2]))));
                    } else if (entInfo.length == 5) {
                        clientSideEnts.get(Integer.parseInt(entInfo[4]) - 1).add(new Platform(new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2])), Integer.parseInt(entInfo[3])));
                    }
                } else if (entInfo[0].matches("BlackHole")) {
                    clientSideEnts.get(Integer.parseInt(entInfo[3]) - 1).add(new BlackHole(new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2]))));
                } else if (entInfo[0].matches("RollingSawblade")) {
                    clientSideEnts.get(Integer.parseInt(entInfo[3]) - 1).add(new RollingSawblade(new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2]))));
                } else if (entInfo[0].matches("Spike")) {
                    if (entInfo.length == 4) {
                        clientSideEnts.get(Integer.parseInt(entInfo[3]) - 1).add(new Spike(new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2]))));
                    } else if (entInfo.length == 5) {
                        clientSideEnts.get(Integer.parseInt(entInfo[4]) - 1).add(new Spike(new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2])), Integer.parseInt(entInfo[3])));
                    }
                } else if(entInfo[0].matches("Spawner")){
                    if(entInfo.length > 4){
                        if(entInfo[3].matches("RollingSawblade")){
                            boolean forward = true;
                            if(entInfo[4].matches("0"))
                                forward = false;
                            Vector2f position = new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2]));
                            RollingSawblade blade = new RollingSawblade(position, forward);
                            clientSideEnts.get(Integer.parseInt(entInfo[5]) - 1).add(new Spawner(position, blade, 500));
                        } else if(entInfo[3].matches("Spike")){
                            int direction = Integer.parseInt(entInfo[4]);
                            Vector2f position = new Vector2f(Integer.parseInt(entInfo[1]), Integer.parseInt(entInfo[2]));
                            Spike spike = new Spike(position, direction);
                            clientSideEnts.get(Integer.parseInt(entInfo[5]) - 1).add(new Spawner(position, spike, 500));
                        }
                    }
                }
            }

            line = reader.readLine();
        }

        reader.close();
        currentRoom = 1;
    }

    public List<ArrayList<Entity>> getClientSideEnts() {
        return clientSideEnts;
    }

    public List<ArrayList<Entity>> getServerSideEnts() {
        return serverSideEnts;
    }

    public void setServerSideEnts(List<ArrayList<Entity>> serverSideEnts) {
        this.serverSideEnts = serverSideEnts;
    }

    public void setClientSideEnts(List<ArrayList<Entity>> clientSideEnts) {
        this.clientSideEnts = clientSideEnts;
    }

    public List<Entity> getEnts() {
        List<Entity> ents = new ArrayList<Entity>();
        ents.addAll(clientSideEnts.get(currentRoom - 1));
        return ents;
    }

    public void removeEnt(Entity e) {
        entsToRemove.add(e);
    }

    public void addEnt(Entity e){
        entsToAdd.add(e);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
