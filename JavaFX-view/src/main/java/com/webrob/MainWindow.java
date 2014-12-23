package com.webrob;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Robert on 2014-12-21.
 */
public class MainWindow extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
	Parent root = loader.load();
	MainWindowController controller = loader.getController();
	controller.setStage(stage);

	Scene scene = new Scene(root);
	stage.setScene(scene);
	stage.show();
    }

    public static void main(String[] args)
    {
	launch(args);
    }

}