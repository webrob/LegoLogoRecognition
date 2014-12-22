package com.webrob.model;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

/**
 * Created by Robert on 2014-12-21.
 */
public class MomentumCalculator
{
    private final Rect boundingRect;
    private final double[] color;
    private final Point centreOfGravityPoint;
    private final double area;
    private final Mat image;

    public MomentumCalculator(Mat image, Rect boundingRect, double[] color, Point centreOfGravityPoint, double area)
    {
        this.image = image;
        this.boundingRect = boundingRect;
        this.color = color;
        this.centreOfGravityPoint = centreOfGravityPoint;
        this.area = area;
    }

    public double N(int p, int q)
    {
        return Mpq(boundingRect, color, centreOfGravityPoint, p, q) /
                        Math.pow(area, (p + q) / 2 + 1);

    }

    private double Mpq(Rect boundingRect, double[] color, Point centreOfGravityPoint, int p, int q)
    {
        double result = 0;

        for (int x = boundingRect.x; x < boundingRect.x + boundingRect.height; x++)
        {
            for (int y = boundingRect.y; y < boundingRect.y + boundingRect.width; y++)
            {
                double[] pixel = image.get(x, y);
                result += Math.pow(x - centreOfGravityPoint.x, p) * Math.pow(y - centreOfGravityPoint.y, q) *
                                (RecognitionHelper.isPixelColor(pixel, color) ? 1 : 0);
            }
        }

        return result;
    }
}
