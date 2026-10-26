package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.UserRepository;
import ru.yandex.practicum.catsgram.dto.NewUserRequest;
import ru.yandex.practicum.catsgram.dto.UpdateUserRequest;
import ru.yandex.practicum.catsgram.dto.UserDto;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.UserMapper;
import ru.yandex.practicum.catsgram.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(NewUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }

        Optional<User> alreadyExistUser = userRepository.findByEmail(request.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new DuplicatedDataException("Данный Email уже используется");
        }

        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto getUserById(long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(long userId, UpdateUserRequest request) {
        User updatedUser = userRepository.findById(userId)
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }
}

/*
package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class UserService {
    // Для хранения данных используйте HashMap.
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        // Если в запросе на добавление нового пользователя не указан адрес электронной почты,
        // то должно генерироваться исключение ConditionsNotMetException с описанием: "Email должен быть указан".
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }
        // Если пользователь с указанным адресом электронной почты уже был добавлен ранее,
        // то генерируется исключение DuplicatedDataException с описанием: "Этот email уже используется".
        if (isEmailExists(user.getEmail())) {
            throw new DuplicatedDataException("Этот email уже используется");
        }
        // формируем дополнительные данные
        user.setId(getNextId());
        // и не забудьте задать дату регистрации нового пользователя.
        user.setRegistrationDate(Instant.now());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        // Если при изменении данных пользователя не указан его идентификатор, то должно генерироваться
        // исключение ConditionsNotMetException с текстом: "Id должен быть указан".
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            // Если при обновлении данных пользователя, указан новый адрес электронной почты и в
            // приложении уже есть пользователь с таким адресом, то должно генерироваться исключение
            // DuplicatedDataException с описанием: "Этот email уже используется".
            if (isEmailExists(newUser.getEmail())) {
                throw new DuplicatedDataException("Этот email уже используется");
            }
            // Если в запросе на обновление данных пользователя поля с адресом электронной почты,
            // именем пользователя или паролем равны null, данные поля не нужно обновлять.
            if (newUser.getEmail() != null) oldUser.setEmail(newUser.getEmail());
            if (newUser.getUsername() != null) oldUser.setUsername(newUser.getUsername());
            if (newUser.getPassword() != null) oldUser.setPassword(newUser.getPassword());
            return oldUser;
        }
        throw new NotFoundException("Пост с id = " + newUser.getId() + " не найден");
    }

    // В класс UserService нужно добавить метод, который находит (и возвращает) пользователя по id:
    // (например, с названием findUserById). Метод должен возвращать Optional с пользователем,
    // если он найден, и пустой Optional, если пользователь не найден.
    public Optional<User> findUserById(Long id) {
        return users.values()
                .stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findAny();
    }

    public Optional<User> findById(long authorId) {
        return Optional.ofNullable(users.get(authorId));
    }

    private boolean isEmailExists(String email) {
        Optional<User> userOptional = users.values()
                .stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findAny();

        // Пользователь с таким email найден
        return userOptional.isPresent();
    }

    // Напишите метод getNextId для генерации идентификатора пользователя при создании аккаунта
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
*/