package com.agelm.legomine;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class BallFinder {
    private float view_ratio = 0.0f;
    private int min_area = 0;

    private int sat_lower = 96;
    private int sat_upper = 255;
    private int red_lower = 160;
    private int red_upper = 180;
    private int blue_lower = 105;
    private int blue_upper = 120;
    private int yellow_lower = 16;
    private int yellow_upper = 25;

    private boolean debug = false;
    private String orientation = "portrait";

    private Mat frame;

    public BallFinder(Mat frame) {
        this.frame = frame.clone();
    }

    public BallFinder(Mat frame, boolean debug) {
        this(frame);

        if (debug) {
            this.frame = frame;
            this.debug = true;
        }
    }

    public void setOrientation(String orientation) {
        if (orientation == "landscape" || orientation == "portrait")
            this.orientation = orientation;
        else
            throw new IllegalArgumentException("Invalid orientation, only \"portrait\" or \"landscape\" are allowed");
    }

    public void setViewRatio(float view_ratio) {
        this.view_ratio = view_ratio;
    }

    public void setMinArea(int min_area) {
        this.min_area = min_area;
    }

    public void setSaturationThreshold(int lower, int upper) {
        this.sat_lower = lower;
        this.sat_upper = upper;
    }

    public void setRedThreshold(int lower, int upper) {
        this.red_lower = lower;
        this.red_upper = upper;
    }

    public void setBlueThreshold(int lower, int upper) {
        this.blue_lower = lower;
        this.blue_upper = upper;
    }

    public void setYellowThreshold(int lower, int upper) {
        this.yellow_lower = lower;
        this.yellow_upper= upper;
    }

    public ArrayList<Ball> findBalls() {
        ArrayList<Ball> balls = new ArrayList<>();

        Mat hsv = new Mat();
        List <Mat> split_hsv = new ArrayList<>();

        Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV);
        Core.split(hsv, split_hsv);

        Mat mask_sat = new Mat();
        Imgproc.threshold(split_hsv.get(1), mask_sat, sat_lower, sat_upper, Imgproc.THRESH_BINARY);

        Mat kernel = new Mat(new Size(3, 3), CvType.CV_8UC1, new Scalar(255));
        Imgproc.morphologyEx(mask_sat, mask_sat, Imgproc.MORPH_OPEN, kernel);

        Mat hue = split_hsv.get(0);
        Mat mask_red = new Mat();
        Mat mask_blue = new Mat();
        Mat mask_yellow = new Mat();

        Core.inRange(hsv, new Scalar(red_lower, 0, 0), new Scalar(red_upper, 255, 255), mask_red);
        Core.inRange(hsv, new Scalar(blue_lower, 0, 0), new Scalar(blue_upper, 255, 255), mask_blue);
        Core.inRange(hsv, new Scalar(yellow_lower, 0, 0), new Scalar(yellow_upper, 255, 255), mask_yellow);

        Mat mask_hue = new Mat();
        Mat mask = new Mat();

        Core.bitwise_or(mask_red, mask_blue, mask_hue);
        Core.bitwise_or(mask_hue, mask_yellow, mask_hue);
        Core.bitwise_and(mask_sat, mask_hue, mask);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (debug) {
            Point p1 = new Point(frame.width() * view_ratio, 0);
            Point p2 = new Point(frame.width() * view_ratio, frame.height());

            if (orientation == "landscape") {
                p1 = new Point(0, frame.height() * view_ratio);
                p2 = new Point(frame.width(), frame.height() * view_ratio);
            }

            Imgproc.line(frame, p1, p2, new Scalar(0, 255, 255), 2);

            for (int i = 0; i < contours.size(); i++)
                Imgproc.drawContours(frame, contours, i, new Scalar(255, 0, 0), 1);
        }

        float[] radius = new float[1];
        Point center = new Point();

        for (MatOfPoint c : contours) {
            Imgproc.minEnclosingCircle(new MatOfPoint2f(c.toArray()), center, radius);

            boolean cond = center.x > frame.width() * view_ratio;

            if (orientation == "landscape")
                cond = center.y > frame.height() * view_ratio;

            if (cond && Imgproc.contourArea(c) > min_area) {
                // TODO: add color mean for area_hue
                int area_hue = (int) hue.get((int) center.y, (int) center.x)[0];
                String color;

                if (area_hue >= red_lower && area_hue <= red_upper)
                    color = "red";
                else if (area_hue >= blue_lower && area_hue <= blue_upper)
                    color = "blue";
                else if (area_hue >= yellow_lower && area_hue <= yellow_upper)
                    color = "yellow";
                else
                    color = "unknown";

                balls.add(new Ball(center, radius[0], color));

                if (debug) {
                    Scalar color_rgb;

                    if (color == "red")
                        color_rgb = new Scalar(255, 0, 0);
                    else if (color == "blue")
                        color_rgb = new Scalar(0, 0, 255);
                    else if (color == "yellow")
                        color_rgb = new Scalar(255, 255, 0);
                    else
                        color_rgb = new Scalar(0, 0, 0);

                    Imgproc.circle(frame, center, (int) radius[0], color_rgb, 2);
                }
            }
        }

        return balls;
    }
}