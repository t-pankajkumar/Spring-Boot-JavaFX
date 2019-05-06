package com.springfx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springfx.beans.AttendanceLog;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

}
