package com.webrob.recognition.utils;

/**
 * Created by Robert on 2014-12-23.
 */
public class FileHelper
{
    public static String getFilePathToSave(String originalFilePath)
    {
	int lastIndexOf = originalFilePath.lastIndexOf("/");

	return originalFilePath.substring(0, lastIndexOf);
    }
}
