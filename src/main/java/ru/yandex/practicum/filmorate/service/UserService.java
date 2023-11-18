package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String NDF_MESS = "Not found user with id = ";
    private final UserStorage userStorage;

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
        user.getFriends().add(friendUser.getId());
        userStorage.save(user);
    }

    private void deleteFriend(User user, User friendUser) {
        user.getFriends().remove(friendUser.getId());
    }

    public User addUser(User user) {
        if (isNameEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        return userStorage.save(user);
    }

    public User updateUser(User user) {
        User oldUser = getAndCheckUser(user.getId());

        if (isNameEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        userStorage.save(user);

        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(Long id) {
        return getAndCheckUser(id);
    }

    public List<User> getAllUserFriends(Long id) {
        User user = getAndCheckUser(id);

        return user.getFriends()
                .stream()
                .map(friendId -> userStorage.getById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long parentId, Long priorId) {
        User userParent = getAndCheckUser(parentId);
        User userPrior = getAndCheckUser(priorId);

        return userParent.getFriends().stream()
                .filter(friendId -> userPrior.getFriends().contains(friendId))
                .map(friendId -> userStorage.getById(friendId))
                .collect(Collectors.toList());
    }

    public void addToFriendUsers(Long id, Long friendId) {
        User user = getAndCheckUser(id);
        User friendUser = getAndCheckUser(friendId);

        addFriend(user, friendUser);
        addFriend(friendUser, user);
    }

    public void deleteFromFriendUsers(Long id, Long friendId) {
        User user = getAndCheckUser(id);
        User friendUser = getAndCheckUser(friendId);

        deleteFriend(user, friendUser);
        deleteFriend(friendUser, user);
    }
}
