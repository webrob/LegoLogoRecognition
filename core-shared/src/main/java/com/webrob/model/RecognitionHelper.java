package com.webrob.model;

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
}
