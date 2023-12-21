package com.spring.springJwt.controller;

import com.spring.springJwt.FileUploadUtil;
import com.spring.springJwt.models.User;
import com.spring.springJwt.models.User;
import com.spring.springJwt.payload.response.MessageResponse;
import com.spring.springJwt.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
// for Angular Client (withCredentials)
// @CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    public PasswordEncoder encoder;
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<User>> userAccess() {
        List<User> users =new ArrayList<User>();
        userRepository.findAll().forEach(users::add);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
//    @PostMapping("/user/add")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public ResponseEntity<?> createUser(@Valid @RequestBody User user) throws IOException {
//        if (userRepository.existsByUsername(user.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
////            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
//        User user1 = new User(user.getUsername(), user.getEmail(), encoder.encode(user.getPassword()));
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        user1.setAvatar(filename);
//        User saveUser = userRepository.save(user1); //"./logo/"
//        String uploadDir = "D:\\VueJsProject\\vue-3-authentication-jwt\\src\\assets\\images_user\\" + saveUser.getId();
//        FileUploadUtil.saveFile(uploadDir, filename, file);
//        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
//        userRepository.save(user1);
//        return ResponseEntity.ok(new MessageResponse("User add successfully!"));
//    }
@PostMapping(value = "/user/add" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public ResponseEntity<?> createUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file) throws IOException {
    if(userRepository.existsByUsername(user.getUsername())){

        return ResponseEntity.badRequest().body(new MessageResponse("User name had already used"));
    }
    User user1 = userRepository.save(new User(user.getUsername(), user.getEmail(),
            encoder.encode(user.getPassword())));

    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    user1.setAvatar(filename);
    User saveUser = userRepository.save(user1); //"./logo/"
    String uploadDir = "D:\\VueJsProject\\vue-3-authentication-jwt\\src\\assets\\images_user\\" + saveUser.getId();
    FileUploadUtil.saveFile(uploadDir, filename, file);

    return new ResponseEntity<>(saveUser, HttpStatus.CREATED);

}

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        User user = userRepository.findById(id).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user){
        User userUpdate =userRepository.findById(id).get();
        userUpdate.setUsername(user.getUsername());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setRoles(user.getRoles());
        userUpdate.setPassword(user.getPassword());
        return new ResponseEntity<>(userRepository.save(userUpdate),HttpStatus.OK);

    }
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        userRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        userRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}