package com.webrob.utils;

import java.util.prefs.Preferences;

/**
 * Created by Robert on 2014-12-27.
 */
public class DirectoryPath
{
    private static final Preferences prefs = Preferences.userNodeForPackage(DirectoryPath.class);
    private static final String NAME = "lego_logo_recognition_path";
    private static final String DEFAULT_PATH = System.getProperty("user.dir");
    private static String projectPath = prefs.get(NAME, DEFAULT_PATH);

    public static String getPath()
    {
	return projectPath;
    }

    public static void setPath(String path)
    {
	projectPath = path;
	savePath();
    }

    private static void savePath()
    {
	prefs.put(NAME, projectPath);
    }
}
