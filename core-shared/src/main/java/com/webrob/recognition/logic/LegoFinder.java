package com.webrob.recognition.logic;

import com.google.common.collect.Multimap;
import com.webrob.recognition.domain.Lego;
import com.webrob.recognition.domain.Letter;
import com.webrob.recognition.domain.Segment;
import com.webrob.recognition.utils.GlobalDef;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Robert on 2014-12-23.
 */
public class LegoFinder
{

    public List<Lego> findLegoLogos(Multimap<Letter, Segment> recognizedLetters)
    {
	List<Lego> foundLego = new ArrayList<>();

	Collection<Segment> allSegmentsL = recognizedLetters.get(Letter.L);
	Collection<Segment> allSegmentsE = recognizedLetters.get(Letter.E);
	Collection<Segment> allSegmentsG = recognizedLetters.get(Letter.G);
	Collection<Segment> allSegmentsO = recognizedLetters.get(Letter.O);

	for (Segment segmentL : allSegmentsL)
	{
	    for (Segment segmentO : allSegmentsO)
	    {
		LetterManager letterManager = new LetterManager();

		for (Segment segmentE : allSegmentsE)
		{
		    letterManager.setLetterToCheckLocation(segmentE);
		    letterManager.setLetterBefore(segmentL);
		    letterManager.setLetterAfter(segmentO);

		    if (letterManager.isLetterBetween())
		    {
			boolean isLNearE = letterManager.areLettersNearEachOther();
			if (isLNearE)
			{
			    for (Segment segmentG : allSegmentsG)
			    {
				letterManager.setLetterToCheckLocation(segmentG);
				letterManager.setLetterBefore(segmentE);
				letterManager.setLetterAfter(segmentO);

				if (letterManager.isLetterBetween())
				{
				    boolean isENearG = letterManager.areLettersNearEachOther();
				    if (isENearG)
				    {

					letterManager.setLetterToCheckLocation(segmentO);
					letterManager.setLetterBefore(segmentG);

					boolean isGNearO = letterManager.areLettersNearEachOther();
					if (isGNearO)
					{
					    Lego lego = new Lego(segmentL, segmentE, segmentG, segmentO);
					    foundLego.add(lego);

					    Rect boundingRect = segmentL.getBoundingRect();
					    System.out.println("LEGO!!!!!!!!!!!!!!!!!!!!!!!!!!" + boundingRect +
							    segmentE.getBoundingRect() + " " + segmentG
							    .getBoundingRect() + " " + segmentO.getBoundingRect());



					}
				    }
				}
			    }
			}
		    }
		}
	    }
	}
	return foundLego;
    }
}
