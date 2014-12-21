package com.webrob.service;

import com.webrob.client.TestService;
import com.webrob.example.OpenCVExample;
import com.webrob.example.OpenCVExampleImpl;

/**
 * Created by Robert on 2014-12-20.
 */
public class TestServiceImpl implements TestService
{

    private OpenCVExample openCVExample = new OpenCVExampleImpl();

    @Override public String getMessage()
    {
	return "hello"  + openCVExample.getExample();
    }
}
