package com.webrob.utils;

import javafx.scene.image.Image;

/**
 * Created by Robert on 2014-12-23.
 */
public class ProcessedStagesImages
{
    private Image blackAndWhiteSegmentationImage;
    private Image markedLegoWithRedBackgroundSamplingImage;
    private Image originalImageWithMarkedLegoImage;

    public Image getBlackAndWhiteSegmentationImage()
    {
	return blackAndWhiteSegmentationImage;
    }

    public void setBlackAndWhiteSegmentationImage(Image blackAndWhiteSegmentationImage)
    {
	this.blackAndWhiteSegmentationImage = blackAndWhiteSegmentationImage;
    }

    public Image getMarkedLegoWithRedBackgroundSamplingImage()
    {
	return markedLegoWithRedBackgroundSamplingImage;
    }

    public void setMarkedLegoWithRedBackgroundSamplingImage(Image markedLegoWithRedBackgroundSamplingImage)
    {
	this.markedLegoWithRedBackgroundSamplingImage = markedLegoWithRedBackgroundSamplingImage;
    }

    public Image getOriginalImageWithMarkedLegoImage()
    {
	return originalImageWithMarkedLegoImage;
    }

    public void setOriginalImageWithMarkedLegoImage(Image originalImageWithMarkedLego)
    {
	this.originalImageWithMarkedLegoImage = originalImageWithMarkedLego;
    }

}
