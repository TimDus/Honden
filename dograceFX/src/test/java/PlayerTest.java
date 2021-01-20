import Animals.Animal;
import Animals.Gender;
import Animals.Reservor;
import dograce.models.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

public class AnimalTest
{
    @Test
    void TestConstructor()
    {
        Player player = new Player("Piet", "wachtwoord");

        String name = "Piet";
        String password = "wachtwoord";

        boolean result = false;
        if(animal.getName().equals(name) && animal.getGender().equals(gender) && animal.getPrice().equals(price))
        {
            result = true;
        }

        Assertions.assertTrue(result);
    }
}
