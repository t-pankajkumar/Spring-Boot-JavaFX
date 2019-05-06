package com.springfx.beans;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AttendanceLog")
public class AttendanceLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;

	private long enrollNumber;

	private Timestamp punchTimestamp;

	private long deviceId;

	private long inOutMode;

	private long verifyMode;

	private long workCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEnrollNumber() {
		return enrollNumber;
	}

	public void setEnrollNumber(long enrollNumber) {
		this.enrollNumber = enrollNumber;
	}

	public Timestamp getPunchTimestamp() {
		return punchTimestamp;
	}

	public void setPunchTimestamp(Timestamp punchTimestamp) {
		this.punchTimestamp = punchTimestamp;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public long getInOutMode() {
		return inOutMode;
	}

	public void setInOutMode(long inOutMode) {
		this.inOutMode = inOutMode;
	}

	public long getVerifyMode() {
		return verifyMode;
	}

	public void setVerifyMode(long verifyMode) {
		this.verifyMode = verifyMode;
	}

	public long getWorkCode() {
		return workCode;
	}

	public void setWorkCode(long workCode) {
		this.workCode = workCode;
	}

}
