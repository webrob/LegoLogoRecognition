package com.webrob.model;

/**
 * Created by Robert on 2014-12-22.
 */
public class LetterManager
{
    public static Letter recognizeLetter(double[] NM)
    {
	Letter letter = Letter.NOT_FOUND;

	if ((NM[1] > 0.3 && NM[1] < 0.36) && (NM[6] > 0.03 && NM[6] < 0.26))
	{
	    letter = Letter.L;
	}
	else if ((NM[1] > 0.27 && NM[1] < 0.33) && (NM[3] > 0.3 && NM[3] < 3.1)
			&& (NM[6] > -0.008 && NM[6] < 0.004))
	{
	    letter = Letter.E;
	}
	else if ((NM[1] > 0.25 && NM[1] < 0.32) && (NM[3] > 0.06 && NM[3] < 0.44)
			&& (NM[4] > 0.04 && NM[4] < 0.2))
	{
	    letter = Letter.G;
	}

	else if ((NM[1] > 0.24 && NM[1] < 0.31) && (NM[3] > 0.004 && NM[3] < 0.04)
			&& (NM[6] > 0.0001 && NM[6] < 0.06))
	{
	    letter = Letter.O;
	}

	return letter;
    }



}
