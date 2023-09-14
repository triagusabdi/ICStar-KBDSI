package com.icstar.attendance.Repository;


import com.icstar.attendance.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsernameAndRole(String username, String role);
}
