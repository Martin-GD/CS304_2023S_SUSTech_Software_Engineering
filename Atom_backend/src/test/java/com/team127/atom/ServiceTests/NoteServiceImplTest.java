package com.team127.atom.ServiceTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.team127.atom.mapper.NoteMapper;
import com.team127.atom.model.Note;
import com.team127.atom.service.NoteService;
import com.team127.atom.service.impl.NoteServiceImpl;
import com.team127.atom.model.NoteContent;

import static org.mockito.Mockito.*;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteServiceImplTest {

    @Mock
    private NoteMapper noteMapper;

    @Mock
    private NoteServiceImpl noteService;

    private Note  testNote;

    @BeforeEach
    public void setUp() {
        testNote = new Note();
        testNote.setId("test-id");
        testNote.setName("test-note");
        testNote.setOwner("test-owner");
        testNote.setCreatedAt(0L);
        testNote.setUpdatedAt(0L);
        testNote.setAuth(0);
        testNote.setRepository("test-repo");
        testNote.setDescription("test-description");
        noteMapper.insert(testNote);
    }

    @AfterEach
    public void tearDown() {
        noteMapper.deleteByPrimaryKey(testNote.getId());
    }

    @Test
    public void testRemoveNoteById() {
        when(noteService.removeNoteById(testNote.getId())).thenReturn(true);
    }

    @Test
    public void testGetNoteById() {
        when(noteService.getNoteById(testNote.getId())).thenReturn(testNote);
    }

    @Test
    public void testUpdateNoteInfoById() {
        when(noteService.updateNoteInfoById(testNote)).thenReturn(true);
    }

    @Test
    public void testAddNewNote() {
        when(noteService.addNewNote(testNote)).thenReturn(true);
    }
}
