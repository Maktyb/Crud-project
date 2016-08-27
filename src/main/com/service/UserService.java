package com.service;


import com.model.User;

import java.util.List;

public interface UserService {
    public void addUser (User user);

    public void updateUser (User user);

    public void removeUser (int id);

    public List<User> searchUser (String name);

    public User getUserById(int id );

    public List<User> listUsers();


}

