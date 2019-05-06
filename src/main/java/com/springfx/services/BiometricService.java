package com.springfx.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


public interface BiometricService {
	public boolean connect(String ip_addr, int device_no);
	public void disconnect();
	public String getSerialNumber();
	public List<Map<String, Object>> getAttendanceLogs(Date date);
	public List<Map<String, Object>> getUsers();
}
