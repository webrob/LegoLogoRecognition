package com.webrob;


import com.webrob.example.LegoRecognition;
import com.webrob.utils.ImageHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Robert
 */
public class MainWindowController implements Initializable
{
    @FXML private ImageView orgImageView;
    @FXML private ImageView newImageView;
    private ImageHelper imageHelper = new ImageHelper();

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        setOriginalImage();
        setProcessedImage();
    }

    private void setOriginalImage()
    {
        Image originalImage = imageHelper.getOriginalImage("logo.jpg");
        orgImageView.setImage(originalImage);
    }

    private void setProcessedImage()
    {
        Image newImage = imageHelper.getNewImage();
        newImageView.setImage(newImage);
    }
}
