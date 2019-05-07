package com.springfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.springfx.beans.Device;
import com.springfx.beans.User;
import com.springfx.repositories.DeviceRepository;
import com.springfx.repositories.UserRepository;
import com.springfx.services.BiometricService;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	@Autowired
	private BiometricService bio;

	@Autowired
	private UserRepository userRepository;

	ApplicationContext ac;

	@Autowired
	public AppController(ApplicationContext ac) {
		this.ac = ac;
	}

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

	}

	@FXML
	public void addDevice(Event event) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/AddDevice.fxml"));
		loader.setControllerFactory(ac::getBean);
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
		Alert a = new Alert(AlertType.NONE);
		a.setAlertType(AlertType.CONFIRMATION);
		if (bio.connect(device.getIp(), Integer.parseInt(device.getDeviceno()))) {
			String serialno = bio.getSerialNumber();
			if (serialno != null) {
				List<Map<String, Object>> users = bio.getUsers();
				long status[] = { 0, 0, 0 };
				if (users != null) {
					users.forEach(user -> {
						Map<String, Object> map = user;
						List<User> match = userRepository
								.findByEnrollNumber(Long.parseLong(String.valueOf(map.get("enrollNumber"))));
						if (match.size() <= 0) {
							User newuser = new User();
							newuser.setEnrollNumber(Long.parseLong(String.valueOf(map.get("enrollNumber"))));
							newuser.setName(String.valueOf(map.get("name")));
							newuser.setPrivilege(Long.parseLong(String.valueOf(map.get("privilege"))));
							newuser.setEnabled(Boolean.valueOf(String.valueOf(map.get("enabled"))));
							User result = userRepository.save(newuser);
							if (result != null) {
								status[0]++;
							} else {
								status[1]++;
							}
						} else {
							status[2]++;
						}
					});
					if (status[0] > 0 || status[2] > 0) {
						((Node) (event.getSource())).getScene().getWindow().hide();
						a.setAlertType(AlertType.INFORMATION);
						a.setContentText("User(s) added: " + status[0] + "\nUser(s) skipped: " + status[2]);
						a.show();
					} else if (status[1] > 0) {
						a.setAlertType(AlertType.ERROR);
						a.setContentText("Unable to add users.");
						a.show();
					}
				}
			}
			bio.disconnect();
		} else {
			a.setAlertType(AlertType.ERROR);
			a.setContentText("Unable to connect device.");
			a.show();
		}
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
