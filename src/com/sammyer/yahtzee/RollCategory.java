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

	//getDiceScore is score for heuristic calcualtions
	//this return the actual in-game points scored
	//usually it is the same, but can be different
	public int getPointsScored(int dice) {
		return Math.round(getDiceScore(dice));
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
