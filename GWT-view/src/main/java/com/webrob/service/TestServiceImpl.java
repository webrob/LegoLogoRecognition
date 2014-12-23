package com.webrob.service;

import com.webrob.client.TestService;


/**
 * Created by Robert on 2014-12-20.
 */
public class TestServiceImpl implements TestService
{

    @Override public String getMessage()
    {
	return "hello" ;
    }
}
