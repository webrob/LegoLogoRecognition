package com.webrob.recognition.utils;

import org.opencv.core.Point;
import org.opencv.core.Rect;

/**
 * Created by Robert on 2014-12-22.
 */
public class RecognitionHelper
{
    public static boolean isPixelColor(double[] pixel, double[] color)
    {
	boolean isPixelColor = true;
	for (int i = 0; i < color.length; i++)
	{
	    if (pixel[i] != color[i])
	    {
		isPixelColor = false;
		break;
	    }
	}
	return isPixelColor;
    }

    public static double[] getNextSegmentColor(double[] color)
    {
	//color[1]+=70;

	Double b = color[0];
	Double g = color[1];
	Double r = color[2];

	if (b.intValue() < 255)
	{
	    color[0]++;
	}
	else if (g.intValue() < 255)
	{
	    color[1]++;
	}
	else if (r.intValue() < 255)
	{
	    color[2]++;
	}
	return color;

    }

    public static double calculateRectDiagonal(Rect rect)
    {
	Point firstPoint = rect.br();
	Point lastPoint = rect.tl();

	return calculateEuclideanDistance(firstPoint, lastPoint);
    }

    public static double calculateEuclideanDistance(Point p1, Point p2)
    {
	return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
