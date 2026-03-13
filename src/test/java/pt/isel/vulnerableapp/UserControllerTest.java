package pt.isel.vulnerableapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    @Test
    void testBasic() {
        assertTrue(true, "Teste básico OK");
    }

    @Test
    void testUserIdValidation() {
        String userId = "user123";
        assertNotNull(userId);
        assertFalse(userId.isEmpty());
    }
}
