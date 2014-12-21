package com.webrob.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Created by Robert on 2014-12-20.
 */
public class LegoLogoRecognition implements EntryPoint
{

    private TestServiceAsync testServiceAsync = GWT.create(TestService.class);
    private Button testButton = new Button("new");
    private Label testLabel = new Label();

    @Override public void onModuleLoad()
    {

        RootPanel.get("slot1").add(testButton);
        RootPanel.get("slot2").add(testLabel);

        testButton.addClickHandler(new ClickHandler()
        {
            @Override public void onClick(ClickEvent event)
            {
                test();
            }
        });
    }

    private void test()
    {

        testServiceAsync.getMessage(new AsyncCallback<String>()
        {
            @Override public void onFailure(Throwable caught)
            {

            }

            @Override public void onSuccess(String result)
            {
                testLabel.setText(result);
            }
        });
    }
}
