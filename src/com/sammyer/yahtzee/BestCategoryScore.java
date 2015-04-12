package com.sammyer.yahtzee;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BestCategoryScore implements ScoreHeuristic {
	protected List<RollCategory> availableCategories;
	protected ExpectedScoreDatabase expectedScoreDatabase;
	protected int numCategories;

	public BestCategoryScore(ExpectedScoreDatabase expectedScoreDatabase, List<RollCategory> availableCategories) {
		this.availableCategories = availableCategories;
		this.expectedScoreDatabase = expectedScoreDatabase;
		this.numCategories =availableCategories.size();
	}

	@Override
	public float getDiceScore(DiceRoll dice) {
		if (numCategories==0) return 0;
		else return getBestCategoryScore(dice).score;
	}

	public RollCategory getBestCategory(DiceRoll dice) {
		return getBestCategoryScore(dice).category;
	}

	public List<RollCategory> getAvailableCategories() {
		return availableCategories;
	}

	public String getCategoryNames() {
		return RollCategory.getCategoryNames(availableCategories);
	}

	private CategoryScore getBestCategoryScore(DiceRoll dice) {
		if (numCategories==1) {
			RollCategory category=availableCategories.get(0);
			return new CategoryScore(category,category.getDiceScore(dice));
		}
		RollCategory bestCategory=null;
		float maxScore=0;
		for (RollCategory category: availableCategories) {
			float score=getCategoryScore(category,dice);
			if (bestCategory==null||score>maxScore) {
				bestCategory=category;
				maxScore=score;
			}
		}
		return new CategoryScore(bestCategory,maxScore);
	}

	protected float getCategoryScore(RollCategory category, DiceRoll dice) {
		return category.getDiceScore(dice)+ expectedScoreDatabase.getExpectedScore(availableCategories,category);
	}

	protected static class CategoryScore {
		public RollCategory category;
		public float score;

		private CategoryScore(RollCategory category, float score) {
			this.category = category;
			this.score = score;
		}
	}
}
