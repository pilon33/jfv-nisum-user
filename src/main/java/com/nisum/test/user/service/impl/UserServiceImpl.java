package com.nisum.test.user.service.impl;


import com.nisum.test.user.errors.DataFormatException;
import com.nisum.test.user.errors.EmailExistsException;
import com.nisum.test.user.model.User;
import com.nisum.test.user.repository.IUserRepository;
import com.nisum.test.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User insert(User user) throws EmailExistsException {
        User userExists = null;
        userExists =findByToEmail(user.getEmail());
    	if (userExists!= null) {
            throw new EmailExistsException(HttpStatus.CONFLICT,
        		"El correo ya se encuentra registrado: "
                + user.getEmail());
        } else if (!isValidEmail(user.getEmail())) {
        	throw new DataFormatException(HttpStatus.BAD_REQUEST,
    			"El correo no tiene el formato correcto: "
    			+ user.getEmail());
        } else if (!isValidPassword(user.getPassword())) {
        	throw new DataFormatException(HttpStatus.BAD_REQUEST,
    			"El password no tiene el formato correcto: "
    			+ user.getPassword());
    	}

        return userRepository.save(user);
    }

    @Override
    public List <User> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User findByToEmail(String email) {
        return  userRepository.findByEmail(email);

    }

    @Override
    public User findByToName(String name) {
        return  userRepository.findByName(name);
    }


    private User emailExists(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidEmail(String email) {
    	String regex = "^.*@[a-zA-Z]+\\.[a-zA-Z]+$";
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(email);

    	return matcher.matches();
    }

    private boolean isValidPassword(String password) {
    	String regex = "^(?=(?:.*?[A-Z]){1})(?=.*?[a-z])(?=(?:.*?[0-9]){2}).*$";
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(password);

    	return matcher.matches();
    }
}
