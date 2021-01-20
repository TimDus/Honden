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

    @Test
    void checkDog()
    {
        String name = "hondje";
        Color color = Color.BLACK;
        boolean result = false;
        if(game.getPlayer().getDog().getDogName().equals(name) && game.getPlayer().getDog().getColor().equals(color))
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }

    @Test
    void moveDog()
    {
        boolean result = false;
        double pos = game.moveDog(0.0);
        if(pos == 10.0)
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }

    @Test
    void moveDog3Times()
    {
        boolean result = false;
        double pos = 0;
        pos = game.moveDog(pos);
        pos = game.moveDog(pos);
        pos = game.moveDog(pos);

        if(pos == 30.0)
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }

    @Test
    void ReadyTrue()
    {
        boolean result = false;
        game.readyPlayer();

        if(game.getPlayer().getReady())
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }

    @Test
    void ReadyFalse()
    {
        boolean result = false;
        game.readyPlayer();

        game.readyPlayer();

        if(!game.getPlayer().getReady())
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }

}
