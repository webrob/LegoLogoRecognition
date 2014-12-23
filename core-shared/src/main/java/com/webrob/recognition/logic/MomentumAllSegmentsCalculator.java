package com.webrob.recognition.logic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.webrob.recognition.domain.Letter;
import com.webrob.recognition.domain.Segment;
import com.webrob.recognition.utils.GlobalDef;
import com.webrob.recognition.utils.RecognitionHelper;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

/**
 * Created by Robert on 2014-12-23.
 */
public class MomentumAllSegmentsCalculator
{
    private final Mat processingImage;
    private Multimap<Letter, Segment> recognizedLetters = HashMultimap.create();

    public MomentumAllSegmentsCalculator(Mat processingImage)
    {
	this.processingImage = processingImage;
    }

    public Multimap<Letter, Segment> calculateForEachSegmentMomentum(int regionFounds)
    {
	double[] currentSegmentColor = GlobalDef.SEGMENT_START_COLOR.clone();

	for (int i = 0; i < regionFounds; i++)
	{
	    Rect rect = calculateBoundingRectForColor(currentSegmentColor);
	    double area = mpq(rect, currentSegmentColor, 0, 0);

	    if (area > GlobalDef.MIN_SEGMENT_AREA)
	    {
		double ax = mpq(rect, currentSegmentColor, 1, 0) / area;
		double ay = mpq(rect, currentSegmentColor, 0, 1) / area;
		Point gravityPoint = new org.opencv.core.Point(ax, ay);
		MomentumCalculator cal = new MomentumCalculator(processingImage, rect, currentSegmentColor,
				gravityPoint,
				area);

		double[] NM = calculateMomentumsForColor(cal);
		for (int z = 1; z < 11; z++)
		{
		    System.out.println("M " + z + " = " + NM[z]);
		}
		recognizeLetters(NM, rect, gravityPoint);
	    }

	    currentSegmentColor = RecognitionHelper.getNextSegmentColor(currentSegmentColor);
	}

	return recognizedLetters;

    }

    private void recognizeLetters(double[] NM, Rect rect, org.opencv.core.Point gravityPoint)
    {
	Letter letter = LetterManager.recognizeLetter(NM);
	Segment segment = new Segment(rect, gravityPoint);
	System.out.println(letter + " " + rect);
	recognizedLetters.put(letter, segment);
    }

    private double mpq(Rect boundingRect, double[] color, int p, int q)
    {
	double result = 0;

	for (int x = boundingRect.x; x < boundingRect.x + boundingRect.height; x++)
	{
	    for (int y = boundingRect.y; y < boundingRect.y + boundingRect.width; y++)
	    {
		double[] pixel = processingImage.get(x, y);
		result += Math.pow(x, p) * Math.pow(y, q) *
				(RecognitionHelper.isPixelColor(pixel, color) ? 1 : 0);
	    }
	}

	return result;
    }

    private Rect calculateBoundingRectForColor(double[] color)
    {
	Size size = processingImage.size();
	int x2 = 0;
	int y2 = 0;
	int x1 = (int) size.height - 1;
	int y1 = (int) size.width - 1;

	for (int x = 0; x < size.height; x++)
	{
	    for (int y = 0; y < size.width; y++)
	    {
		double[] pixel = processingImage.get(x, y);
		if (RecognitionHelper.isPixelColor(pixel, color))
		{
		    if (x < x1)
		    {
			x1 = x;
		    }
		    else if (x > x2)
		    {
			x2 = x;
		    }

		    if (y < y1)
		    {
			y1 = y;
		    }
		    else if (y > y2)
		    {
			y2 = y;
		    }
		}

	    }
	}

	return new Rect(x1, y1, y2 - y1, x2 - x1);
    }

    private double[] calculateMomentumsForColor(MomentumCalculator cal)
    {
	double[] NM = new double[11];

	NM[1] = cal.N(2, 0) + cal.N(0, 2);

	NM[2] = Math.pow(cal.N(2, 0) - cal.N(0, 2), 2) + 4 * Math.pow(cal.N(1, 1), 2);

	NM[3] = Math.pow(cal.N(3, 0) - 3 * cal.N(1, 2), 2) + Math.pow(3 * cal.N(2, 1) - cal.N(0, 3), 2);

	NM[4] = Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) + Math.pow(cal.N(2, 1) + cal.N(0, 3), 2);

	NM[5] = (cal.N(3, 0) - 3 * cal.N(1, 2)) * (cal.N(3, 0) + cal.N(1, 2)) *
			(Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) - 3 * Math.pow(cal.N(2, 1) + cal.N(0, 3), 2))
			+ (3 * cal.N(2, 1) - cal.N(0, 3)) * (cal.N(2, 1) + cal.N(0, 3)) *
			(3 * Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) - Math.pow(cal.N(2, 1) + cal.N(0, 3), 2));

	NM[6] = (cal.N(2, 0) - cal.N(0, 2)) * (Math.pow(cal.N(3, 0) + cal.N(1, 2), 2) - Math.pow(cal.N(2, 1) +
			cal.N(0, 3), 2)) + 4 * cal.N(1, 1) * (cal.N(3, 0) + cal.N(1, 2)) * (cal.N(2, 1) +
			cal.N(0, 3));

	NM[7] = cal.N(2, 0) * cal.N(0, 2) + Math.pow(cal.N(1, 1), 2);

	NM[8] = cal.N(3, 0) * cal.N(1, 2) + cal.N(2, 1) * cal.N(0, 3) - Math.pow(cal.N(1, 2), 2) - Math
			.pow(cal.N(2, 1), 2);

	NM[9] = cal.N(2, 0) * (cal.N(2, 1) * cal.N(0, 3) - Math.pow(cal.N(1, 2), 2)) + cal.N(0, 2) *
			(cal.N(3, 0) * cal.N(1, 2) - Math.pow(cal.N(2, 1), 2)) - cal.N(1, 1) *
			(cal.N(3, 0) * cal.N(0, 3) - cal.N(2, 1) * cal.N(1, 2));

	NM[10] = Math.pow(cal.N(3, 0) * cal.N(0, 3) - cal.N(1, 2) * cal.N(2, 1), 2) -
			4 * (cal.N(3, 0) * cal.N(1, 2) - Math.pow(cal.N(2, 1), 2)) *
					(cal.N(0, 3) * cal.N(2, 1) - cal.N(1, 2));
	return NM;
    }
}
