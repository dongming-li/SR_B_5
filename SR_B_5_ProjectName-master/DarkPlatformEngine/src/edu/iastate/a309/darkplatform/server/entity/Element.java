package edu.iastate.a309.darkplatform.server.entity;

public class Element {
    private Long id;

    private Long element;

    private Long xCoord;
    private Long yCoord;

    public Long getElement() {
        return element;
    }

    public void setElement(Long element) {
        this.element = element;
    }

    public Long getxCoord() {
        return xCoord;
    }

    public void setxCoord(Long xCoord) {
        this.xCoord = xCoord;
    }

    public Long getyCoord() {
        return yCoord;
    }

    public void setyCoord(Long yCoord) {
        this.yCoord = yCoord;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
