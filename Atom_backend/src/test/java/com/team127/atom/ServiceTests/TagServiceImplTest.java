package com.team127.atom.ServiceTests;

import com.team127.atom.model.Note;
import com.team127.atom.model.Repository;
import com.team127.atom.model.User;
import com.team127.atom.service.NoteService;
import com.team127.atom.service.RepoService;
import com.team127.atom.service.UserService;
import com.team127.atom.service.impl.NoteServiceImpl;
import com.team127.atom.service.impl.RepoServiceImpl;
import com.team127.atom.service.impl.TagServiceImpl;
import com.team127.atom.service.impl.UserServiceImpl;
import java.sql.Date;
import java.util.ArrayList;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;

import com.team127.atom.model.Tag;
import com.team127.atom.service.TagService;

import java.util.List;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TagServiceImplTest {

    @Mock
    private TagServiceImpl tagService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private RepoServiceImpl repoService;

    @Mock
    private NoteServiceImpl noteService;

    private Tag tag;
    private User user;
    private Repository repo;
    private Note note;

    @BeforeEach
    public void setUp() {
        tag = new Tag("testTagId", "testTagName", "testTagDescription", "testTagColor", "testuser.getId()");
        user = new User("testuser.getId()", "testUserName", "testUserBio", "testUserLocation", "testUserCompany", "testUserCreatedAt", "testUserUpdatedAt", "testUserEmail", "testUserPassword", 1, false, new Date(0), 0);
        repo = new Repository("testrepo.getId()", "testuser.getId()", "testRepoName", 1);
        note = new Note("testnote.getId()", "testuser.getId()", "testrepo.getId()", "testNoteName", 1, 0L, 0L, "testNoteDescription");
        userService.insertUser(user);
        repoService.addNewRepo(repo);
        noteService.addNewNote(note);
    }

    @AfterEach
    public void tearDown() {
        tagService.removeTag(tag.getId());
        userService.deleteUserById(user.getId());
        repoService.removeRepoById(repo.getId());
        noteService.removeNoteById(note.getId());
    }

    @Test
    public void testCreateTag() {
        when(tagService.createTag(tag, user.getId())).thenReturn(true);
    }

    @Test
    public void testRemoveTag() {
        tagService.createTag(tag, user.getId());
        when(tagService.removeTag(tag.getId())).thenReturn(true);
    }

    @Test
    public void testGetAllTags() {
        tagService.createTag(tag, user.getId());
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(tagService.getAllTags(user.getId())).thenReturn(tags);
    }

    @Test
    public void testGetRepoTags() {
        tagService.createTag(tag, user.getId());
        tagService.addRepoTag(repo.getId(), tag.getId());
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(tagService.getRepoTags(repo.getId())).thenReturn(tags);
    }

    @Test
    public void testGetNoteTags() {
        tagService.createTag(tag, user.getId());
        tagService.addNoteTag(note.getId(), tag.getId());
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        when(tagService.getNoteTags(note.getId())).thenReturn(tags);
    }

    @Test
    public void testAddRepoTag() {
        tagService.createTag(tag, user.getId());
        when(tagService.addRepoTag(repo.getId(), tag.getId())).thenReturn(true);
    }

    @Test
    public void testRemoveRepoTag() {
        tagService.createTag(tag, user.getId());
        tagService.addRepoTag(repo.getId(), tag.getId());
        when(tagService.removeRepoTag(repo.getId(), tag.getId())).thenReturn(true);
    }

    @Test
    public void testAddNoteTag() {
        tagService.createTag(tag, user.getId());
        when(tagService.addNoteTag(note.getId(), tag.getId())).thenReturn(true);
    }

    @Test
    public void testRemoveNoteTag() {
        tagService.createTag(tag, user.getId());
        tagService.addNoteTag(note.getId(), tag.getId());
        when(tagService.removeNoteTag(note.getId(), tag.getId())).thenReturn(true);
    }

    @Test
    public void testGetTag() {
        tagService.createTag(tag, user.getId());
        when(tagService.getTag(tag.getId())).thenReturn(tag);
    }
}