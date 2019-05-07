package com.springfx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.springfx.beans.Device;
import com.springfx.repositories.DeviceRepository;
import com.springfx.services.BiometricService;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

@Controller
public class DeviceController {

	@FXML
	private TextField ip_addr;

	@FXML
	private TextField device_no;

	@FXML
	private TextField srl_no;

	@FXML
	private Label teststatus;

	@FXML
	private ToggleGroup deviceTypeToggleGroup;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private BiometricService bio;

	@FXML
	public void getSerial(Event event) {
		if (!ip_addr.getText().equals("") && !device_no.getText().equals("")) {
			if (bio.connect(ip_addr.getText(), Integer.parseInt(device_no.getText()))) {
				String serialno = bio.getSerialNumber();
				bio.disconnect();
				if (serialno != null) {
					srl_no.setText(serialno);
				} else {

				}
			}
		}
	}

	@FXML
	public void save(Event event) {
		Alert a = new Alert(AlertType.NONE);
		RadioButton selectedRadioButton = (RadioButton) deviceTypeToggleGroup.getSelectedToggle();
		if (!ip_addr.getText().equals("") && !device_no.getText().equals("") && selectedRadioButton != null) {
			if (bio.connect(ip_addr.getText(), Integer.parseInt(device_no.getText()))) {
				String serialno = bio.getSerialNumber();
				bio.disconnect();
				Device result = null;
				if (serialno != null) {
					List<Device> devices = deviceRepository.findByIp(ip_addr.getText());
					if (devices.size() > 0) {
						a.setAlertType(AlertType.ERROR);
						a.setContentText("Device already exists.");
						a.show();
					} else {
						Device device = new Device();
						device.setIp(ip_addr.getText());
						device.setDeviceno(device_no.getText());
						device.setSerialno(serialno);
						device.setType(selectedRadioButton.getText());
						result = deviceRepository.save(device);
					}
					if (result != null)
						((Node) (event.getSource())).getScene().getWindow().hide();
					a.setAlertType(AlertType.INFORMATION);
					a.setContentText("Device added successfully.");
					a.show();
				} else {
					a.setAlertType(AlertType.ERROR);
					a.setContentText("Unable to add device.");
					a.show();
				}
			}
		} else {
			a.setAlertType(AlertType.ERROR);
			a.setContentText("Unable to connect device.");
			a.show();
		}
	}

	@FXML
	public void test(Event event) {
		if (!ip_addr.getText().equals("") && !device_no.getText().equals("")) {
			System.out.println(bio);
			if (bio.connect(ip_addr.getText(), Integer.parseInt(device_no.getText()))) {
				bio.disconnect();
				teststatus.setText("Connected");
			} else {
				teststatus.setText("Unable to connect");
			}
		}
	}

	@FXML
	public void close(ActionEvent event) {
		((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
	}
}
