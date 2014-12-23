package com.webrob.recognition.logic;

import com.webrob.recognition.utils.GlobalDef;
import com.webrob.recognition.utils.RecognitionHelper;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Robert on 2014-12-23.
 */
public class RegionGrow
{
    private final Mat processingImage;

    public RegionGrow(Mat processingImage)
    {
	this.processingImage = processingImage;
    }

    public int regionGrowForEachSegment()
    {
	int segmentsFound = 0;
	Size size = processingImage.size();

	double[] nextSegmentColor = GlobalDef.SEGMENT_START_COLOR.clone();
	for (int x = 1; x < size.height; x += 4)
	{
	    for (int y = 1; y < size.width; y += 4)
	    {
		double[] pixel = processingImage.get(x, y);
		if (RecognitionHelper.isPixelColor(pixel, GlobalDef.WHITE_COLOR))
		{
		    Point point = new Point(x, y);
		    regionGrow(point, nextSegmentColor);
		    nextSegmentColor = RecognitionHelper.getNextSegmentColor(nextSegmentColor);
		    segmentsFound++;
		}
	    }
	}
	return segmentsFound;
    }

    private void regionGrow(Point point, double[] currentSegmentColor)
    {
	Size size = processingImage.size();
	Set<Point> allPoints = new HashSet<>();
	allPoints.add(point);

	while (!allPoints.isEmpty())
	{
	    Point p = allPoints.iterator().next();
	    int x = p.x;
	    int y = p.y;
	    allPoints.remove(p);
	    processingImage.put(x, y, currentSegmentColor);

	    if (y > 0)
	    {
		double[] topPoint = processingImage.get(x, y - 1);
		if (isPixelNotCheckedYetAndIsNotBlack(topPoint, currentSegmentColor))
		{
		    allPoints.add(new Point(x, y - 1));
		}
	    }
	    if (x > 0)
	    {
		double[] leftPoint = processingImage.get(x - 1, y);
		if (isPixelNotCheckedYetAndIsNotBlack(leftPoint, currentSegmentColor))
		{
		    allPoints.add(new Point(x - 1, y));
		}
	    }
	    if (y < size.width - 1)
	    {
		double[] bottomPoint = processingImage.get(x, y + 1);
		if (isPixelNotCheckedYetAndIsNotBlack(bottomPoint, currentSegmentColor))
		{
		    allPoints.add(new Point(x, y + 1));
		}
	    }
	    if (x < size.height - 1)
	    {
		double[] rightPoint = processingImage.get(x + 1, y);
		if (isPixelNotCheckedYetAndIsNotBlack(rightPoint, currentSegmentColor))
		{
		    allPoints.add(new Point(x + 1, y));
		}
	    }
	}
    }

    private boolean isPixelNotCheckedYetAndIsNotBlack(double[] pixel, double[] color)
    {
	return !RecognitionHelper.isPixelColor(pixel, color) &&
			!RecognitionHelper.isPixelColor(pixel, GlobalDef.BLACK_COLOR);
    }
}
