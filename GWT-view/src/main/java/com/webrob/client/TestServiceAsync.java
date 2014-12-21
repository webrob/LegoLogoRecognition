package com.webrob.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Robert on 2014-12-20.
 */
public interface TestServiceAsync
{
    void getMessage(AsyncCallback<String> callback);
}
