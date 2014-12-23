package com.webrob.recognition.logic;

/**
 * Created by Robert on 2014-12-23.
 */
public interface LegoRecognition extends Runnable
{
    void calculateProcessedStagePaths();
    void addListeners(ImageProcessingListener listener);
}
