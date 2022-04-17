package edu.iastate.thedarkplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "map")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Map {
    @Id
    private String name;

    @ElementCollection
    @CollectionTable(name = "server")
    @OneToMany(cascade = CascadeType.ALL)
    private List<RoomEntities> serverSideEnts;

    @ElementCollection
    @CollectionTable(name = "client")
    @OneToMany(cascade = CascadeType.ALL)
    private List<RoomEntities> clientSideEnts;

    @Embedded
    private Vector2f spawnPoint;

    private int numRooms;
    private int currentRoom;

    @Embeddable
    private static class Vector2f {
        private float x, y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    @Entity
    @Table(name = "entities")
    private static class RoomEntities {
        @Id
        private Long roomId;

        @ElementCollection
        @OneToMany(cascade = CascadeType.ALL)
        @JoinColumn(name = "room_id")
        private List<RoomEntity> roomEnts;

        public List<RoomEntity> getRoomEnts() {
            return roomEnts;
        }

        public void setRoomEnts(List<RoomEntity> roomEnts) {
            this.roomEnts = roomEnts;
        }

        public Long getRoomId() {
            return roomId;
        }

        public void setRoomId(Long roomId) {
            this.roomId = roomId;
        }


        @Entity
        @Table(name = "entity")
        private static class RoomEntity {
            @Id
            private int id;
            private int type;
            private int spawnDelay;

            @Embedded
            private Vector2f position;

            @OneToOne(cascade = CascadeType.ALL)
            private RoomEntity entToSpawn;

            private Boolean respawning;

            private Integer direction;

            public Vector2f getPosition() {
                return position;
            }

            public void setPosition(Vector2f position) {
                this.position = position;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public Boolean isRespawning() {
                return respawning;
            }

            public void setRespawning(Boolean respawning) {
                this.respawning = respawning;
            }

            public Integer getDirection() {
                return direction;
            }

            public void setDirection(Integer direction) {
                this.direction = direction;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public RoomEntity getEntToSpawn() {
                return entToSpawn;
            }

            public void setEntToSpawn(RoomEntity entToSpawn) {
                this.entToSpawn = entToSpawn;
            }

            public int getSpawnDelay() {
                return spawnDelay;
            }

            public void setSpawnDelay(int spawnDelay) {
                this.spawnDelay = spawnDelay;
            }
        }

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector2f getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Vector2f spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }


    public int getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(int currentRoom) {
        this.currentRoom = currentRoom;
    }

    public List<RoomEntities> getServerSideEnts() {
        return serverSideEnts;
    }

    public void setServerSideEnts(List<RoomEntities> serverSideEnts) {
        this.serverSideEnts = serverSideEnts;
    }

    public List<RoomEntities> getClientSideEnts() {
        return clientSideEnts;
    }

    public void setClientSideEnts(List<RoomEntities> clientSideEnts) {
        this.clientSideEnts = clientSideEnts;
    }

}
