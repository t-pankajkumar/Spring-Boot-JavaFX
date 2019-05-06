package com.springfx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springfx.beans.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEnrollNumber(String enrollNumber);
}