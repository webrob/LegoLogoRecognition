package com.webrob.model;

import org.opencv.core.Rect;

import java.awt.*;

/**
 * Created by Robert on 2014-12-22.
 */
public class Lego
{
    public Segment getSegmentL()
    {
	return segmentL;
    }

    public Segment getSegmentE()
    {
	return segmentE;
    }

    public Segment getSegmentG()
    {
	return segmentG;
    }

    public Segment getSegmentO()
    {
	return segmentO;
    }

    private final Segment segmentL;
    private final Segment segmentE;
    private final Segment segmentG;
    private final Segment segmentO;

    public Lego(Segment segmentL, Segment segmentE, Segment segmentG, Segment segmentO)
    {
	this.segmentL = segmentL;
	this.segmentE = segmentE;
	this.segmentG = segmentG;
	this.segmentO = segmentO;
    }


    public Rect getBoundingRect()
    {
	Rect rectL = segmentL.getBoundingRect();
	Rect rectE = segmentE.getBoundingRect();
	Rect rectG = segmentG.getBoundingRect();
	Rect rectO = segmentO.getBoundingRect();

	int x1 = rectL.x;
	int x2 = rectL.x + rectL.height;
	int y1 = rectL.y;
	int y2 = rectL.y + rectL.width;

	x1 = Math.min(x1, rectE.x);
	x2 = Math.max(x2, rectE.x + rectE.height);
	y1 = Math.min(y1, rectE.y);
	y2 = Math.max(y2, rectE.y + rectE.width);

	x1 = Math.min(x1, rectG.x);
	x2 = Math.max(x2, rectG.x + rectG.height);
	y1 = Math.min(y1, rectG.y);
	y2 = Math.max(y2, rectG.y + rectG.width);

	x1 = Math.min(x1, rectO.x);
	x2 = Math.max(x2, rectO.x + rectO.height);
	y1 = Math.min(y1, rectO.y);
	y2 = Math.max(y2, rectO.y + rectO.width);

	return new Rect(x1,y1,y2-y1, x2-x1 );
    }
}
