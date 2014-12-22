package com.webrob.model;

import org.opencv.core.Point;
import org.opencv.core.Rect;

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
    private Direction direction = Direction.NotDetermined;


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
	    direction = Direction.Right;
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

	if ((NM[1] > 0.3 && NM[1] < 0.37) && (NM[6] > 0.03 && NM[6] < 0.37))
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
	Point letterBeforePoint = letterBefore.getGravityPoint();
	Point letterAfterPoint = letterAfter.getGravityPoint();
	boolean isLetterBetween;

	if (mainLetterPoint.y <= letterBeforePoint.y || mainLetterPoint.y >= letterAfterPoint.y)
	{
	    isLetterBetween = false;
	}
	else
	{
	    double distanceFromStraight = calculateDistanceFromStraight();
	    isLetterBetween = distanceFromStraight < 5;
	}
	return isLetterBetween;
    }

    public boolean areLettersNearEachOther()
    {
	Rect firstRect;
	Rect secondRect;

	 if (direction == Direction.Right)
	 {
	     firstRect = letterBefore.getBoundingRect();
	     secondRect = letterToCheckLocation.getBoundingRect();
	 }
	else
	 {
	     firstRect = letterToCheckLocation.getBoundingRect();
	     secondRect = letterBefore.getBoundingRect();
	 }

	double distance = (firstRect.width + secondRect.width /2.0f) /2.0f;

	double dist = secondRect.y - (firstRect.y + firstRect.width);
	return dist < distance;
    }

    private double calculateDistanceFromStraight()
    {
	Point mainLetterPoint = letterToCheckLocation.getGravityPoint();
	Point letterBeforePoint = letterBefore.getGravityPoint();
	Point letterAfterPoint = letterAfter.getGravityPoint();

	double a = (letterAfterPoint.x - letterBeforePoint.x) /
			(letterAfterPoint.y - letterBeforePoint.y);

	double b = letterBeforePoint.x - letterBeforePoint.y * a;

	return Math.abs(-a * mainLetterPoint.y + mainLetterPoint.x - b)/
			Math.sqrt(Math.pow(a,2) + 1);
    }

}
