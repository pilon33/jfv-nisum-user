package com.nisum.test.user.controller;


import com.nisum.test.user.dtos.ErrorResponseDTO;
import com.nisum.test.user.dtos.PhoneDTO;
import com.nisum.test.user.dtos.UserByEmailRequestDTO;
import com.nisum.test.user.dtos.UserDTO;
import com.nisum.test.user.model.Phone;
import com.nisum.test.user.model.User;
import com.nisum.test.user.security.JwtTokenUtil;
import com.nisum.test.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @Autowired
    private PasswordEncoder bcryptEncoder;

    private static final String VALIDATE_OK = "OK";

    @PostMapping("/register-user")
    public ResponseEntity<Object> save(@RequestBody UserDTO userDTO){
            User user = new User();
            List<Phone> listPhone = new ArrayList <>();
            if(!userDTO.getPhones().isEmpty()) {
                for (int i = 0; i < userDTO.getPhones().size(); i++) {
                    PhoneDTO phoneDTO;
                    Phone phone = new Phone();

                    phoneDTO = userDTO.getPhones().get(i);
                    phone.setCityCode(phoneDTO.getCityCode());
                    phone.setCountryCode(phoneDTO.getCountryCode());
                    phone.setNumber(phoneDTO.getNumber());
                    listPhone.add(phone);
                }
                user.setPhones(listPhone);
            }
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setPassword(userDTO.getPassword());
            String newUserIdentifier = UUID.randomUUID().toString();
            user.setId(newUserIdentifier);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String formattedDate = formatter.format(date);
            user.setCreated(formattedDate);
            user.setPassword(bcryptEncoder.encode(user.getPassword()));
            user.setLastLogin(formattedDate);
            String token = jwtTokenUtil.generateTokenForNewUser(user.getName());
            user.setToken(token);
            return new ResponseEntity <>(userService.insert(user), HttpStatus.CREATED);
    }

    @PutMapping("/user")
    public ResponseEntity<Object> modify(@RequestBody UserDTO userDTO){
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setPassword(userDTO.getPassword());
            User dbUser = userService.findByToEmail(user.getEmail());
            if (!userDTO.getPhones().isEmpty()) {
                for (int i = 0; i < userDTO.getPhones().size(); i++) {
                    dbUser.getPhones().get(i).setCityCode(userDTO.getPhones().get(i).getCityCode());
                    dbUser.getPhones().get(i).setCountryCode(userDTO.getPhones().get(i).getCountryCode());
                    dbUser.getPhones().get(i).setNumber(userDTO.getPhones().get(i).getNumber());
                }

            }
            dbUser.setName(user.getName());
            dbUser.setPassword(bcryptEncoder.encode(user.getPassword()));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String formattedDate = formatter.format(date);
            dbUser.setModified(formattedDate);
            dbUser.setLastLogin(formattedDate);
            return new ResponseEntity <>(userService.insert(dbUser), HttpStatus.OK);

    }

    @GetMapping("/findUserByEmail")
    public ResponseEntity<Object> findUserByEmail(@RequestBody UserByEmailRequestDTO emailRequest){
            User foundUser = userService.findByToEmail(emailRequest.getEmail());
            return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    @GetMapping("/findAllUsers")
    public ResponseEntity<Object> findAllUsers(){
            List<User> foundUsers = userService.getUsers();
            return new ResponseEntity<>(foundUsers, HttpStatus.OK);
    }

}
