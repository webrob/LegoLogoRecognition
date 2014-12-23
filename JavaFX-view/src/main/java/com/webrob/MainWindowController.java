package com.webrob;

import com.webrob.utils.ImageHelper;
import com.webrob.utils.ProcessedStagesImages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Robert
 */
public class MainWindowController implements Initializable
{
    @FXML private ImageView orgImageView;
    @FXML private ImageView blackAndWhiteSegmentationImageView;
    @FXML private ImageView markedLegoWithRedBackgroundSamplingImageView;
    @FXML private ImageView originalImageWithMarkedLegoImageView;

    private ImageHelper imageHelper;
    private Stage stage;

    public void setStage(Stage stage)
    {
	this.stage = stage;
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
    }


    public void openFilePressed()
    {
        FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Open Resource File");
	fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

	File selectedFile = fileChooser.showOpenDialog(stage);
	if (selectedFile != null)
	{
            imageHelper = new ImageHelper(selectedFile);
	    Image imageFromFile = imageHelper.getImageFromFile();
	    orgImageView.setImage(imageFromFile);
	}
    }

    public void recognizeLegoPressed()
    {
        ProcessedStagesImages stagesImages = imageHelper.recognizeLego();

        blackAndWhiteSegmentationImageView.setImage(stagesImages.getBlackAndWhiteSegmentationImage());
        markedLegoWithRedBackgroundSamplingImageView.setImage(stagesImages.getMarkedLegoWithRedBackgroundSamplingImage());
        originalImageWithMarkedLegoImageView.setImage(stagesImages.getOriginalImageWithMarkedLegoImage());
    }
}

