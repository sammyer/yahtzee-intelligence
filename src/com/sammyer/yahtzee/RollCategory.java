package com.sammyer.yahtzee;

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

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
