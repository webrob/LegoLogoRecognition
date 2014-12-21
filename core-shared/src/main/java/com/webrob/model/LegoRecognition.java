package com.webrob.model;

import org.opencv.core.Mat;
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
        Highgui.imwrite(pathToWrite, newImage);
        return pathToWrite;
    }


}
