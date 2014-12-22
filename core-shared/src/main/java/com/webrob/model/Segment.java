package com.webrob.model;

import org.opencv.core.Point;
import org.opencv.core.Rect;

/**
 * Created by Robert on 2014-12-22.
 */
public class Segment
{
    private final Point gravityPoint;
    private final Rect boundingRect;

    public Segment(Rect boundingRect, Point gravityPoint)
    {
	this.boundingRect = boundingRect;
	this.gravityPoint = gravityPoint;
    }
}
