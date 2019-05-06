package com.springfx.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springfx.beans.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
	List<Device> findByIp(String ip);
}
