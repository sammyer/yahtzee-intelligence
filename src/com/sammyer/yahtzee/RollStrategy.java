package com.sammyer.yahtzee;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class RollStrategy {
	private BestCategoryScore categoryChooser;
	private ScoreMaximizer firstRoll;
	private ScoreMaximizer secondRoll;

	public RollStrategy(StrategyLookup strategyTable, List<RollCategory> availableCategories) {
		categoryChooser=new BestCategoryScore(strategyTable,availableCategories);
		secondRoll=new ScoreMaximizer(categoryChooser);
		firstRoll=new ScoreMaximizer(secondRoll);
	}

	public List<RollCategory> getAvailableCategories() {
		return categoryChooser.getAvailableCategories();
	}

	public String getCategoryNames() {
		return categoryChooser.getCategoryNames();
	}

	//expected score before first roll
	public float getExpectedScore() {
		return firstRoll.getExpectedScore();
	}
	//expected score after first roll
	public float getExpectedScoreFirstRoll(int dice) {
		return firstRoll.getDiceScore(dice);
	}
	//expected score after first roll
	public float getExpectedScoreSecondRoll(int dice) {
		return secondRoll.getDiceScore(dice);
	}

	//get dice to keep after first roll
	public int getDiceToKeepFirstRoll(int dice) {
		return firstRoll.getDiceToKeep(dice);
	}
	//get dice to keep after second roll
	public int getDiceToKeepSecondRoll(int dice) {
		return secondRoll.getDiceToKeep(dice);
	}
	//get category to choose after third roll
	public RollCategory getBestCategory(int dice) {
		return categoryChooser.getBestCategory(dice);
	}
}
