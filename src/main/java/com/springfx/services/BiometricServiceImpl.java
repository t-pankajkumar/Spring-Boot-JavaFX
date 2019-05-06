package com.springfx.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.springfx.sdk.ZkemSDK;

@Service
@ConditionalOnProperty(name = "app.env", havingValue = "live")
public class BiometricServiceImpl implements BiometricService {
	ZkemSDK sdk;
	private String ip_addr;
	private int device_no;

	@Bean
	public BiometricServiceImpl te() {
		return new BiometricServiceImpl();

	}

	public BiometricServiceImpl() {
		super();
		System.out.println(124);
	}

	public boolean connect(String ip_addr, int device_no) {
		try {
			sdk = new ZkemSDK();
			this.ip_addr = ip_addr;
			this.device_no = device_no;
			sdk.initSTA();
			return sdk.connect(this.ip_addr, 4370, device_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void disconnect() {
		sdk.disConnect();
		sdk.release();
	}

	public String getSerialNumber() {
		String serialno = sdk.getSerialNumber(device_no);
		if (serialno != null) {
			return serialno;
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> getAttendanceLogs(Date date) {
		try {
			boolean flag = sdk.readLastestLogData(device_no, getLastDate(date)); // Read data into the cache
			if (flag)
				return sdk.getGeneralLogData(device_no); // Read the latest attendance data after the provided date
			else {
				System.out.println("Unable to get the logs, Error code:" + sdk.GetLastError());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> getGeneralAttendanceLogs() {
		try {
			boolean flag = sdk.readGeneralLogData(device_no);
			if (flag)
				return sdk.getGeneralLogData(device_no);
			else {
				System.out.println("Unable to get the logs, Error code:" + sdk.GetLastError());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> getUsers() {
		try {
			return sdk.SSR_GetAllUserInfo(device_no);
		} catch (Exception e) {
		}
		return null;
	}

	private static Date getLastDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}
}
