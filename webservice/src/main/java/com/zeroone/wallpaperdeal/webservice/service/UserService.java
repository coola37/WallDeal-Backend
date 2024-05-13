package com.zeroone.wallpaperdeal.webservice.service;

import com.zeroone.wallpaperdeal.webservice.entity.User;
import com.zeroone.wallpaperdeal.webservice.entity.UserDTO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    public User createUser(User user);
    public User getUser(String userId);
    public List<User> getUsers();
    public void followOrUnfollow(String currentUserId, String targetUserId);
    public Boolean checkFollow(String currentUserId, String targetUserId);
    public UserDTO userConvertUserDTO(User user);
    public void editUser(User user);
    public void deleteAccount(String userId) throws ExecutionException, InterruptedException;
}
