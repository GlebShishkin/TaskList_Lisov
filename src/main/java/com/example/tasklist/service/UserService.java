package com.example.tasklist.service;

import com.example.tasklist.domain.user.User;

public interface UserService {

    User getById(Long id);
    User getByUsername(String username);
    User update(User user);
    User create(User user);

    boolean isTaskOwner(Long userId, Long TaskId);
    void delete(Long id);
}
