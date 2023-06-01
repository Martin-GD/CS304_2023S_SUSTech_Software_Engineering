package com.team127.atom.ControllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.team127.atom.model.Response;
import com.team127.atom.model.User;
import com.team127.atom.service.UserService;
import com.team127.atom.util.JWTUtil;
import com.team127.atom.web.controller.UserController;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setName("test");
        user.setPassword("password");

        User userDB = new User();
        userDB.setId("123");
        userDB.setName("test");
        userDB.setPassword("password");

        when(userService.getUserByName(user.getName())).thenReturn(userDB);

        Response expectedResponse = Response.success(JWTUtil.sign(userDB.getId(), userDB.getPassword()));
        Response actualResponse = userController.login(user);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testLoginUserDoesNotExist() {
        User user = new User();
        user.setName("test");
        user.setPassword("password");

        when(userService.getUserByName(user.getName())).thenReturn(null);

        Response expectedResponse = Response.error(1000, "User does not exist");
        Response actualResponse = userController.login(user);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testLoginWrongPassword() {
        User user = new User();
        user.setName("test");
        user.setPassword("password");

        User userDB = new User();
        userDB.setId("123");
        userDB.setName("test");
        userDB.setPassword("wrongpassword");

        when(userService.getUserByName(user.getName())).thenReturn(userDB);

        Response expectedResponse = Response.error(1001, "Wrong password");
        Response actualResponse = userController.login(user);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testRegisterSuccess() {
        User user = new User();
        user.setName("test");
        user.setPassword("password");

        when(userService.getUserByName(user.getName())).thenReturn(null);
        when(userService.insertUser(user)).thenReturn(true);

        Response expectedResponse = Response.success(null);
        Response actualResponse = userController.register(user);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        User user = new User();
        user.setName("test");
        user.setPassword("password");

        User userDB = new User();
        userDB.setId("123");
        userDB.setName("test");
        userDB.setPassword("password");

        when(userService.getUserByName(user.getName())).thenReturn(userDB);

        Response expectedResponse = Response.error(1002, "User already exists");
        Response actualResponse = userController.register(user);

        assertEquals(expectedResponse, actualResponse);
    }

//    @Test
//    public void testModifyProfileSuccess() {
//        User user = new User();
//        user.setId("123");
//        user.setName("test");
//        user.setPassword("password");
//
//        String token = JWTUtil.sign(user.getId(), user.getPassword());
//
//        when(userService.getUserById(user.getId())).thenReturn(user);
//        assertEquals(true, JWTUtil.verify(token, user.getId(), user.getPassword()));
//        when(userService.saveUser(user)).thenReturn(true);
//
//        Response expectedResponse = Response.success(null);
//        Response actualResponse = userController.modifyProfile(token, user);
//
//        assertEquals(expectedResponse, actualResponse);
//        assertEquals(200, actualResponse.getCode());
//    }

//    @Test
//    public void testModifyProfileTokenVerificationFailed() {
//        User user = new User();
//        user.setId("123");
//        user.setName("test");
//        user.setPassword("password");
//
//        String token = JWTUtil.sign(user.getId(), "wrongpassword");
//
//        when(userService.getUserById(user.getId())).thenReturn(user);
//        assertEquals(false, JWTUtil.verify(token, user.getId(), user.getPassword()));
//
//        Response expectedResponse = Response.error(1004, "Token verification failed");
//        Response actualResponse = userController.modifyProfile(token, user);
//
//        assertEquals(expectedResponse, actualResponse);
//    }

    @Test
    public void testModifyPasswordSuccess() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), user.getPassword());

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(true, JWTUtil.verify(token, user.getId(), user.getPassword()));
        when(userService.saveUser(user)).thenReturn(true);

        Response expectedResponse = Response.success(null);
        Response actualResponse = userController.modifyPassword(token, "newpassword");

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testModifyPasswordTokenVerificationFailed() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), "wrongpassword");

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(false, JWTUtil.verify(token, user.getId(), user.getPassword()));

        Response expectedResponse = Response.error(1004, "Token verification failed");
        Response actualResponse = userController.modifyPassword(token, "newpassword");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testLogout() {
        Response expectedResponse = Response.success(null);
        Response actualResponse = userController.logout("test");

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testChangeAvatarSuccess() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), user.getPassword());

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(true, JWTUtil.verify(token, user.getId(), user.getPassword()));

        Response expectedResponse = Response.success("http://10.26.11.88:8080/avatar/123");
        byte[] testFile = new byte[1024];
        InputStream inputStream = new ByteArrayInputStream(testFile);
        MultipartFile multipartFile;
        try {
            multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", inputStream);
        } catch (IOException e) {
            multipartFile = null;
            e.printStackTrace();
        }
        Response actualResponse = userController.changeAvatar(token, multipartFile);
        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testChangeAvatarTokenVerificationFailed() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), "wrongpassword");

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(false, JWTUtil.verify(token, user.getId(), user.getPassword()));

        Response expectedResponse = Response.error(1004, "Token verification failed");
        Response actualResponse = userController.changeAvatar(token, null);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetProfileSuccess() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), user.getPassword());

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(true, JWTUtil.verify(token, user.getId(), user.getPassword()));

        Response expectedResponse = Response.success(user);
        Response actualResponse = userController.getProfile(user.getId());

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }


    @Test
    public void testGetAvatarSuccess() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), user.getPassword());

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(true, JWTUtil.verify(token, user.getId(), user.getPassword()));
        when(userService.getAvatar(user.getId())).thenReturn(new byte[1024]);

        Response actualResponse = userController.getAvatar(token);

        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testGetAvatarTokenVerificationFailed() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), "wrongpassword");

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(false, JWTUtil.verify(token, user.getId(), user.getPassword()));

        Response expectedResponse = Response.error(1004, "Token verification failed");
        Response actualResponse = userController.getAvatar(token);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetLikeUserSuccess() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), user.getPassword());

        List<User> userList = new ArrayList<>();
        userList.add(new User());

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertTrue(JWTUtil.verify(token, user.getId(), user.getPassword()));
        when(userService.getLikeUser(user.getId())).thenReturn(userList);

        Response expectedResponse = Response.success(userList);
        Response actualResponse = userController.getlikeUser(user.getId());

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testLikeUserSuccess() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), user.getPassword());

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(true, JWTUtil.verify(token, user.getId(), user.getPassword()));
        when(userService.likeUser(user.getId(), "456")).thenReturn(true);

        Response expectedResponse = Response.success(null);
        Response actualResponse = userController.likeUser(token, "456");

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testLikeUserTokenVerificationFailed() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), "wrongpassword");

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(false, JWTUtil.verify(token, user.getId(), user.getPassword()));

        Response expectedResponse = Response.error(1004, "Token verification failed");
        Response actualResponse = userController.likeUser(token, "456");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testUnlikeUserSuccess() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), user.getPassword());

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(true, JWTUtil.verify(token, user.getId(), user.getPassword()));
        when(userService.unlikeUser(user.getId(), "456")).thenReturn(true);

        Response expectedResponse = Response.success(null);
        Response actualResponse = userController.unlikeUser(token, "456");

        assertEquals(expectedResponse, actualResponse);
        assertEquals(200, actualResponse.getCode());
    }

    @Test
    public void testUnlikeUserTokenVerificationFailed() {
        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setPassword("password");

        String token = JWTUtil.sign(user.getId(), "wrongpassword");

        when(userService.getUserById(user.getId())).thenReturn(user);
        assertEquals(false, JWTUtil.verify(token, user.getId(), user.getPassword()));

        Response expectedResponse = Response.error(1004, "Token verification failed");
        Response actualResponse = userController.unlikeUser(token, "456");

        assertEquals(expectedResponse, actualResponse);
    }
}