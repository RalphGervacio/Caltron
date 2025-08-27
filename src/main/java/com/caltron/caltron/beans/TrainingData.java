package com.caltron.caltron.beans;

public class TrainingData {

    private double[][] X; 
    private double[] y;   

    public double[][] getX() {
        return X;
    }

    public void setX(double[][] X) {
        this.X = X;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }
}
