package com.sammyer.yahtzee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class StrategyLookup {
	private List<RollCategory> rollCategories;
	private RollStrategy[] strategies;

	public StrategyLookup() {
		rollCategories =new ArrayList<RollCategory>();
		for (int i=1;i<=6;i++) rollCategories.add(new RollTypes.NumberRoll(i,true));
		rollCategories.add(RollTypes.threeOfAKind);
		rollCategories.add(RollTypes.fourOfAKind);
		rollCategories.add(RollTypes.fullHouse);
		rollCategories.add(RollTypes.smallStraight);
		rollCategories.add(RollTypes.largeStraight);
		rollCategories.add(RollTypes.yahtzee);
		rollCategories.add(RollTypes.chance);
	}

	public void generateStrategies() {
		strategies=new RollStrategy[8192];
		RollStrategy strategy;
		List<RollCategory> categorySet;
		int idx;

		for (int level=1;level<=2;level++) {
			CombinationIterator<RollCategory> iterator;
			iterator=new CombinationIterator<RollCategory>(rollCategories,level);
			while (iterator.hasNext()) {
				categorySet=iterator.next();
				idx=getIdx(categorySet);
				strategy=new RollStrategy(this,categorySet);
				strategies[idx]=strategy;
			}
		}
	}

	private int getIdx(List<RollCategory> categories) {
		int idx=0;
		for (RollCategory category:categories) idx+=getIdx(category);
		return idx;
	}
	private int getIdx(RollCategory category) {
		return 1<<rollCategories.indexOf(category);
	}

	public float getExpectedScore(List<RollCategory> categories, RollCategory categoryToRemove) {
		int idx=getIdx(categories)-getIdx(categoryToRemove);
		return strategies[idx].getExpectedScore();
	}

	public List<RollCategory> getCategories() {
		return rollCategories;
	}

	public RollStrategy getStrategy(List<RollCategory> categories) {
		return strategies[getIdx(categories)];
	}
	public RollStrategy getStrategy(RollCategory category) {
		return strategies[getIdx(category)];
	}
	public RollStrategy getStrategy(RollCategory... categories) {
		return getStrategy(Arrays.asList(categories));
	}
}
