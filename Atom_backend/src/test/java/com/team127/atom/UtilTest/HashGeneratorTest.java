package com.team127.atom.UtilTest;

import org.junit.jupiter.api.Test;

import com.team127.atom.util.HashGenerator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashGeneratorTest {
    
    @Test
    public void testHashWithEmptyParams() {
        String result = HashGenerator.hash();
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", result);
    }
    
    @Test
    public void testHashWithSingleParam() {
        String result = HashGenerator.hash("hello");
        assertEquals("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", result);
    }
    
    @Test
    public void testHashWithMultipleParams() {
        String result = HashGenerator.hash("hello", "world");
        String result2 = HashGenerator.hash("hello", "world");
        assertEquals(result2, result);
    }
    
    @Test
    public void testHashWithSpecialCharacters() {
        String result = HashGenerator.hash("hello", "world", "!@#$%^&*()");
        String result2 = HashGenerator.hash("hello", "world", "!@#$%^&*()");
        assertEquals(result2, result);
    }
}
