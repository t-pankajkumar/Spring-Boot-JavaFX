package com.springfx.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;

	private long enrollNumber;

	private String name;

	private long privilege;

	private boolean enabled;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getEnrollNumber() {
		return enrollNumber;
	}

	public void setEnrollNumber(long enrollNumber) {
		this.enrollNumber = enrollNumber;
	}

	public long getPrivilege() {
		return privilege;
	}

	public void setPrivilege(long privilege) {
		this.privilege = privilege;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
