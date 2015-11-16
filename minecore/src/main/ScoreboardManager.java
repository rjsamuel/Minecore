package main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {
	
	private Scoreboard scoreboard;
	private Objective obj;
	
	public void newScoreboard(String title)
	{
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = scoreboard.registerNewObjective(title, "Dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public void addScore(String label, int score)
	{
		@SuppressWarnings("deprecation")
		Score newScore = obj.getScore(Bukkit.getOfflinePlayer(label + ": "));
		newScore.setScore(score);
	}
	
	public void setScoreboard(Player player)
	{
		player.setScoreboard(scoreboard);
	}
	
	public void test()
	{
		
	}
}