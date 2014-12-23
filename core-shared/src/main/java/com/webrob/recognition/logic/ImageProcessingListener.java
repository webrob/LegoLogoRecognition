package com.webrob.recognition.logic;

import com.webrob.recognition.domain.ProcessedStagesPaths;

/**
 * Created by Robert on 2014-12-23.
 */
public interface ImageProcessingListener
{
    void imageProcessingHasFinished(ProcessedStagesPaths stagesPaths);
}
