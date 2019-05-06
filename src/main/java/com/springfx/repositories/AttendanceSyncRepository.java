package com.springfx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springfx.beans.AttendanceSync;

@Repository
public interface AttendanceSyncRepository extends JpaRepository<AttendanceSync, Long> {

}
