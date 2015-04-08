import com.sammyer.yahtzee.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
	public static void main(String args[]) {
		StrategyLookup strategies=new StrategyLookup();
		strategies.generateStrategies();

		StrategyList lev1=new StrategyList(strategies,22445,1,true);
		StrategyList lev2=new StrategyList(strategies,22445,2,false);
		int dice=DiceRoll.fromFaceValues(22445);

		for (RollStrategy strategy:lev2.strategies) {
			RollStrategy expectedStrategy=lev1.findFirstCategorySubset(strategy);
			if (strategy.getDiceToKeepFirstRoll(dice)!=expectedStrategy.getDiceToKeepFirstRoll(dice)) {
				System.out.println("Mismatch - ");
				lev2.show(strategy);
				lev1.show(expectedStrategy);

				RollCategory chosenCat=expectedStrategy.getAvailableCategories().get(0);
				for (RollCategory otherCat:strategy.getAvailableCategories()) {
					if (otherCat==chosenCat) continue;
					lev1.show(strategies.getStrategy(otherCat));
				}
				System.out.println("------------------------------------------------");
			}
		}
	}

	public static class StrategyComparator implements Comparator<RollStrategy> {
		private int dice;

		public StrategyComparator(int dice) {
			this.dice=dice;
		}

		@Override
		public int compare(RollStrategy o1, RollStrategy o2) {
			return Float.compare(o2.getExpectedScoreFirstRoll(dice),
					o1.getExpectedScoreFirstRoll(dice));
		}
	}

	private static class StrategyList {
		//private StrategyLookup strategyTable;
		public List<RollStrategy> strategies;
		private int dice;
		private int diceFaces;

		public int subsetSize;
		public boolean isSorted;

		private StrategyList(StrategyLookup strategyTable, int diceFaces, int subsetSize, boolean doSort) {
			//this.strategyTable = strategyTable;
			this.diceFaces=diceFaces;
			this.dice = DiceRoll.fromFaceValues(diceFaces);
			this.subsetSize = subsetSize;
			this.isSorted=doSort;

			CombinationIterator<RollCategory> iterator;
			iterator=new CombinationIterator<RollCategory>(strategyTable.getCategories(),subsetSize);
			strategies=new ArrayList<RollStrategy>();
			while (iterator.hasNext()) {
				strategies.add(strategyTable.getStrategy(iterator.next()));
			}

			if (doSort) Collections.sort(strategies,new StrategyComparator(dice));
		}

		public RollStrategy findFirstCategorySubset(RollStrategy target) {
			for (RollStrategy strategy:strategies) {
				if (target.getAvailableCategories().containsAll(strategy.getAvailableCategories())) {
					return strategy;
				}
			}
			return null;
		}

		public void showAll() {
			for (RollStrategy strategy:strategies) {
				show(strategy);
			}
		}

		public void show(RollStrategy strategy) {
			logStrategy(strategy,diceFaces);
		}

		private static void logStrategy(RollStrategy strategy, int diceFaces) {
			int dice=DiceRoll.fromFaceValues(diceFaces);
			int keep1=DiceRoll.toFaceValues(strategy.getDiceToKeepFirstRoll(dice));
			int keep2=DiceRoll.toFaceValues(strategy.getDiceToKeepSecondRoll(dice));

			String s=String.format("%-25s : dice=%-5d keep1=%-5d keep2=%-5d score=%2.1f  cat=%s",
					strategy.getCategoryNames(),diceFaces,keep1,keep2,
					strategy.getExpectedScoreFirstRoll(dice),
					strategy.getBestCategory(dice).getName());
			System.out.println(s);
		}
	}
}
