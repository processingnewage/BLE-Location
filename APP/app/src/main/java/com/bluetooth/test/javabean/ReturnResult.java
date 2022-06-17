package com.bluetooth.test.javabean;

public class ReturnResult {
    private double x;
    private double y;
    private int returnID;

    public ReturnResult(double x, double y, int returnID) {
        this.x = x;
        this.y = y;
        this.returnID = returnID;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getReturnID() {
        return this.returnID;
    }

    public void setReturnID(int returnID) {
        this.returnID = returnID;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
}
