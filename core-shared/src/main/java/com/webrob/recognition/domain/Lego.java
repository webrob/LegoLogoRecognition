package com.webrob.recognition.domain;

import com.webrob.recognition.utils.RecognitionHelper;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2014-12-22.
 */
public class Lego
{
    private final Segment segmentL;
    private final Segment segmentE;
    private final Segment segmentG;
    private final Segment segmentO;
    private final Rect boundingRect;
    private final double factorB;
    private final double variableB;
    private double variableA;



    private List<Point> pointsAbove;
    private List<Point> pointsUnder;

    public Lego(Segment segmentL, Segment segmentE, Segment segmentG, Segment segmentO)
    {
	this.segmentL = segmentL;
	this.segmentE = segmentE;
	this.segmentG = segmentG;
	this.segmentO = segmentO;

	boundingRect = calculateBoundingRect();
	variableB = calculateVariablesAandB();
	factorB = calculateFactorB(variableA);
	calculatePointsAbove();
	calculatePointsUnder();
    }


    public Rect getBoundingRect()
    {
	return boundingRect;
    }

    private Rect calculateBoundingRect()
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

    public Segment getSegmentL()
    {
	return segmentL;
    }

    public Segment getSegmentO()
    {
	return segmentO;
    }

    public List<Point> getPointsAbove()
    {
	return pointsAbove;
    }

    public List<Point> getPointsUnder()
    {
	return pointsUnder;
    }

    private double calculateVariablesAandB()
    {
	Point letterBeforePoint = segmentL.getGravityPoint();
	Point letterAfterPoint = segmentO.getGravityPoint();

	variableA = (letterAfterPoint.x - letterBeforePoint.x) /
			(letterAfterPoint.y - letterBeforePoint.y);

	return letterBeforePoint.x - letterBeforePoint.y * variableA;
    }

    private double calculateFactorB(double variableA)
    {
	double diagonal = RecognitionHelper.calculateRectDiagonal(boundingRect);

	double alpha = Math.atan(variableA);
	return diagonal / 3.5 / Math.cos(alpha);
    }

    private List<Point> calculatePointsAbove()
    {
	pointsAbove = new ArrayList<>();
	double factorBAbove = variableB + factorB;
	for (double y = boundingRect.y - boundingRect.width;
	     y < boundingRect.y + boundingRect.width; y += boundingRect.width / 25)
	{
	    double x = variableA * y + factorBAbove;

	    Point p = new Point(x, y);
	    pointsAbove.add(p);
	}
	return pointsAbove;
    }

    private List<Point> calculatePointsUnder()
    {
	pointsUnder = new ArrayList<>();

	double factorBAbove = variableB - factorB;
	for (double y = boundingRect.y; y < boundingRect.y + 2 * boundingRect.width; y += boundingRect.width / 25)
	{
	    double x = variableA * y + factorBAbove;
	    Point p = new Point(x, y);
	    pointsUnder.add(p);
	}

	return pointsUnder;
    }
}
