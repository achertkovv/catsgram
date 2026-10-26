package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.dto.UpdatePostRequest;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/posts")
    public PostDto createPost(@PathVariable("userId") long userId,
                              @RequestBody NewPostRequest postRequest) {
        return postService.createPost(userId, postRequest);
    }

    @PutMapping("/posts/{postId}")
    public PostDto updatePost(@PathVariable("postId") long postId,
                              @RequestBody UpdatePostRequest request) {
        return postService.updatePost(postId, request);
    }

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getPosts(@RequestParam(defaultValue = "desc") String sort,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        //параметр sort должен содержать корректное значение;
        if (sort == null || SortOrder.from(sort) == null) {
            throw new ParameterNotValidException("sort", "Получено: " + sort + " должно быть: ask или desc");
        }
        //параметр size должен быть больше нуля;
        if (size <= 0) {
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }
        //параметр from не может быть меньше нуля.
        if (from < 0) {
            throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
        }
        return postService.getPosts(SortOrder.from(sort), from, size);
    }

    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPostById(@PathVariable("postId") long postId) {
        return postService.getPostById(postId);
    }
}

/*
package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // Можно отметить конструктор аннотацией @Autowired — это явно укажет, что здесь нужно
    // произвести внедрение зависимостей. А можно опустить эту аннотацию, так как конструктор
    // единственный и Spring сам поймёт, что нужно создать объект типа PostService и передать
    // его при создании PostController.
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Для каждого из параметров строки запроса укажите значение по умолчанию.
    // Для этого воспользуйтесь элементом default аннотации @RequestParam.
    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "desc") String sort,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        //параметр sort должен содержать корректное значение;
        if (sort == null) {
            throw new ParameterNotValidException("sort", "Получено: " + sort + " должно быть: ask или desc");
        }
        //параметр size должен быть больше нуля;
        if (size <= 0) {
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }
        //параметр from не может быть меньше нуля.
        if (from < 0) {
            throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
        }
        return postService.findAll(SortOrder.from(sort), from, size);
    }

    @GetMapping("/{postId}")
    public Optional<Post> findById(@PathVariable long postId) {
        return postService.findById(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}
*/