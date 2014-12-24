package com.webrob.recognition.logic;

import com.google.common.collect.Multimap;
import com.webrob.recognition.domain.Lego;
import com.webrob.recognition.domain.Letter;
import com.webrob.recognition.domain.ProcessedStagesPaths;
import com.webrob.recognition.domain.Segment;
import com.webrob.recognition.utils.FileHelper;
import com.webrob.recognition.utils.GlobalDef;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2014-12-21.
 */
public class LegoRecognitionImpl implements LegoRecognition
{
    static
    {
	System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    private final List<ImageProcessingListener> listeners = new ArrayList<>();
    private Mat originalImage;
    private Mat processingImage = new Mat();
    private String filePathToSave;
    private ProcessedStagesPaths stagesPaths;

    public LegoRecognitionImpl(String filePath)
    {
	filePathToSave = FileHelper.getFilePathToSave(filePath);
	originalImage = Highgui.imread(filePath);
	originalImage.copyTo(processingImage);
    }



    @Override
    public void calculateProcessedStagePaths()
    {
	stagesPaths = new ProcessedStagesPaths();

	doSegmentation();
	int regionFounds = doRegionGrow();
	Multimap<Letter, Segment> recognizedLetters = doCalculationMomentums(regionFounds);
	List<Lego> legoLogosOnAnyBackgroundColor = doLegoLogosFind(recognizedLetters);
	doBackgroundColorVeryficationAndMarkLogos(legoLogosOnAnyBackgroundColor);

	String markedLegoWithRedBackgroundSamplingPath = saveImage(GlobalDef.MARKED_LEGO_WITH_RED_BACKGROUND_SAMPLE_STAGE_IMAGE_NAME,
			processingImage);
	stagesPaths.setMarkedLegoWithRedBackgroundSamplingPath(markedLegoWithRedBackgroundSamplingPath);

	String originalImageWithMarkedLegoPath = saveImage(GlobalDef.ORIGINAL_IMAGE_WITH_MARKED_LEGO__STAGE_IMAGE_NAME, originalImage);
	stagesPaths.setOriginalImageWithMarkedLegoPath(originalImageWithMarkedLegoPath);

	notifyListeners(stagesPaths);
    }

    private void doSegmentation()
    {
	Segmentation segmentation = new Segmentation(processingImage);
	segmentation.segmentToBlackAndWhite();
	String blackAndWhitePath = saveImage(GlobalDef.BLACK_AND_WHITE_STAGE_IMAGE_NAME, processingImage);
	stagesPaths.setBlackAndWhiteSegmentationPath(blackAndWhitePath);
    }

    private int doRegionGrow()
    {
	RegionGrow regionGrow = new RegionGrow(processingImage);
	return regionGrow.regionGrowForEachSegment();
    }

    private Multimap<Letter, Segment> doCalculationMomentums(int regionFounds)
    {
	MomentumAllSegmentsCalculator calculator = new MomentumAllSegmentsCalculator(processingImage);
	return calculator.calculateForEachSegmentMomentum(regionFounds);
    }

    private List<Lego> doLegoLogosFind(Multimap<Letter, Segment> recognizedLetters)
    {
	LegoFinder legoFinder = new LegoFinder();
	return legoFinder.findLegoLogos(recognizedLetters);
    }

    private void doBackgroundColorVeryficationAndMarkLogos(List<Lego> legoLogosOnAnyBackgroundColor )
    {
	RedBackgroundVerifier backgroundVerifier = new RedBackgroundVerifier(originalImage, processingImage);
	for (Lego lego : legoLogosOnAnyBackgroundColor)
	{
	    boolean pointsOnRedBackground = backgroundVerifier.drawPoints(lego.getPointsAbove(), lego.getPointsUnder());
	    if (pointsOnRedBackground)
	    {
		Rect boundingRect = lego.getBoundingRect();
		backgroundVerifier.drawFoundLogoFrameOnOriginalImage(boundingRect);
		backgroundVerifier.drawFoundLogoFrameOnProcessingImage(boundingRect);
	    }
	}
    }

    @Override
    public void addListeners(ImageProcessingListener listener)
    {
	listeners.add(listener);
    }

    private void notifyListeners(ProcessedStagesPaths stagesPaths)
    {
	for (ImageProcessingListener listener : listeners)
	{
	    listener.imageProcessingHasFinished(stagesPaths);
	}
    }

    private String saveImage(String nameWithExt, Mat image)
    {
	String pathToSavedImage = filePathToSave + nameWithExt;
	Highgui.imwrite(pathToSavedImage, image);
	return pathToSavedImage;
    }

    @Override
    public void run()
    {
	calculateProcessedStagePaths();
    }


}
