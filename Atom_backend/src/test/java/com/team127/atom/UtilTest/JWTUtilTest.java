package com.team127.atom.UtilTest;

import org.junit.jupiter.api.Test;
import com.team127.atom.util.JWTUtil;
import static org.junit.jupiter.api.Assertions.*;

public class JWTUtilTest {

    private static final String SECRET = "mySecret";
    private static final String ID = "12345";
    private static final String TOKEN = JWTUtil.sign(ID, SECRET);

    @Test
    public void testVerifyWithValidToken() {
        assertTrue(JWTUtil.verify(TOKEN, ID, SECRET));
    }

    @Test
    public void testVerifyWithInvalidToken() {
        assertFalse(JWTUtil.verify("invalidToken", ID, SECRET));
    }

    @Test
    public void testGetUserIdWithValidToken() {
        assertEquals(ID, JWTUtil.getUserId(TOKEN));
    }

    @Test
    public void testGetUserIdWithInvalidToken() {
        assertNull(JWTUtil.getUserId("invalidToken"));
    }

    @Test
    public void testSign() {
        String token = JWTUtil.sign(ID, SECRET);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }
}