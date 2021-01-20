package Models;

import dograce.models.Dog;
import dograce.models.Player;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DogTest
{
    @Test
    void TestConstructor()
    {
        Dog dog = new Dog("Pietje", Color.BLACK);

        String name = "Pietje";
        Color color = Color.BLACK;

        boolean result = false;
        if(dog.getDogName().equals(name) && dog.getColor().equals(color))
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }
}
