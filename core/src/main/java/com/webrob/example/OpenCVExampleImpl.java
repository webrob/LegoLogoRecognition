package com.webrob.example;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by Robert on 2014-12-21.
 */
public class OpenCVExampleImpl implements OpenCVExample
{
    static
    {
	System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    public String getExample()
    {
	Mat m = Mat.eye(3, 3, CvType.CV_8UC1);
	return m.dump();
    }
}
