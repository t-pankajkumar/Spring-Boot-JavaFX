package com.springfx.sdk;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ZkemSDK {
	private static ActiveXComponent zkem;

	public void initSTA() {
		// Statically load zkemkeeper.dll, zkemkeeper.ZKEM.1 for the ProgID value in the
		// registry
		// Build an ActiveX component instance
		zkem = new ActiveXComponent("zkemkeeper.ZKEM.1");
		ComThread.InitSTA(); // call initialization and put into memory waiting to call new ActiveXComponent
								// ("zkemkeeper.ZKEM.1")
	}

	public void release() {
		// Release the occupied memory space but such a call will have a serious
		// problem, when the amount of access is too large, the initialization of too
		// much memory can not be released in time, this will lead to memory overflow,
		// the application will crash, the best You have to restart the service and
		// re-release the project.
		// The long connection was originally run on the Windows platform, but after
		// some technical processing, it can also be called in Java. This problem has
		// arisen. The solution is still there, Net develops the webService call, and
		// then generates the Java client code.
		// Then use java to call, so the problem is solved, and the efficiency is also
		// very good, easy to use.
		ComThread.Release();
	}

	/**
	 * Connect to the attendance machine
	 * 
	 * @param address
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public boolean connect(String address, int port, int machineNum) throws Exception {
		// Connect to the attendance machine, return whether the connection is
		// successful, successfully returns true, failure returns false.
		// 1, Connect_NET: zkem method, connected to the central control attendance
		// machine through the network.
		// 2, address: IP address of the central control attendance machine.
		// 3, port: port number
		boolean result = zkem.invoke("Connect_NET", address, port).getBoolean();
		if (result) {
			try {
				// encrypt: MD5 encryption method, incoming central control attendance machine
				// number, MD5 encryption, return encrypted machine number
				// String sn = encrypt(getSerialNumber(machineNum));
				// Encrypt the machine number, whether it is consistent with the number read
				// if (sns.contains(sn)) {
				result = true;
				// } else {
				// throw new Exception(" sequence code exception! ");
				// }
			} catch (Exception e) {
				throw new Exception(" sequence code exception! ");
			}

		}
		return result;
	}

	/**
	 * Disconnect the attendance machine
	 */
	public void disConnect() {
		zkem.invoke("Disconnect");
	}

	/**
	 * Read all attendance data into the cache. Used in conjunction with
	 * getGeneralLogData.
	 * 
	 * @return
	 */
	public boolean readGeneralLogData(int machineNum) {
		// Call the ReadGeneralLogData method in zkem, pass parameters, machine number
		boolean result = zkem.invoke("ReadGeneralLogData", new Variant[] { new Variant(machineNum) }).getBoolean();
		return result;
	}

	/**
	 * Read the latest attendance data after this time. Used in conjunction with
	 * getGeneralLogData.
	 * 
	 * @param lastest
	 * @return
	 */
	public boolean readLastestLogData(int machineNum, Date lastest) {
		Calendar c = Calendar.getInstance();
		c.setTime(lastest);

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		int seconds = c.get(Calendar.SECOND);

		Variant v0 = new Variant(machineNum);
		Variant vLog = new Variant(1);
		Variant dwYear = new Variant(year, true);
		Variant dwMonth = new Variant(month, true);
		Variant dwDay = new Variant(day, true);
		Variant dwHour = new Variant(hours, true);
		Variant dwMinute = new Variant(minutes, true);
		Variant dwSecond = new Variant(seconds, true);

		boolean result = zkem.invoke("ReadLastestLogData", v0, vLog, dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond).getBoolean();
		return result;
	}

	/**
	 * Get attendance data in the cache. Used in conjunction with readGeneralLogData
	 * / readLastestLogData. The map returned by @return contains the following key
	 * values: "EnrollNumber" staff number "Time" attendance time string, format:
	 * yyyy-MM-dd HH:mm:ss "VerifyMode" Authentication method: 0 for password
	 * verification, 1 for fingerprint verification, 2 for card verification...
	 * "InOutMode" ?? 0Check-In 1Check-Out 2Break-O 3Break-In 4OT-In 5OT-Out
	 */
	public List<Map<String, Object>> getGeneralLogData(int machineNum) {
		Variant v0 = new Variant(machineNum);
		Variant dwEnrollNumber = new Variant("", true);
		Variant dwVerifyMode = new Variant(0, true);
		Variant dwInOutMode = new Variant(0, true);
		Variant dwYear = new Variant(0, true);
		Variant dwMonth = new Variant(0, true);
		Variant dwDay = new Variant(0, true);
		Variant dwHour = new Variant(0, true);
		Variant dwMinute = new Variant(0, true);
		Variant dwSecond = new Variant(0, true);
		Variant dwWorkCode = new Variant(0, true);
		List<Map<String, Object>> strList = new ArrayList<Map<String, Object>>();
		boolean newresult = false;
		do {
			Variant vResult = Dispatch.call(zkem, "SSR_GetGeneralLogData", v0, dwEnrollNumber, dwVerifyMode, dwInOutMode, dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond, dwWorkCode);
			newresult = vResult.getBoolean();
			if (newresult) {
				String enrollNumber = dwEnrollNumber.getStringRef();

				// If there is no number, skip it.
				if (enrollNumber == null || enrollNumber.trim().length() == 0)
					continue;
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("EnrollNumber", enrollNumber);
				m.put("Time", dwYear.getIntRef() + "-" + dwMonth.getIntRef() + "-" + dwDay.getIntRef() + " " + dwHour.getIntRef() + ":" + dwMinute.getIntRef() + ":" + dwSecond.getIntRef());
				m.put("VerifyMode", dwVerifyMode.getIntRef());
				m.put("InOutMode", dwInOutMode.getIntRef());
				m.put("WorkCode", dwWorkCode.getIntRef());
				strList.add(m);
			}
		} while (newresult == true);
		return strList;
	}

	/**
	 * Read all user information into the PC memory, including user number,
	 * password, name, card number, etc. (except fingerprint template). The function
	 * is executed After that, you can get the user information with a GetAllUserID
	 * Prototype: VARIANT_BOOL ReadAllUserID([in]long dwMachineNumber)
	 * 
	 * @param machineNumber machine number
	 * @return cache returns true, cache fails to return false
	 */
	public boolean ReadAllUserID(int machineNumber) {
		return zkem.invoke("ReadAllUserID", new Variant(machineNumber)).getBoolean();
	}

	/**
	 * Get all user information, before executing this function, ReadAllUserID can
	 * be used to read all user information to PC memory, SSR_GetAllUserInfo Execute
	 * once, point to the user information pointer to move to the next record, when
	 * all user information is read, the function returns false Prototype:
	 * VARIANT_BOOL SSR_GetAllUserInfo([in]LONG dwMachineNumber,[out]BSTR*
	 * dwEnrollNumber, [out] BSTR* Name,[out]BSTR* Password,[out] LONG*
	 * Privilege,[out] VARIANT_BOOL* Enabled)
	 * 
	 * dwMachineNumber: machine number dwEnrollNumber: user number Name: User name
	 * Password: User password Privilege: user rights, 3 administrators, 0 regular
	 * users Enabled: User enable flag, 1 is enabled, 0 is disabled
	 * 
	 * @param inMachineNumber machine number
	 * @return List<Map<String,Object>> user information, read failure returns null
	 */
	public List<Map<String, Object>> SSR_GetAllUserInfo(int inMachineNumber) {
		List<Map<String, Object>> listUser = new ArrayList<Map<String, Object>>();

		boolean status = this.ReadAllUserID(inMachineNumber);

		if (status == false) {
			return null;
		}

		Variant machineNumber = new Variant(1, true);
		Variant enrollNumber = new Variant("", true);
		Variant name = new Variant("", true);
		Variant password = new Variant("", true);
		Variant privilege = new Variant(0, true);
		Variant enable = new Variant(false, true);

		while (status) {
			status = zkem.invoke("SSR_GetAllUserInfo", machineNumber, enrollNumber, name, password, privilege, enable).getBoolean();

			// Skip if there is no user number
			String strEnrollnumber = enrollNumber.getStringRef();
//			System.out.println(strEnrollnumber);
			if (strEnrollnumber == null || strEnrollnumber.trim().length() == 0)
				continue;

// 			// skip if there is no name
//			if(strName==null || strName.trim().length()==0)
//				continue;

			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("enrollNumber", Integer.valueOf(strEnrollnumber));
			userMap.put("name", cutString(name));
			userMap.put("password", password.getStringRef());
			userMap.put("privilege", privilege.getIntRef());
			userMap.put("enabled", enable.getBooleanRef());
			listUser.add(userMap);
		}
		return listUser;
	}

	private String cutString(Variant name) {
		String nameStr = name.getStringRef();
		char[] n = nameStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n.length; i++) {
			if (n[i] == 0) {
				break;
			}
			sb.append(n[i]);
		}
		nameStr = sb.toString();
		return nameStr;
	}

	/**
	 * Get the attendance machine serial code
	 */
	public String getSerialNumber(int machineNum) {
		// Variant: Variant type that dynamically changes the type during runtime.
		// Variant types can support all simple data types, such as integers, floats,
		// strings, booleans, datetimes, currency, and OLE automation objects, and
		// cannot represent Object Pascal objects.
		Variant v0 = new Variant(machineNum);
		Variant dwSerialNumber = new Variant("", true);
		// zkem.invoke: Calls the method represented by the Method class, which
		// indicates that the GetSerialNumber method in zkem is called.
		boolean result = zkem.invoke("GetSerialNumber", v0, dwSerialNumber).getBoolean();
		if (result) {
			return dwSerialNumber.getStringRef();
		}
		return null;
	}

	public Integer GetLastError() {
		Variant errorCode = new Variant(0, true);
		boolean status = zkem.invoke("GetLastError", errorCode).getBoolean();
		if (status == false) {
			return null;
		}
		return errorCode.getIntRef();
	}

	/**
	 * MD5 encryption
	 * 
	 * @return
	 */
	public String encrypt(String s) throws Exception {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		byte[] btInput = s.getBytes();
		MessageDigest mdInst;
		mdInst = MessageDigest.getInstance("MD5");
		mdInst.update(btInput);
		byte[] md = mdInst.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
}