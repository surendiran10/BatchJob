package com.suren.batchjob.repository;

import com.suren.batchjob.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
