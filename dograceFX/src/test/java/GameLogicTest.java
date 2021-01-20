import dograce.DogRaceGame;
import dograce.models.Dog;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameLogicTest
{
    DogRaceGame game;
    @BeforeEach
    void setUp()
    {
        game = new DogRaceGame();
        game.registerPlayer("pietje","wachtwoord");
        game.saveDog("hondje", Color.BLACK);
    }

    @Test
    void checkPlayer()
    {
        String name = "pietje";
        String password = "wachtwoord";
        boolean result = false;
        if(game.getPlayer().getPlayerName().equals(name) && game.getPlayer().getPassword().equals(password))
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }
}
