package com.icstar.attendance.Controllers;


import com.icstar.attendance.Models.Users;
import com.icstar.attendance.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("api")
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/list/users")
    public ResponseEntity<Iterable<Users>> getUsers(){

        Iterable<Users> listUser = userRepository.findAll();
        return new ResponseEntity<>(listUser, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> processLogin(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("role") String role,
        HttpSession session) {
        Users users = userRepository.findByUsernameAndRole(username, role);

        if (users != null && users.getPassword().equals(password)) {
//            session.setAttribute("loggedInUser", users);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("userId", users.getId());

            session.setAttribute("userRole", role);
            return new ResponseEntity<>(response, HttpStatus.OK);
//            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Login berhasil\"}");
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"status\": \"error\", \"message\": \"Login gagal\"}");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Logout berhasil\"}");
    }
}
