package com.webrob.model;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Robert on 2014-12-21.
 */
public class LegoRecognition
{
    static
    {
	System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    public static final int MIN_LOGO_AREA = 200;

    protected final String PATH_TO_ORIGINAL_IMAGE_FOLDER = "images/org/";
    private String originalPath;
    private Mat newImage = new Mat();
    private String originalImageNameWithExt;
    private Size size;
    private final double[] WHITE_COLOR = new double[] { 255, 255, 255 };
    private final double[] BLACK_COLOR = new double[] { 0, 0, 0 };
    private final double[] SEGMENT_START_COLOR = new double[] { 150, 0, 0 };

    public Mat getOriginalImage(String nameWithExtension)
    {
	originalPath = getClass().getResource("/" + PATH_TO_ORIGINAL_IMAGE_FOLDER + nameWithExtension).getPath();
	originalPath = originalPath.substring(1, originalPath.length());
	Mat originalImage = Highgui.imread(originalPath);
	originalImage.copyTo(newImage);
	originalImageNameWithExt = nameWithExtension;
	size = originalImage.size();
	return originalImage;
    }

    public Mat getNewImage()
    {
	saveImage();
	return newImage;
    }

    public String getNewImagePath()
    {
	return saveImage();
    }

    private String saveImage()
    {
	String pathToWrite = originalPath.replace("org/" + originalImageNameWithExt, "tmp/test.jpg");
	//convertImageToGray();
	segmentToBlackAndWhite();
	calculateForEachSegmentMomentum();
	recognizeLetters();
	checkCorrectLettersOrder();
	checkRedBackground();
	markFoundLogos();
	Highgui.imwrite(pathToWrite, newImage);
	return pathToWrite;
    }

    private void convertImageToGray()
    {
	for (int x = 0; x < size.width; x++)
	{
	    for (int y = 0; y < size.height; y++)
	    {
		double[] pixel = newImage.get(x, y);
		pixel = convertPixelToGray(pixel);
		newImage.put(x, y, pixel);
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

    private double[] convertPixelToGray(double[] pixel)
    {
	double gray = calculateGrayValue(pixel);
	for (int i = 0; i < pixel.length; i++)
	{
	    pixel[i] = gray;
	}
	return pixel;
    }

    private void changePixelColor(double[] pixel, double[] color)
    {
	System.arraycopy(color, 0, pixel, 0, pixel.length);
    }

    private void segmentToBlackAndWhite()
    {
	for (int x = 0; x < size.height; x++)
	{
	    for (int y = 0; y < size.width; y++)
	    {
		double[] pixel = newImage.get(x, y);
		double gray = calculateGrayValue(pixel);
		if (gray > 210)
		{
		    changePixelColor(pixel, WHITE_COLOR);
		}
		else
		{
		    changePixelColor(pixel, BLACK_COLOR);
		}
		newImage.put(x, y, pixel);
	    }
	}
    }

    private int regionGrowForEachSegment()
    {
	int segmentsFound = 0;

	for (int x = 0; x < size.height; x++)
	{
	    newImage.put(x, 0, BLACK_COLOR);
	}
	for (int y = 0; y < size.width; y++)
	{
	    newImage.put(0, y, BLACK_COLOR);
	}

	double[] nextSegmentColor = SEGMENT_START_COLOR.clone();
	for (int x = 1; x < size.height; x += 4)
	{
	    for (int y = 1; y < size.width; y += 4)
	    {
		double[] pixel = newImage.get(x, y);
		if (RecognitionHelper.isPixelColor(pixel, WHITE_COLOR))
		{
		    Point point = new Point(x, y);
		    regionGrow(point, nextSegmentColor);
		    nextSegmentColor = getNextSegmentColor(nextSegmentColor);
		    segmentsFound++;
		    //System.out.println(segmentsFound);
		    //System.out.println(nextSegmentColor[0] + " "+ nextSegmentColor[1] +  " "+ nextSegmentColor[2]);
		}
	    }
	}
	return segmentsFound;
    }

    private void regionGrow(Point point, double[] currentSegmentColor)
    {
	Set<Point> allPoints = new HashSet<>();
	allPoints.add(point);

	while (!allPoints.isEmpty())
	{
	    Point p = allPoints.iterator().next();
	    int x = p.x;
	    int y = p.y;
	    allPoints.remove(p);
	    newImage.put(x, y, currentSegmentColor);

	    if (y > 0)
	    {
		double[] topPoint = newImage.get(x, y - 1);
		if (isPixelNotCheckedYetAndIsNotBlack(topPoint, currentSegmentColor))
		{
		    allPoints.add(new Point(x, y - 1));
		}
	    }
	    if (x > 0)
	    {
		double[] leftPoint = newImage.get(x - 1, y);
		if (isPixelNotCheckedYetAndIsNotBlack(leftPoint, currentSegmentColor))
		{
		    allPoints.add(new Point(x - 1, y));
		}
	    }
	    if (y < size.width - 1)
	    {
		double[] bottomPoint = newImage.get(x, y + 1);
		if (isPixelNotCheckedYetAndIsNotBlack(bottomPoint, currentSegmentColor))
		{
		    allPoints.add(new Point(x, y + 1));
		}
	    }
	    if (x < size.height - 1)
	    {
		double[] rightPoint = newImage.get(x + 1, y);
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
			!RecognitionHelper.isPixelColor(pixel, BLACK_COLOR);
    }

    private double[] getNextSegmentColor(double[] color)
    {
	Double b = color[0];
	Double g = color[1];
	Double r = color[2];
	if (b.intValue() < 254)
	{
	    color[0]++;
	}
	else if (g.intValue() < 254)
	{
	    color[1]++;
	}
	else if (r.intValue() < 254)
	{
	    color[2]++;
	}

	return color;
    }

    private void calculateForEachSegmentMomentum()
    {
	int regionFounds = regionGrowForEachSegment();

	double[] currentSegmentColor = SEGMENT_START_COLOR.clone();

	for (int i = 0; i < regionFounds; i++)
	{
	    Rect rect = calculateBoundingRectForColor(currentSegmentColor);
	    double area = mpq(rect, currentSegmentColor, 0, 0);

	    if (area > MIN_LOGO_AREA)
	    {
		double ax = mpq(rect, currentSegmentColor, 1, 0) / area;
		double ay = mpq(rect, currentSegmentColor, 0, 1) / area;
		org.opencv.core.Point gravityPoint = new org.opencv.core.Point(ax, ay);
		MomentumCalculator cal = new MomentumCalculator(newImage, rect, currentSegmentColor, gravityPoint,
				area);

		double[] NM = new double[11];

		NM[1] = cal.N(2, 0) + cal.N(0, 2);

		NM[2] = Math.pow(cal.N(2, 0) - cal.N(0, 2), 2) + 4 * Math.pow(cal.N(1, 1), 2);

		NM[3] = Math.pow(cal.N(3, 0) - 3 * cal.N(1, 2), 2) + Math.pow(3 * cal.N(2, 1) - cal.N(0, 3), 2);

		NM[4] = Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) + Math.pow(cal.N(2, 1) + cal.N(0, 3), 2);

		NM[5] = (cal.N(3, 0) - 3 * cal.N(1, 2)) * (cal.N(3, 0) + cal.N(1, 2)) *
				(Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) - 3 * Math.pow(cal.N(2, 1) + cal.N(0, 3), 2))
				+ (3 * cal.N(2, 1) - cal.N(0, 3)) * (cal.N(2, 1) + cal.N(0, 3)) *
				(3 * Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) - Math.pow(cal.N(2, 1) + cal.N(0, 3), 2));

		NM[6] = (cal.N(2, 0) - cal.N(0, 2)) * (Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) - Math.pow(cal.N(2, 1) +
				cal.N(0, 3), 2)) + 4 * cal.N(1, 1) * (cal.N(3, 0) + cal.N(1, 2)) * (cal.N(2, 1) +
				cal.N(0, 3));

		NM[7] = cal.N(2, 0) * cal.N(0, 2) + Math.pow(cal.N(1, 1), 2);

		NM[8] = cal.N(3, 0) * cal.N(1, 2) + cal.N(2, 1) * cal.N(0, 3) - Math.pow(cal.N(1, 2), 2) - Math
				.pow(cal.N(2, 1), 2);

		NM[9] = cal.N(2, 0) * (cal.N(2, 1) * cal.N(0, 3) - Math.pow(cal.N(1, 2), 2)) + cal.N(0, 2) *
				(cal.N(3, 0) * cal.N(1, 2) - Math.pow(cal.N(2, 1), 2)) - cal.N(1, 1) *
				(cal.N(3, 0) * cal.N(0, 3) - cal.N(2, 1) * cal.N(1, 2));

		NM[10] = Math.pow(cal.N(3, 0) * cal.N(0, 3) - cal.N(1, 2) * cal.N(2, 1), 2) -
				4 * (cal.N(3, 0) * cal.N(1, 2) - Math.pow(cal.N(2, 1), 2)) *
						(cal.N(0, 3) * cal.N(2, 1) - cal.N(1, 2));

		for (int z = 1; z < 11; z++)
		{
		    System.out.println(NM[z]);
		}

	    }

	    currentSegmentColor = getNextSegmentColor(currentSegmentColor);
	}

    }

    private double N(Rect boundingRect, double[] color, org.opencv.core.Point centreOfGravityPoint, double area, int p, int q)
    {
	return Mpq(boundingRect, color, centreOfGravityPoint, p, q) /
			Math.pow(area, (p + q) / 2 + 1);

    }

    private double Mpq(Rect boundingRect, double[] color, org.opencv.core.Point centreOfGravityPoint, int p, int q)
    {
	double result = 0;

	for (int x = boundingRect.x; x < boundingRect.x + boundingRect.height; x++)
	{
	    for (int y = boundingRect.y; y < boundingRect.y + boundingRect.width; y++)
	    {
		double[] pixel = newImage.get(x, y);
		result += Math.pow(x - centreOfGravityPoint.x, p) * Math.pow(y - centreOfGravityPoint.y, q) *
				(RecognitionHelper.isPixelColor(pixel, color) ? 1 : 0);
	    }
	}

	return result;
    }

    private double mpq(Rect boundingRect, double[] color, int p, int q)
    {
	double result = 0;

	for (int x = boundingRect.x; x < boundingRect.x + boundingRect.height; x++)
	{
	    for (int y = boundingRect.y; y < boundingRect.y + boundingRect.width; y++)
	    {
		double[] pixel = newImage.get(x, y);
		result += Math.pow(x, p) * Math.pow(y, q) *
				(RecognitionHelper.isPixelColor(pixel, color) ? 1 : 0);
	    }
	}

	return result;
    }

    private void calculateMomentForColor(double[] color)
    {

    }

    private Rect calculateBoundingRectForColor(double[] color)
    {
	int x2 = 0;
	int y2 = 0;
	int x1 = (int) size.height - 1;
	int y1 = (int) size.width - 1;

	for (int x = 0; x < size.height; x++)
	{
	    for (int y = 0; y < size.width; y++)
	    {
		double[] pixel = newImage.get(x, y);
		if (RecognitionHelper.isPixelColor(pixel, color))
		{
		    if (x < x1)
		    {
			x1 = x;
		    }
		    else if (x > x2)
		    {
			x2 = x;
		    }

		    if (y < y1)
		    {
			y1 = y;
		    }
		    else if (y > y2)
		    {
			y2 = y;
		    }
		}

	    }
	}

	//System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);

	return new Rect(x1, y1, y2 - y1, x2 - x1);
    }

    private void recognizeLetters()
    {

    }

    private void checkCorrectLettersOrder()
    {

    }

    private void markFoundLogos()
    {

    }

    private void checkRedBackground()
    {

    }

}
