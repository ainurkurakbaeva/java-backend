package com.taskflow.TaskFlow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import taskFlow.DTO.request.UserRequest;
import taskFlow.DTO.response.UserResponse;
import taskFlow.model.User;
import taskFlow.repository.UserRepository;
import taskFlow.service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    private UserRequest createUserRequest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("210103189@stu.sdu.edu.kz");
        userRequest.setFirstName("Student");
        userRequest.setLastName("SDU");
        userRequest.setActive(true);
        return userRequest;
    }
    private User createUser(UserRequest userRequest){
        User user = new User();
        user.setId(7L);
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setActive(userRequest.getActive());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
    @Test
    void testCreateUser(){
        UserRequest userRequest = createUserRequest();
        User savedUser = createUser(createUserRequest());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        UserResponse userResponse = userService.createUser(userRequest);
        assertEquals(userRequest.getEmail(), userResponse.getEmail());
        assertEquals(userRequest.getFirstName(), userResponse.getFirstName());
        assertEquals(userRequest.getLastName(), userResponse.getLastName());
    }
    @Test
    void testUserFindByIdSuccess(){
        User user = createUser(createUserRequest());
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        UserResponse userResponse = userService.findById(7L);
        assertEquals(userResponse.getId(), user.getId());
        verify(userRepository).findById(7L);
        System.out.println("success");
    }
    @Test
    void testUserFindByIdNotFound(){
        when(userRepository.findById(111L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findById(111L), "id not found");
    }
    @Test
    void testCreateUser_WithDuplicateEmail_ShouldThrowException() {
        UserRequest userRequest = createUserRequest();
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userRequest);
        });

        assertEquals("Email already exists: 210103189@stu.sdu.edu.kz", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
//•	Проверить валидацию данных (например, уникальность email).