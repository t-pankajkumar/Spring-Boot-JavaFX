package com.springfx;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class SpringFxApplication {

	public static void main(String[] args) {
		Application.launch(AppFx.class, args);
	}

}
