package com.nisum.test.user.service;



import com.nisum.test.user.errors.EmailExistsException;
import com.nisum.test.user.model.User;

import java.util.List;

public interface UserService {

    User insert(User user) throws EmailExistsException;
    List<User> getUsers();
    User findByToEmail(String email);
    User findByToName(String name);


}

