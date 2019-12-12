package com.agelm.legomine;

import org.opencv.core.Point;

public class Ball {
    public Point center;
    public float radius;
    public String color;

    public Ball(Point center, float radius, String color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }
}
