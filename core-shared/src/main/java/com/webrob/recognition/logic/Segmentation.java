package com.webrob.recognition.logic;

import com.webrob.recognition.utils.GlobalDef;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 * Created by Robert on 2014-12-23.
 */
public class Segmentation
{
    private final Mat processingImage;

    public static final int GRAY_SEGMENTATION_THRESHOLD = 210;

    public Segmentation(Mat processingImage)
    {
	this.processingImage = processingImage;
    }

    public void segmentToBlackAndWhite()
    {
	Size size = processingImage.size();
	for (int x = 0; x < size.height; x++)
	{
	    for (int y = 0; y < size.width; y++)
	    {
		double[] pixel = processingImage.get(x, y);
		double gray = calculateGrayValue(pixel);
		if (gray > GRAY_SEGMENTATION_THRESHOLD)
		{
		    changePixelColor(pixel, GlobalDef.WHITE_COLOR);
		}
		else
		{
		    changePixelColor(pixel, GlobalDef.BLACK_COLOR);
		}
		processingImage.put(x, y, pixel);
	    }
	}
    }

    private double calculateGrayValue(double[] pixel)
    {
	double gray = 0;
	for (double componentRGB : pixel)
	{
	    gray += componentRGB;
	}
	gray /= pixel.length;
	return gray;
    }


    private void changePixelColor(double[] pixel, double[] color)
    {
	System.arraycopy(color, 0, pixel, 0, pixel.length);
    }

}
