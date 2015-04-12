package com.sammyer.yahtzee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 11/04/15
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameStrategy {
	private ExpectedScoreDatabase database;
	private List<RollCategory> categoriesLeft;
	private RollStrategy curStrategy;
	private int totalScore;
	private int numberRollScore;
	private boolean hasYahtzee;

	public GameStrategy(ExpectedScoreDatabase database) {
		this.database=database;
		categoriesLeft=new ArrayList<RollCategory>(RollTypes.getRollCategories());

		totalScore=0;
		numberRollScore=0;
		hasYahtzee=false;
		curStrategy=new RollStrategy(database,categoriesLeft);
	}

	public int getDiceToKeepFirstRoll(int dice) {
		return curStrategy.getDiceToKeepFirstRoll(dice);
	}
	public int getDiceToKeepSecondRoll(int dice) {
		return curStrategy.getDiceToKeepSecondRoll(dice);
	}
	public RollCategory getSuggestedCategory(int dice) {
		return curStrategy.getBestCategory(dice);
	}
	public void selectCategory(RollCategory category, int dice) {
		if (category==RollTypes.yahtzee&&category.matches(dice)) hasYahtzee=true;
		if (isNumberRoll(category)) numberRollScore+=category.getPointsScored(dice);
		totalScore+=getPointsScored(category, dice);
		categoriesLeft.remove(category);
		curStrategy=new RollStrategy(database,categoriesLeft);
	}

	public List<RollCategory> getCategoriesLeft() {
		return categoriesLeft;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getPointsScored(RollCategory category, int dice) {
		int diceScore;
		int score;
		if (hasYahtzee&&RollTypes.yahtzee.getDiceScore(dice)>0) {
			diceScore=category.getPointsScoredIfMatches(dice);
			score=diceScore+100;
		} else {
			diceScore=category.getPointsScored(dice);
			score=diceScore;
		}

		if (isLastNumberCategory(category)&&numberRollScore+diceScore>=63) score+=35;

		return score;
	}
	private boolean isLastNumberCategory(RollCategory category) {
		if (!isNumberRoll(category)) return false;
		for (RollCategory cat:categoriesLeft) {
			if (isNumberRoll(cat)&&cat!=category) return false;
		}
		return true;
	}
	private boolean isNumberRoll(RollCategory category) {
		return category instanceof RollTypes.NumberRoll;
	}


	public float getExpectedScoreRestOfGame(RollCategory category) {
		return database.getExpectedScore(categoriesLeft,category);
	}

	public float getExpectedFinalScore(RollCategory category, int dice) {
		//return totalScore+getPointsScored(category, dice)+getExpectedScoreRestOfGame(category);
		return totalScore+category.getDiceScore(dice)+getExpectedScoreRestOfGame(category);
	}

	public int getNumberRollScore() {
		return numberRollScore;
	}

	public boolean getHasYahtzee() {
		return hasYahtzee;
	}
}
