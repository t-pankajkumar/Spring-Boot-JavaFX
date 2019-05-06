package com.springfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springfx.beans.Device;
import com.springfx.repositories.DeviceRepository;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class AppController extends Application {
	@FXML
	private Label devicecount;

	@FXML
	private Label employeescount;

	@Autowired
	private DeviceRepository deviceRepository;

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

	}

	@FXML
	public void addDevice(Event event) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/AddDevice.fxml"));
		AnchorPane vbox;
		try {
			vbox = loader.<AnchorPane>load();
			Scene secondScene = new Scene(vbox);
			// New window (Stage)
			Stage newWindow = new Stage();
			newWindow.setTitle("Add Device");
			newWindow.setScene(secondScene);
			secondScene.getWindow().centerOnScreen();
			newWindow.setResizable(false);
			// Specifies the modality for new window.
			newWindow.initModality(Modality.WINDOW_MODAL);

			// Specifies the owner Window (parent) for new window
			newWindow.initOwner(primaryStage);
			newWindow.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void showMenuItems(Event event) {

	}

	@FXML
	public void exit(Event event) {
		System.exit(0);
	}

	@FXML
	public void showDevice(Event event) {
		ArrayList<TableColumn> tc = setDeviceColumns();
		TableView tableView = new TableView();

		TableColumn actionCol = new TableColumn("Action");
		actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Device, String>, TableCell<Device, String>> cellFactory = //
				new Callback<TableColumn<Device, String>, TableCell<Device, String>>() {
					@Override
					public TableCell call(final TableColumn<Device, String> param) {
						final TableCell<Device, String> cell = new TableCell<Device, String>() {
							final Button btn = new Button("Sync Users");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										Device device = getTableView().getItems().get(getIndex());
										syncUsers(device, event);
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};

		actionCol.setCellFactory(cellFactory);
		tc.add(actionCol);
		tableView.getColumns().addAll(tc);
		deviceRepository.findAll().forEach(device -> {
			tableView.getItems().add(device);
		});
		Stage newWindow = new Stage();
		newWindow.setTitle("Devices");
		VBox vbox = new VBox(tableView);
		Scene scene = new Scene(vbox);
		newWindow.setScene(scene);
		newWindow.initOwner(primaryStage);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.show();
	}

	public ArrayList<TableColumn> setDeviceColumns() {
		ArrayList<TableColumn> tc = new ArrayList<TableColumn>();
		TableColumn<String, Device> column2 = new TableColumn<>("Id");
		column2.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<String, Device> column3 = new TableColumn<>("Device No");
		column3.setCellValueFactory(new PropertyValueFactory<>("deviceno"));

		TableColumn<String, Device> column4 = new TableColumn<>("IP");
		column4.setCellValueFactory(new PropertyValueFactory<>("ip"));

		TableColumn<String, Device> column5 = new TableColumn<>("Type");
		column5.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn<String, Device> column6 = new TableColumn<>("Serial No");
		column6.setCellValueFactory(new PropertyValueFactory<>("serialno"));
		tc.addAll(Arrays.asList(column2, column3, column4, column5, column6));
		return tc;
	}

	public void syncUsers(Device device, Event event) {

	}

	@FXML
	public void showUsers(Event event) {
	}

	@FXML
	public void resetData(Event event) {

	}

	@FXML
	public void syncAttendance(Event event) {

	}

	@FXML
	public void getAttendanceLogs(Event event) {

	}

	@FXML
	public void showAttendanceLogs(Event event) {

	}

}
