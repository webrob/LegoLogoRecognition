package com.webrob.recognition.domain;

/**
 * Created by Robert on 2014-12-23.
 */
public class ProcessedStagesPaths
{
    private String blackAndWhiteSegmentationPath;
    private String markedLegoWithRedBackgroundSamplingPath;
    private String originalImageWithMarkedPath;

    public String getBlackAndWhiteSegmentationPath()
    {
	return blackAndWhiteSegmentationPath;
    }

    public void setBlackAndWhiteSegmentationPath(String blackAndWhiteSegmentationPath)
    {
	this.blackAndWhiteSegmentationPath = blackAndWhiteSegmentationPath;
    }

    public String getMarkedLegoWithRedBackgroundSamplingPath()
    {
	return markedLegoWithRedBackgroundSamplingPath;
    }

    public void setMarkedLegoWithRedBackgroundSamplingPath(String markedLegoWithRedBackgroundSamplingPath)
    {
	this.markedLegoWithRedBackgroundSamplingPath = markedLegoWithRedBackgroundSamplingPath;
    }

    public String getOriginalImageWithMarkedLegoPath()
    {
	return originalImageWithMarkedPath;
    }

    public void setOriginalImageWithMarkedPath(String originalImageWithMarkedPath)
    {
	this.originalImageWithMarkedPath = originalImageWithMarkedPath;
    }
}
