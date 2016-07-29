import java.time.LocalDate;

public class Loser
{
    private String name;
    private String email;
    private LocalDate lastLost;
    private int lossFrequency;

    public Loser(String name, String email, LocalDate last, int next)
    {
        this.name = name;
        this.email = email;
        this.lastLost = last;
        this.lossFrequency = next;
    }

    public Loser(String name, String email, int next)
    {
        this.name = name;
        this.email = email;
        this.lastLost = LocalDate.now();
        this.lossFrequency = next;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public LocalDate getLastLost()
    {
        return lastLost;
    }

    public long getLossFrequency()
    {
        return lossFrequency;
    }

    public boolean timeToLose()
    {
        return LocalDate.now().isAfter(lastLost.plusDays(lossFrequency - 1));
    }

    public void setFrequency(int newF)
    {
        lossFrequency = newF;
    }

    public void setLastLoss(LocalDate newL)
    {
        lastLost = newL;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public void setEmail(String newEmail)
    {
        this.email = newEmail;
    }

    public String toString()
    {
        return "{ " + name + " | " + email + " | " + lastLost + " | " + lossFrequency + " } ";
    }

}
