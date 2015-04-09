package com.sammyer.yahtzee;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class StrategyCalculator {

	public StrategyDatabase generateStrategies() {
		return generateStrategies(RollTypes.getRollCategories().size());
	}

	public StrategyDatabase generateStrategies(int maxLevel) {
		StrategyDatabase database=new StrategyDatabase();
		RollStrategy strategy;
		List<RollCategory> categorySet;
		int idx;

		for (int level=1;level<=maxLevel;level++) {
			CombinationIterator<RollCategory> iterator;
			iterator=new CombinationIterator<RollCategory>(RollTypes.getRollCategories(),level);
			while (iterator.hasNext()) {
				categorySet=iterator.next();
				strategy=new RollStrategy(database,categorySet);
				database.add(strategy);
			}
		}

		return database;
	}
}
