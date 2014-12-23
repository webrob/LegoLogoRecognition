package com.webrob.recognition.logic;

import com.webrob.recognition.domain.Direction;
import com.webrob.recognition.domain.Lego;
import com.webrob.recognition.domain.Letter;
import com.webrob.recognition.domain.Segment;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2014-12-22.
 */
public class LetterManager
{
    private final Segment firstSegment;
    private final Segment lastSegment;
    private Segment letterToCheckLocation;
    private Segment letterBefore;
    private Segment letterAfter;
    private Direction direction;

    public LetterManager(Segment firstSegment, Segment lastSegment)
    {
	this.firstSegment = firstSegment;
	this.lastSegment = lastSegment;

	Point firstSegmentPoint = firstSegment.getGravityPoint();
	Point lastSegmentPoint = lastSegment.getGravityPoint();

	if (lastSegmentPoint.y > firstSegmentPoint.y)
	{
	    direction = Direction.Right;
	}
	else
	{
	    direction = Direction.Left;
	}
    }

    public void setLetterToCheckLocation(Segment letterToCheckLocation)
    {
	this.letterToCheckLocation = letterToCheckLocation;
    }

    public void setLetterBefore(Segment letterBefore)
    {
	this.letterBefore = letterBefore;
    }

    public void setLetterAfter(Segment letterAfter)
    {
	this.letterAfter = letterAfter;
    }

    public static Letter recognizeLetter(double[] NM)
    {
	Letter letter = Letter.NOT_FOUND;

	if ((NM[1] > 0.3 && NM[1] < 0.37) && (NM[6] > 0.0255 && NM[6] < 0.37))
	{
	    letter = Letter.L;
	}
	else if ((NM[1] > 0.27 && NM[1] < 0.36) && (NM[3] > 0.23 && NM[3] < 3.6)
			&& (NM[6] > -0.035 && NM[6] < 0.025))
	{
	    letter = Letter.E;
	}
	else if ((NM[1] > 0.25 && NM[1] < 0.35) && (NM[3] > 0.06 && NM[3] < 0.82)
			&& (NM[4] > 0.035 && NM[4] < 0.35))
	{
	    letter = Letter.G;
	}

	else if ((NM[1] > 0.24 && NM[1] < 0.33) && (NM[3] > 0.0015 && NM[3] < 0.07)
			&& (NM[6] > 0.000003 && NM[6] < 0.06))
	{
	    letter = Letter.O;
	}

	return letter;
    }

    public boolean isLetterBetween()
    {
	Point mainLetterPoint = letterToCheckLocation.getGravityPoint();
	Point letterBeforePoint;
	Point letterAfterPoint;

	if (direction == Direction.Right)
	{
	    letterBeforePoint = letterBefore.getGravityPoint();
	    letterAfterPoint = letterAfter.getGravityPoint();
	}
	else
	{
	    letterBeforePoint = letterAfter.getGravityPoint();
	    letterAfterPoint = letterBefore.getGravityPoint();
	}
	boolean isLetterBetween;

	//if (mainLetterPoint.y <= letterBeforePoint.y || mainLetterPoint.y >= letterAfterPoint.y)
	{
	    isLetterBetween = false;
	}
	//else
	{
	    double distanceFromStraight = calculateDistanceFromStraight();
	    isLetterBetween = distanceFromStraight < 5;
	}
	return isLetterBetween;
    }

    public boolean areLettersNearEachOther()
    {
	Rect firstRect = letterBefore.getBoundingRect();

	double diagonalFirstRect = calculateRectDiagonal(firstRect);
	double gravityCenterDistance = caculateGravityCenterDistance();

	return gravityCenterDistance < diagonalFirstRect;
    }

    private double caculateGravityCenterDistance()
    {
	Point gravityPoint1 = letterBefore.getGravityPoint();
	Point gravityPoint2 = letterToCheckLocation.getGravityPoint();

	return calculateEuclideanDistance(gravityPoint1, gravityPoint2);
    }

    private double calculateRectDiagonal(Rect rect)
    {
	Point firstPoint = rect.br();
	Point lastPoint = rect.tl();

	return calculateEuclideanDistance(firstPoint, lastPoint);
    }

    private double calculateEuclideanDistance(Point p1, Point p2)
    {
	return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    private double calculateDistanceFromStraight()
    {
	Point mainLetterPoint = letterToCheckLocation.getGravityPoint();
	Point letterBeforePoint = letterBefore.getGravityPoint();
	Point letterAfterPoint = letterAfter.getGravityPoint();

	double a = (letterAfterPoint.x - letterBeforePoint.x) /
			(letterAfterPoint.y - letterBeforePoint.y);

	double b = letterBeforePoint.x - letterBeforePoint.y * a;

	return Math.abs(-a * mainLetterPoint.y + mainLetterPoint.x - b) /
			Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public List<Point> calculatePointsAbove(Lego lego)
    {
	List<Point> points = new ArrayList<>();

	Segment segmentL = lego.getSegmentL();
	Segment segmentO = lego.getSegmentO();
	org.opencv.core.Point letterBeforePoint = segmentL.getGravityPoint();
	org.opencv.core.Point letterAfterPoint = segmentO.getGravityPoint();

	double a = (letterAfterPoint.x - letterBeforePoint.x) /
			(letterAfterPoint.y - letterBeforePoint.y);

	double b = letterBeforePoint.x - letterBeforePoint.y * a;

	Rect boundingRect = lego.getBoundingRect();
	double diagonal = calculateRectDiagonal(boundingRect);

	double alfa = Math.atan(a);


	double factorBAbove = b + diagonal / 3.5 / Math.cos(alfa);

	for (double y = boundingRect.y - boundingRect.width; y < boundingRect.y + boundingRect.width; y += boundingRect.width / 25)
	{
	    double x = a * y + factorBAbove;

	    Point p = new Point(x, y);
	    points.add(p);
	}

	return points;
    }

    public List<Point> calculatePointsUnder(Lego lego)
    {
	List<Point> points = new ArrayList<>();

	Segment segmentL = lego.getSegmentL();
	Segment segmentO = lego.getSegmentO();
	org.opencv.core.Point letterBeforePoint = segmentL.getGravityPoint();
	org.opencv.core.Point letterAfterPoint = segmentO.getGravityPoint();

	double a = (letterAfterPoint.x - letterBeforePoint.x) /
			(letterAfterPoint.y - letterBeforePoint.y);

	double b = letterBeforePoint.x - letterBeforePoint.y * a;

	Rect boundingRect = lego.getBoundingRect();
	double diagonal = calculateRectDiagonal(boundingRect);

	double alfa = Math.atan(a);
	System.out.println(alfa * 180/ Math.PI + " a = " +a);

	double factorBAbove = b - diagonal / 3.5 / Math.cos(alfa);

	for (double y = boundingRect.y ; y < boundingRect.y + 2 *boundingRect.width; y += boundingRect.width / 25)
	{
	    double x = a * y + factorBAbove;

	    Point p = new Point(x, y);
	    points.add(p);
	}

	return points;
    }

}
