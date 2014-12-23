package com.webrob.recognition.logic;

import com.webrob.recognition.domain.Lego;
import com.webrob.recognition.utils.GlobalDef;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.List;

/**
 * Created by Robert on 2014-12-23.
 */
public class RedBackgroundVerifier
{
    private final Mat originalImage;
    private final Mat processingImage;

    public RedBackgroundVerifier(Mat originalImage, Mat processingImage)
    {

	this.originalImage = originalImage;
	this.processingImage = processingImage;
    }

    public void drawFoundLogoFrame(Rect rect)
    {
	System.out.println(rect);
	for (int x = rect.x; x < rect.x + rect.height; x++)
	{
	    processingImage.put(x, rect.y, GlobalDef.GREEN_COLOR);
	    processingImage.put(x, rect.y - 1, GlobalDef.GREEN_COLOR);
	    processingImage.put(x, rect.y + rect.width, GlobalDef.GREEN_COLOR);
	    processingImage.put(x, rect.y + rect.width + 1, GlobalDef.GREEN_COLOR);
	}

	for (int y = rect.y; y < rect.y + rect.width; y++)
	{
	    processingImage.put(rect.x, y, GlobalDef.GREEN_COLOR);
	    processingImage.put(rect.x - 1, y, GlobalDef.GREEN_COLOR);
	    processingImage.put(rect.x + rect.height, y, GlobalDef.GREEN_COLOR);
	    processingImage.put(rect.x + rect.height + 1, y, GlobalDef.GREEN_COLOR);
	}
    }


    public boolean drawPoints(List<org.opencv.core.Point> pointsAbove, List<org.opencv.core.Point> pointsUnder)
    {
	System.out.println(pointsAbove.size() + " " + pointsUnder.size());

	int numberRedPixelsAbove = 0;
	int numberRedPixelsUnder = 0;

	for (Point point : pointsAbove)
	{
	    int x = (int) point.x;
	    int y = (int) point.y;
	    if (x > 0 && y > 0)
	    {
		System.out.println(point);
		processingImage.put(x, y, GlobalDef.RED_COLOR);
		processingImage.put(x + 1, y, GlobalDef.RED_COLOR);
		processingImage.put(x, y + 1, GlobalDef.RED_COLOR);
		processingImage.put(x + 1, y + 1, GlobalDef.RED_COLOR);

		double[] pixel = originalImage.get(x, y);
		if (pixel != null)
		{
		    if (isColorSimilarToRed(pixel))
		    {
			numberRedPixelsAbove++;
		    }
		}
	    }
	}

	for (Point point : pointsUnder)
	{
	    int x = (int) point.x;
	    int y = (int) point.y;
	    System.out.println(point);
	    if (x > 0 && y > 0)
	    {
		processingImage.put(x, y, GlobalDef.RED_COLOR);
		processingImage.put(x + 1, y, GlobalDef.RED_COLOR);
		processingImage.put(x, y + 1, GlobalDef.RED_COLOR);
		processingImage.put(x + 1, y + 1, GlobalDef.RED_COLOR);

		double[] pixel = originalImage.get(x, y);
		if (pixel != null)
		{
		    if (isColorSimilarToRed(pixel))
		    {
			numberRedPixelsUnder++;
		    }
		}
	    }
	}

	System.out.println(numberRedPixelsAbove + " " + numberRedPixelsUnder);

	return numberRedPixelsAbove >= 2 && numberRedPixelsUnder >= 2;

    }


    private boolean isColorSimilarToRed(double[] color)
    {
	return (color[0] < 80 && color[1] < 80 && color[2] > 180);
    }
}
