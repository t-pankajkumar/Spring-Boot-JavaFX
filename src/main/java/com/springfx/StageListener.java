package com.springfx;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
	private final String applicationTitle;
	private final Resource fxml;
	private final ApplicationContext ac;
	public StageListener(@Value("${spring.application.ui.title}") String applicationTitle,
			@Value("classpath:/ui.fxml") Resource resource,
			ApplicationContext ac) {
		this.applicationTitle = applicationTitle;
		this.fxml = resource;
		this.ac = ac;
	}
	@Override
	public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
		try {
			Stage stage = stageReadyEvent.getStage();
			URL url = this.fxml.getURL();
			FXMLLoader  loader = new FXMLLoader(url);
			loader.setControllerFactory(ac::getBean);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setTitle(this.applicationTitle);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
