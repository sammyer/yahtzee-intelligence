package com.sammyer.yahtzee;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RollCategory implements ScoreHeuristic {
	private String name;

	protected RollCategory(String name) {
		this.name = name;
	}

	//get points scored
	public int getPointsScored(DiceRoll dice) {
		return matches(dice)?getPointsScoredIfMatches(dice):0;
	}

	//matches category
	public boolean matches(DiceRoll dice) {
		return true;
	}

	//for yahtzees - get points scored when matches
	//this way you can score e.g. large straight on a yahtzee
	abstract public int getPointsScoredIfMatches(DiceRoll dice);

	//getDiceScore is score for heuristic calcualtions
	//this return the actual in-game points scored
	//usually it is the same, but can be different
	public float getDiceScore(DiceRoll dice) {
		return getPointsScored(dice);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public static String getCategoryNames(List<RollCategory> availableCategories) {
		StringBuilder s=new StringBuilder();
		boolean first=true;
		for (RollCategory category:availableCategories) {
			if (!first) s.append(", ");
			first=false;
			s.append(category.getName());
		}
		return s.toString();
	}
}
