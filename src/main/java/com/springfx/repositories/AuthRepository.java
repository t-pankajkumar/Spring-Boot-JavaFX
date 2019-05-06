package com.springfx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springfx.beans.Auth;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

}
