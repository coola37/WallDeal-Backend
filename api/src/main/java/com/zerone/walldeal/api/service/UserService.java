package com.zerone.walldeal.api.service;

import com.zerone.walldeal.api.entity.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    void createUser(User user);
    User getUserById(String userId);
    Set<User> getAllUsers();
    void followOrUnfollow(String currentUserId, String targetUserId);
    Boolean checkFollow(String currentUserId, String targetUserId);
    //UserDTO userConvertUserDTO(User user);
    void editUser(User user);
    void deleteAccount(String userId);
}
