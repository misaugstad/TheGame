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

import java.util.*;

public class LoserList
{
	private ArrayList<Loser> losers;

	public LoserList()
	{
		losers = new ArrayList<Loser>();
	}

	public void addLoser(Loser l)
	{
		losers.add(l);
	}

	public void removeLoser(String name)
	{
		Loser r = new Loser("", "", -1);
		for (Loser l : losers)
		{
			if (l.getName().equals(name))
			{
				r = l;
			}
		}
		losers.remove(r);
	}

	public Loser getLoser(int i)
	{
		return losers.get(i);
	}

	public int whereIsLoser(String name)
	{
		for (int i = 0; i < losers.size(); i++)
		{
			if (losers.get(i).getName().equals(name))
			{
				return i;
			}
		}
		return -1;
	}

	public int howMany()
	{
		return losers.size();
	}

	public boolean anyToNotify()
	{
		for (Loser l : losers)
		{
			if (l.timeToLose())
				return true;
		}
		return false;
	}

	public ArrayList<Loser> whoToNotify()
	{
		ArrayList<Loser> almostWinners = new ArrayList<Loser>();
		for (Loser l : losers)
		{
			if (l.timeToLose())
			{
				almostWinners.add(l);
			}
		}
		return almostWinners;
	}
}
