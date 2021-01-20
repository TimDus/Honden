package dograce.models;

public class Player
{
    private String playerName;
    private String password;
    private int playerNumber;
    private boolean ready;
    private Dog dog;

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }
    public String getPlayerName()
    {
        return playerName;
    }

    public void setPassword(String password){this.password = password;}
    public String getPassword()
    {
        return password;
    }

    public void setPlayerNumber(int playerNumber){this.playerNumber = playerNumber;}
    public int getPlayerNumber(){return playerNumber;}

    public void setDog(Dog dog){this.dog = dog;}
    public Dog getDog(){return dog;}

    public Boolean ReadyStatus()
    {
        ready = !ready;
        return ready;
    }

    public boolean getReady()
    {
        return ready;
    }

    public Player(String Name, String Password)
    {
        playerName = Name;
        password = Password;
        dog = new Dog();
    }

    public Player(){};
}
