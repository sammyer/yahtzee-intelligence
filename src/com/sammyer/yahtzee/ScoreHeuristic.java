package com.sammyer.yahtzee;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 06/04/15
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ScoreHeuristic {
	public float getDiceScore(DiceRoll dice);
}
