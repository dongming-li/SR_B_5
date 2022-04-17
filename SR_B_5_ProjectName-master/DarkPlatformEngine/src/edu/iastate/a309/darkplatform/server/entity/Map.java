package edu.iastate.a309.darkplatform.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@JsonSerialize
public class Map {
    private Long id;

    private List<Element> players;

    private List<Element> elements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID: ").append(id).append("\n");
        for (Element e : elements) {
            str.append("Element: ").append(e.getElement())
                    .append(" ").append("X: ").append(e.getxCoord())
                    .append(" ").append("Y: ").append(e.getyCoord()).append("\n\n");
        }
        return str.toString();
    }

    public List<Element> getPlayers() {
        return players;
    }

    public void setPlayers(List<Element> players) {
        this.players = players;
    }
}
