package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String NDF_MESS = "Not found user with id = ";
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    private boolean isNameEmpty(String name) {
        return name == null || name.isBlank() || name.isEmpty();
    }

    private User getAndCheckUser(Long id) {
        User user = userStorage.getById(id);

        if (user == null) {
            throw new NotFoundException(NDF_MESS + id);
        }

        return user;
    }

    private void addFriend(User user, User friendUser) {
        friendStorage.save(user.getId(), friendUser.getId());
    }

    private void deleteFriend(User user, User friendUser) {
        friendStorage.delete(user.getId(), friendUser.getId());
    }

    public User addUser(User user) {
        if (isNameEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        userStorage.save(user);
        friendStorage.save(user.getId(), null);

        log.info("Added user {}", user.getId());
        return user;
    }

    public User updateUser(User user) {
        User oldUser = getAndCheckUser(user.getId());

        if (isNameEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        userStorage.save(user);
        log.info("Updated user {}", user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = userStorage.getAll();

        log.info("Get All users {}", allUsers);
        return allUsers;
    }

    public User getUserById(Long id) {
        User foundedUser = getAndCheckUser(id);

        log.info("Founded user {}", foundedUser);
        return foundedUser;
    }

    public List<User> getAllUserFriends(Long id) {
        User user = getAndCheckUser(id);
        List<User> foundedFriends = friendStorage.getFriendsByUserId(user.getId())
                .stream()
                .map(friend -> userStorage.getById(friend))
                .collect(Collectors.toList());

        log.info("Founded friends {}", foundedFriends);
        return foundedFriends;
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User userParent = getAndCheckUser(userId);
        User userPrior = getAndCheckUser(friendId);
        List<User> foundedCommonFriends = friendStorage.getCommonFriends(userParent.getId(), userPrior.getId()).stream()
                .map(foundedFriend -> userStorage.getById(foundedFriend))
                .collect(Collectors.toList());

        log.info("Founded common friends {}", foundedCommonFriends);
        return foundedCommonFriends;
    }

    public void addToFriendUsers(Long id, Long friendId) {
        User user = getAndCheckUser(id);
        User friendUser = getAndCheckUser(friendId);

        addFriend(user, friendUser);
        addFriend(friendUser, user);
        log.info("Added friend {} and {}", id, friendId);
    }

    public void deleteFromFriendUsers(Long id, Long friendId) {
        User user = getAndCheckUser(id);
        User friendUser = getAndCheckUser(friendId);

        deleteFriend(user, friendUser);
        deleteFriend(friendUser, user);
        log.info("Deleted friend {} and {}", id, friendId);
    }
}
