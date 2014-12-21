package com.webrob;

import com.webrob.example.OpenCVExample;
import com.webrob.example.OpenCVExampleImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Robert
 */
public class MainWindowController implements Initializable
{
    @FXML private TextArea exampleTextArea;

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        OpenCVExample openCVExample = new OpenCVExampleImpl();
        exampleTextArea.setText(openCVExample.getExample());
    }
}
