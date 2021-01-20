import dograce.models.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

public class PlayerTest
{
    @Test
    void TestConstructor()
    {
        Player player = new Player("Piet", "wachtwoord");

        String name = "Piet";
        String password = "wachtwoord";

        boolean result = false;
        if(player.getPlayerName().equals(name) && player.getPassword().equals(password))
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }
}
