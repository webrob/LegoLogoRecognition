package com.webrob.recognition.logic;

import com.google.common.collect.Multimap;
import com.webrob.recognition.domain.Lego;
import com.webrob.recognition.domain.Letter;
import com.webrob.recognition.domain.ProcessedStagesPaths;
import com.webrob.recognition.domain.Segment;
import com.webrob.recognition.utils.FileHelper;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

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


    private Mat originalImage;
    private Mat processingImage = new Mat();
    private String filePathToSave;

    public LegoRecognitionImpl(String filePath)
    {
	filePathToSave = FileHelper.getFilePathToSave(filePath);
	originalImage = Highgui.imread(filePath);
	originalImage.copyTo(processingImage);
    }

    @Override
    public ProcessedStagesPaths calculateProcessedStagePaths()
    {
	ProcessedStagesPaths stagesPaths = new ProcessedStagesPaths();

	Segmentation segmentation = new Segmentation(processingImage);
	segmentation.segmentToBlackAndWhite();
	String blackAndWhitePath = saveImage("1-blackAndWhite.jpg", processingImage);
	stagesPaths.setBlackAndWhiteSegmentationPath(blackAndWhitePath);

	RegionGrow regionGrow = new RegionGrow(processingImage);
	int regionFounds = regionGrow.regionGrowForEachSegment();

	MomentumAllSegmentsCalculator calculator = new MomentumAllSegmentsCalculator(processingImage);
	Multimap<Letter, Segment> recognizedLetters = calculator.calculateForEachSegmentMomentum(regionFounds);

	LegoFinder legoFinder = new LegoFinder();
	List<Lego> legoLogosOnAnyBackgroundColor = legoFinder.findLegoLogos(recognizedLetters);


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

	String markedLegoWithRedBackgroundSamplingPath = saveImage("2-markedLegoWithRedBackgroundSampling.jpg", processingImage);
	stagesPaths.setMarkedLegoWithRedBackgroundSamplingPath(markedLegoWithRedBackgroundSamplingPath);

	String originalImageWithMarkedLegoPath = saveImage("3-originalImageWithMarkedLego.jpg", originalImage);
	stagesPaths.setOriginalImageWithMarkedLegoPath(originalImageWithMarkedLegoPath);

	return stagesPaths;
    }


    private String saveImage(String nameWithExt, Mat image)
    {
	String pathToSavedImage = filePathToSave + nameWithExt;
	Highgui.imwrite(pathToSavedImage, image);
	return pathToSavedImage;
    }
}