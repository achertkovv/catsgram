package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dal.UserRepository;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.dto.UpdatePostRequest;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.PostMapper;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Comparator<Post> postDateComparator = Comparator.comparing(Post::getPostDate);

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // сохранение отдельного поста, связанного с указанным пользователем
    public PostDto createPost(long userId, NewPostRequest request) {
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Post post = PostMapper.mapToPost(request);
        post.setAuthorId(userId);
        post = postRepository.save(post);
        return PostMapper.mapToPostDto(post);
    }

    public PostDto getPostById(long postId) {
        return postRepository.findById(postId)
                .map(PostMapper::mapToPostDto)
                .orElseThrow(() -> new NotFoundException("Сообщение с ID: " + postId + " не найдено"));
    }

    public List<PostDto> getPosts(SortOrder sort, int from, int size) {
        return postRepository.findAll()
                .stream()
                .sorted(sort.equals(SortOrder.ASCENDING) ?
                        postDateComparator : postDateComparator.reversed())
                .skip(from)
                .limit(size)
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    public PostDto updatePost(long postId, UpdatePostRequest request) {
        Post updatedPost = postRepository.findById(postId)
                .map(post -> PostMapper.updatePostFields(post, request))
                .orElseThrow(() -> new NotFoundException("Сообщение не найдено"));
        updatedPost = postRepository.update(updatedPost);
        return PostMapper.mapToPostDto(updatedPost);
    }
}

/*
package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.time.Instant;
import java.util.*;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final UserService userService;

    private final Map<Long, Post> posts = new HashMap<>();
    private final Comparator<Post> postDateComparator = Comparator.comparing(Post::getPostDate);

    // Нужно внедрить UserService как зависимость в PostService.
    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(SortOrder sort, int from, int size) {
        return posts.values()
                .stream()
                .sorted(sort.equals(SortOrder.ASCENDING) ?
                        postDateComparator : postDateComparator.reversed())
                .skip(from)
                .limit(size)
                .toList();
    }

    public Optional<Post> findById(long postId) {
        return Optional.ofNullable(posts.get(postId));
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        // В методе create класса PostService нужно добавить проверку на то, что автор поста существует.
        // С помощью метода userService.findUserById(authorId) найти автора поста по его идентификатору.
        // В случае, если автор не найден, выбросить исключение ConditionsNotMetException с
        // сообщением «Автор с id = <тут нужно вставить id> не найден».
        //if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            // Понять, что автор не найден, можно, если метод findUserById возвращает пустой Optional.
        //    throw new ConditionsNotMetException("Автор с id = " + post.getAuthorId() + " не найден");
        //}

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
*/