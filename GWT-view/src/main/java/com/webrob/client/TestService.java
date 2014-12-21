package com.webrob.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Created by Robert on 2014-12-20.
 */

@RemoteServiceRelativePath("rpc/test")
public interface TestService extends RemoteService
{
    String getMessage();
}
