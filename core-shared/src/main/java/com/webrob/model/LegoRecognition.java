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

    public Mat getOriginalImage(String nameWithExtension)
    {
	originalPath = getClass().getResource("/" + PATH_TO_ORIGINAL_IMAGE_FOLDER + nameWithExtension).getPath();
	originalPath = originalPath.substring(1, originalPath.length());
	Mat originalImage = Highgui.imread(originalPath);
	originalImage.copyTo(newImage);
	originalImageNameWithExt = nameWithExtension;
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
        convertToGray();
        Highgui.imwrite(pathToWrite, newImage);
        return pathToWrite;
    }

    private void convertToGray()
    {
        Size size = newImage.size();

        for (int x=0; x <size.width; x++)
        {
            for (int y=0; y <size.height; y++)
            {
                double[] doubles = newImage.get(x, y);
                double gray =0;
                for (int i =0; i< doubles.length; i++)
                {
                   gray += doubles[i];
                }
                gray /= doubles.length;

                for (int i =0; i< doubles.length; i++)
                {
                    doubles[i] = gray;
                }

                newImage.put(x,y, doubles);
            }
        }

    }


}
