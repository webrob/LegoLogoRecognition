package com.webrob.recognition.logic;

import com.webrob.recognition.domain.Direction;
import com.webrob.recognition.domain.Lego;
import com.webrob.recognition.domain.Letter;
import com.webrob.recognition.domain.Segment;
import com.webrob.recognition.utils.RecognitionHelper;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2014-12-22.
 */
public class LetterManager
{
    private Segment letterToCheckLocation;
    private Segment letterBefore;
    private Segment letterAfter;

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


	if ((NM[1] > 0.27 && NM[1] < 0.36) && (NM[3] > 0.23 && NM[3] < 3.6)
			&& (NM[6] > -0.035 && NM[6] < 0.025))
	{
	    letter = Letter.E;
	}
	else if ((NM[1] > 0.25 && NM[1] < 0.35) && (NM[3] > 0.06 && NM[3] < 0.83)
			&& (NM[4] > 0.03 && NM[4] < 0.35))
	{
	    letter = Letter.G;
	}

	else if ((NM[1] > 0.24 && NM[1] < 0.33) && (NM[3] > 0.0015 && NM[3] < 0.075)
			&& (NM[6] > 0.000003 && NM[6] < 0.06))
	{
	    letter = Letter.O;
	}
	else if ((NM[1] > 0.3 && NM[1] < 0.37) && (NM[6] > 0.002 && NM[6] < 1.75))
	{
	    letter = Letter.L;
	}

	return letter;
    }

    public boolean isLetterBetween()
    {
	boolean isLetterBetween;

	double distanceFromStraight = calculateDistanceFromStraight();
	isLetterBetween = distanceFromStraight < 5;

	return isLetterBetween;
    }

    public boolean areLettersNearEachOther()
    {
	Rect firstRect = letterBefore.getBoundingRect();

	double diagonalFirstRect = RecognitionHelper.calculateRectDiagonal(firstRect);
	double gravityCenterDistance = caculateGravityCenterDistance();

	return gravityCenterDistance < diagonalFirstRect;
    }

    private double caculateGravityCenterDistance()
    {
	Point gravityPoint1 = letterBefore.getGravityPoint();
	Point gravityPoint2 = letterToCheckLocation.getGravityPoint();

	return RecognitionHelper.calculateEuclideanDistance(gravityPoint1, gravityPoint2);
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
}
