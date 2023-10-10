package com.crud.backend.controller;

import com.crud.backend.exception.UserNotFoundException;
import com.crud.backend.model.User;
import com.crud.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000/")
public class userController {

    @Autowired
    private UserRepository userRepository;
//    User newUser(@RequestBody User newUser){
//        return userRepository.save(newUser);
//    }
    @PostMapping("/user")
    public ResponseEntity<?> newUser(@RequestBody User newUser) {
        User existingUser = userRepository.findByUsername(newUser.getUsername());

        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User savedUser = userRepository.save(newUser);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> verifyUser(@RequestBody User userLogin) {
        User user = userRepository.findByUsername(userLogin.getUsername());

        if (user == null) {
            return ResponseEntity.badRequest().body("User Not Found");
        }

        if (!user.getPassword().equals(userLogin.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid Password");
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/adminlogin")
    public ResponseEntity<String> verifyAdmin(@RequestBody User adminLogin) {
        User user = userRepository.findByUsername(adminLogin.getUsername());

        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        if (!user.getUsername().equals("admin")) {
            return ResponseEntity.badRequest().body("You are Not a admin");
        }

        if (!user.getPassword().equals(adminLogin.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid admin Password");
        }
        return ResponseEntity.ok("Success");
    }


    @GetMapping("/user")
    List<User> getAllUser() {
        return  userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser,@PathVariable  Long id){
        return userRepository.findById(id)
            .map(user->{
                user.setUsername(newUser.getUsername());
                user.setEmail(newUser.getEmail());
                user.setPassword(newUser.getPassword());
                return userRepository.save(user);
            }).orElseThrow(()->new UserNotFoundException(id));
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "User With id "  +id+" is Deleted.";
    }


}
