package com.webrob;

import com.webrob.logic.ProcessedStagesImagesListener;
import com.webrob.utils.DirectoryPath;
import com.webrob.utils.ImageHelper;
import com.webrob.utils.ProcessedStagesImages;
import javafx.application.Platform;
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
public class MainWindowController implements Initializable, ProcessedStagesImagesListener
{
    @FXML private ImageView orgImageView;
    @FXML private ImageView blackAndWhiteSegmentationImageView;
    @FXML private ImageView markedLegoWithRedBackgroundSamplingImageView;
    @FXML private ImageView originalImageWithMarkedLegoImageView;

    private ImageHelper imageHelper;
    private Stage stage;
    private String directoryPath;

    public void setStage(Stage stage)
    {
	this.stage = stage;
	stage.setOnCloseRequest(event -> {
	    Platform.exit();
	    System.exit(0);
	});
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
    }

    public void openFilePressed()
    {
	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Open Resource File");
	fileChooser.getExtensionFilters()
			.addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

	String initialPath = DirectoryPath.getPath();
	fileChooser.setInitialDirectory(new File(initialPath));
	File selectedFile = fileChooser.showOpenDialog(stage);
	clearAllViews();
	if (selectedFile != null)
	{
	    directoryPath = selectedFile.getParentFile().getAbsolutePath();
	    imageHelper = new ImageHelper(selectedFile);
	    imageHelper.addListener(this);
	    Image imageFromFile = imageHelper.getImageFromFile();
	    orgImageView.setImage(imageFromFile);
	}
    }

    private void clearAllViews()
    {
	orgImageView.setImage(null);
	blackAndWhiteSegmentationImageView.setImage(null);
	markedLegoWithRedBackgroundSamplingImageView.setImage(null);
	originalImageWithMarkedLegoImageView.setImage(null);
    }

    public void recognizeLegoPressed()
    {
	DirectoryPath.setPath(directoryPath);
	imageHelper.recognizeLego();
    }

    @Override public void processedStageImagesAreReady(final ProcessedStagesImages stagesImages)
    {
	Platform.runLater(() -> {
	    blackAndWhiteSegmentationImageView.setImage(stagesImages.getBlackAndWhiteSegmentationImage());
	    markedLegoWithRedBackgroundSamplingImageView
			    .setImage(stagesImages.getMarkedLegoWithRedBackgroundSamplingImage());
	    originalImageWithMarkedLegoImageView.setImage(stagesImages.getOriginalImageWithMarkedLegoImage());
	});
    }
}

