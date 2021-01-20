package dograce;

import dograce.models.Dog;
import javafx.scene.paint.Color;

public interface IDogRaceGame
{
    public void notifyWhenReady(int playerNr);

    public void registerPlayer(String name, String password);

    public void startGame();

    public void endGame();

    public void readyPlayer();

    public void saveDog(String name, Color color);

    public double moveDog(double currentPos);
}
