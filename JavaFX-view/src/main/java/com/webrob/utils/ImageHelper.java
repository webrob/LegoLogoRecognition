package com.webrob.utils;

import com.webrob.recognition.domain.ProcessedStagesPaths;
import com.webrob.recognition.logic.LegoRecognition;
import com.webrob.recognition.logic.LegoRecognitionImpl;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by Robert on 2014-12-21.
 */
public class ImageHelper
{
    public static final String URL_FILE_PREFIX = "file:///";
    private File file;
    private LegoRecognition legoRecognition;

    public ImageHelper(File file)
    {
	this.file = file;
        legoRecognition = new LegoRecognitionImpl(file.getAbsolutePath());
    }

    public Image getImageFromFile()
    {
	return getImageFromPath(file.getAbsolutePath());
    }

    public ProcessedStagesImages recognizeLego()
    {
	ProcessedStagesImages stagesImages = new ProcessedStagesImages();
	ProcessedStagesPaths stagesPaths = legoRecognition.calculateProcessedStagePaths();

	String blackAndWhiteSegmentationPath = stagesPaths.getBlackAndWhiteSegmentationPath();
	stagesImages.setBlackAndWhiteSegmentationImage(getImageFromPath(blackAndWhiteSegmentationPath));

        String markedLegoWithRedBackgroundPath = stagesPaths.getMarkedLegoWithRedBackgroundSamplingPath();
        stagesImages.setMarkedLegoWithRedBackgroundSamplingImage(getImageFromPath(markedLegoWithRedBackgroundPath));

        String originalImageWithMarkedLegoPath = stagesPaths.getOriginalImageWithMarkedLegoPath();
        stagesImages.setOriginalImageWithMarkedLegoImage(getImageFromPath(originalImageWithMarkedLegoPath));

        return stagesImages;
    }

    public static Image getImageFromPath(String path)
    {
	return new Image(URL_FILE_PREFIX + path);
    }

}
