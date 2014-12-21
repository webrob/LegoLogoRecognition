package com.webrob.model;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

/**
 * Created by Robert on 2014-12-21.
 */
public class LegoRecognition
{
    static
    {
	System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    protected final String PATH_TO_ORIGINAL_IMAGE_FOLDER = "images/org/";
    private String originalPath;
    private Mat newImage = new Mat();
    private String originalImageNameWithExt;
    private Size size;
    private final double[] WHITE_COLOR = new double[] { 255, 255, 255 };
    private final double[] BLACK_COLOR = new double[] { 0, 0, 0 };

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
	regionGrowForEachSegment();
	calculateForEachSegmentMomentum();
	recognizeLetters();
	checkCorrectLettersOrder();
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
		if (gray > 200)
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


    private void regionGrowForEachSegment()
    {

    }

    private void calculateForEachSegmentMomentum()
    {

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

}
