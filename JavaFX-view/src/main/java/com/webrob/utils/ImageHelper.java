package com.webrob.utils;


import com.webrob.recognition.logic.LegoRecognitionImpl;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import java.io.ByteArrayInputStream;

/**
 * Created by Robert on 2014-12-21.
 */
public class ImageHelper
{
    private LegoRecognitionImpl legoRecognition = new LegoRecognitionImpl();

    private Image matToImage(Mat input)
    {
	MatOfByte buf = new MatOfByte();
	Highgui.imencode(".jpg", input, buf);
	return new Image(new ByteArrayInputStream(buf.toArray()));
    }

    public Image getOriginalImage(String nameWithExt)
    {
	Mat originalMat = legoRecognition.getOriginalImage(nameWithExt);
	return matToImage(originalMat);
    }

    public Image getNewImage()
    {
	//Mat newMat = legoReconition.getNewImage();
	//Image newImage = ImageHelper.matToImage(newMat);

	String newImagePath = legoRecognition.getNewImagePath();
	return new Image("file:///" +newImagePath);
    }


}
