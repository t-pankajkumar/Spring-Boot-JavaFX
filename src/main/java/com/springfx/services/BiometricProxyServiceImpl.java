package com.springfx.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="app.env", havingValue="dev")
public class BiometricProxyServiceImpl implements BiometricService {
	private String ip_addr;
	private int device_no;
	

	public BiometricProxyServiceImpl() {
		super();
		System.out.println(1);
	}

	public boolean connect(String ip_addr, int device_no) {
		try {
			this.ip_addr = ip_addr;
			this.device_no = device_no;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void disconnect() {

	}

	public String getSerialNumber() {
		return "123";
	}

	public List<Map<String, Object>> getAttendanceLogs(Date date) {
		List<Map<String, Object>> strList = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("EnrollNumber", 123);
		m.put("Time", 2018 + "-0" + 8 + "-0" + 2 + " " + 10 + ":" + 30 + ":" + 25);
		m.put("VerifyMode", 255);
		m.put("InOutMode", 1);
		m.put("WorkCode", 0);
		strList.add(m);
		
		Map<String, Object> m2 = new HashMap<String, Object>();
		m2.put("EnrollNumber", 124);
		m2.put("Time", 2018 + "-0" + 8 + "-0" + 2 + " " + 10 + ":" + 20 + ":" + 25);
		m2.put("VerifyMode", 255);
		m2.put("InOutMode", 1);
		m2.put("WorkCode", 0);
		strList.add(m2);
		return strList;
	}

	public List<Map<String, Object>> getUsers() {
		List<Map<String, Object>> listUser = new ArrayList<Map<String, Object>>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("enrollNumber", 123);
		userMap.put("name", "Pankaj");
		userMap.put("password", "");
		userMap.put("privilege", 0);
		userMap.put("enabled", true);
		listUser.add(userMap);

		Map<String, Object> userMap2 = new HashMap<String, Object>();
		userMap2.put("enrollNumber", 124);
		userMap2.put("name", "Kumar");
		userMap2.put("password", "");
		userMap2.put("privilege", 0);
		userMap2.put("enabled", true);
		listUser.add(userMap2);
		return listUser;
	}
}
