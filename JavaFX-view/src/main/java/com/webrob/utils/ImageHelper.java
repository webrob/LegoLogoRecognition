package com.webrob.utils;

import com.webrob.logic.ProcessedStagesImagesListener;
import com.webrob.recognition.domain.ProcessedStagesPaths;
import com.webrob.recognition.logic.ImageProcessingListener;
import com.webrob.recognition.logic.LegoRecognition;
import com.webrob.recognition.logic.LegoRecognitionImpl;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2014-12-21.
 */
public class ImageHelper implements ImageProcessingListener
{
    public static final String URL_FILE_PREFIX = "file:///";
    private File file;
    private LegoRecognition legoRecognition;
    private List<ProcessedStagesImagesListener> listeners = new ArrayList<>();

    public ImageHelper(File file)
    {
	this.file = file;
	legoRecognition = new LegoRecognitionImpl(file.getAbsolutePath());
        legoRecognition.addListeners(this);
    }

    public static Image getImageFromPath(String path)
    {
	return new Image(URL_FILE_PREFIX + path);
    }

    public void addListener(ProcessedStagesImagesListener listener)
    {
        listeners.add(listener);
    }

    public Image getImageFromFile()
    {
	return getImageFromPath(file.getAbsolutePath());
    }

    public void recognizeLego()
    {
        Thread thread = new Thread(legoRecognition);
        thread.start();

    }

    @Override
    public void imageProcessingHasFinished(ProcessedStagesPaths stagesPaths)
    {
	ProcessedStagesImages stagesImages = new ProcessedStagesImages();
	String blackAndWhiteSegmentationPath = stagesPaths.getBlackAndWhiteSegmentationPath();
	stagesImages.setBlackAndWhiteSegmentationImage(getImageFromPath(blackAndWhiteSegmentationPath));

	String markedLegoWithRedBackgroundPath = stagesPaths.getMarkedLegoWithRedBackgroundSamplingPath();
	stagesImages.setMarkedLegoWithRedBackgroundSamplingImage(getImageFromPath(markedLegoWithRedBackgroundPath));

	String originalImageWithMarkedLegoPath = stagesPaths.getOriginalImageWithMarkedLegoPath();
	stagesImages.setOriginalImageWithMarkedLegoImage(getImageFromPath(originalImageWithMarkedLegoPath));

        notifyAllListeners(stagesImages);
    }

    private void notifyAllListeners(ProcessedStagesImages stagesImages)
    {
        for(ProcessedStagesImagesListener listener : listeners)
        {
            listener.processedStageImagesAreReady(stagesImages);
        }
    }
}
