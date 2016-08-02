/*
Copyright (C) 2016  Michael Saugstad

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
