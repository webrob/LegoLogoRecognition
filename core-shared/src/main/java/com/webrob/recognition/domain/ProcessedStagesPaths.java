package com.webrob.recognition.domain;

/**
 * Created by Robert on 2014-12-23.
 */
public class ProcessedStagesPaths
{
    private String blackAndWhiteSegmentationPath;
    private String markedLegoWithRedBackgroundSamplingPath;
    private String originalImageWithMarkedLegoPath;

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
	return originalImageWithMarkedLegoPath;
    }

    public void setOriginalImageWithMarkedLegoPath(String originalImageWithMarkedLegoPath)
    {
	this.originalImageWithMarkedLegoPath = originalImageWithMarkedLegoPath;
    }
}
