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
