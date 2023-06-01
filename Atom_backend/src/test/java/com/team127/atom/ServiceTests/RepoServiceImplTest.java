package com.team127.atom.ServiceTests;

import com.team127.atom.service.impl.RepoServiceImpl;
import com.team127.atom.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.team127.atom.mapper.CollectionMapper;
import com.team127.atom.model.Repository;
import com.team127.atom.model.User;
import com.team127.atom.service.RepoService;
import com.team127.atom.service.UserService;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepoServiceImplTest {

    @Mock
    private RepoServiceImpl repoService;

    @Mock
    private CollectionMapper collectionMapper;

    @Mock
    private UserServiceImpl userService;

    private Repository testRepo;

    @BeforeEach
    public void setUp() {
        testRepo = new Repository();
        testRepo.setId("test-id");
        testRepo.setName("test-repo");
        testRepo.setOwnerId("test-owner");
        testRepo.setAuth(0);
        collectionMapper.insert(testRepo);
    }

    @AfterEach
    public void tearDown() {
        collectionMapper.deleteByPrimaryKey(testRepo.getId());
    }

    @Test
     public void testGetRepoById() {
        when(repoService.getRepoById(testRepo.getId())).thenReturn(testRepo);
     }


    @Test
    public void testGetRepoByName() {
        List<Repository> repos = new ArrayList<>();
        repos.add(testRepo);
        when(repoService.getRepoByName(testRepo.getName())).thenReturn(repos);
    }

    @Test
    public void testGetReposByStars() {
        List<Repository> repos = new ArrayList<>();
        repos.add(testRepo);
        when(repoService.getReposByStars(1)).thenReturn(repos);
    }

    @Test
    public void testGetReposByOwner() {
        List<Repository> repos = new ArrayList<>();
        repos.add(testRepo);
        when(repoService.getReposByOwner(testRepo.getOwnerId())).thenReturn(repos);
    }

    @Test
    public void testGetUserStarRepos() {
        List<Repository> repos = new ArrayList<>();
        repos.add(testRepo);
        when(repoService.getUserStarRepos(testRepo.getOwnerId())).thenReturn(repos);
    }

    @Test
    public void testRemoveRepoById() {
        when(repoService.removeRepoById(testRepo.getId())).thenReturn(true);
    }

    @Test
    public void testUpdateRepoInfoById() {
        testRepo.setName("new-name");
        when(repoService.updateRepoInfoById(testRepo)).thenReturn(true);
    }

    @Test
    public void testAddNewRepo() {
        collectionMapper.deleteByPrimaryKey(testRepo.getId());
        when(repoService.addNewRepo(testRepo)).thenReturn(true);
    }

    @Test
    public void testGetStarNum() {
        when(repoService.getStarNum(testRepo)).thenReturn(0);
    }

    @Test
    public void testStarRepo() {
        User user = new User();
        user.setId("test-user");
        user.setName("test-user");
        userService.insertUser(user);
        when(repoService.starRepo(testRepo, user)).thenReturn(true);
        when(repoService.getStarNum(testRepo)).thenReturn(1);
        userService.deleteUserById(user.getId());

    }

    @Test
    public void testUnStarRepo() {
        User user = new User();
        user.setId("test-user");
        user.setName("test-user");
        userService.insertUser(user);
        when(repoService.starRepo(testRepo, user)).thenReturn(true);
        when(repoService.getStarNum(testRepo)).thenReturn(1);
        when(repoService.unStarRepo(testRepo, user)).thenReturn(true);
        when(repoService.getStarNum(testRepo)).thenReturn(0);
        userService.deleteUserById(user.getId());
    }
}
