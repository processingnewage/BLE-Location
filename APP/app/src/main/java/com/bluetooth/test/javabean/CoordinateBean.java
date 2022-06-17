package com.bluetooth.test.javabean;

public class CoordinateBean {
    private int coordinateId;
    private float x;
    private float y;

    public CoordinateBean(int coordinateId, float x, float y) {
        this.coordinateId = coordinateId;
        this.x = x;
        this.y = y;
    }

    public int getCoordinateId() {
        return this.coordinateId;
    }

    public void setCoordinateId(int coordinateId) {
        this.coordinateId = coordinateId;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
