package com.sammyer.yahtzee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 06/04/15
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class RollTypes {
	private static List<RollCategory> rollCategories;

	public static List<RollCategory> getRollCategories() {
		if (rollCategories==null) {
			rollCategories =new ArrayList<RollCategory>();
			for (int i=1;i<=6;i++) rollCategories.add(new RollTypes.NumberRoll(i));
			rollCategories.add(RollTypes.threeOfAKind);
			rollCategories.add(RollTypes.fourOfAKind);
			rollCategories.add(RollTypes.fullHouse);
			rollCategories.add(RollTypes.smallStraight);
			rollCategories.add(RollTypes.largeStraight);
			rollCategories.add(RollTypes.yahtzee);
			rollCategories.add(RollTypes.chance);
		}
		return rollCategories;
	}

	public static RollCategory getNumberCategory(int n) {
		return getRollCategories().get(n-1);
	}

	public static RollCategory parseCategory(String categoryString) {
		if (categoryString==null||categoryString.isEmpty()) return null;
		String s=categoryString.toLowerCase();
		int n=0;
		if (s.contains("1")||s.contains("one")) n=1;
		else if (s.contains("2")||s.contains("two")) n=2;
		else if (s.contains("3")||s.contains("three")) n=3;
		else if (s.contains("4")||s.contains("four")) n=4;
		else if (s.contains("5")||s.contains("five")) n=5;
		else if (s.contains("6")||s.contains("six")) n=6;

		RollCategory cat;
		if (s.contains("c")) cat=chance;
		else if (s.contains("y")) cat=yahtzee;
		else if (s.startsWith("f")&&n==0) cat=fullHouse;
		else if (n==3&&(s.contains("k")||s.contains("x"))) cat=threeOfAKind;
		else if (n==4&&(s.contains("k")||s.contains("x"))) cat=fourOfAKind;
		else if (n>0) cat=getNumberCategory(n);
		else if (s.contains("m")) cat=smallStraight;
		else if (s.contains("l")) cat=largeStraight;
		else if (s.startsWith("s")) cat=smallStraight;
		else cat=null;

		return cat;
	}

	public static RollCategory largeStraight=new RollCategory("Lg Straight") {
		@Override
		public boolean matches(DiceRoll dice) {
			int diceCounts=dice.getDiceCounts();
			return ((diceCounts&0x0FFFF0)==0x011110)&&((diceCounts&0xF0000F)>0);
		}

		@Override
		public int getPointsScoredIfMatches(DiceRoll dice) {
			return 40;
		}
	};

	public static RollCategory smallStraight=new RollCategory("Sm Straight") {
		@Override
		public boolean matches(DiceRoll dice) {
			int consecutive=0;
			for (int i=1;i<=6;i++) {
				if (dice.getCount(i)==0) consecutive=0;
				else consecutive++;
				if (consecutive==4) return true;
			}
			return false;
		}

		@Override
		public int getPointsScoredIfMatches(DiceRoll dice) {
			return 30;
		}
	};

	public static RollCategory yahtzee=new RollCategory("Yahtzee") {
		@Override
		public boolean matches(DiceRoll dice) {
			for (int i=1;i<=6;i++) {
				if (dice.getCount(i)==5) return true;
			}
			return false;
		}

		@Override
		public int getPointsScoredIfMatches(DiceRoll dice) {
			return 50;
		}
	};

	public static RollCategory fullHouse=new RollCategory("Full House") {
		@Override
		public boolean matches(DiceRoll dice) {
			boolean hasPair=false;
			boolean hasTriple=false;
			for (int i=1;i<=6;i++) {
				int c=dice.getCount(i);
				if (c<2) continue;
				if (c==3) hasTriple=true;
				else if (c==2) hasPair=true;
				if (c==5||(hasPair&&hasTriple)) return true;
			}
			return false;
		}

		@Override
		public int getPointsScoredIfMatches(DiceRoll dice) {
			return 25;
		}
	};

	private static class NOfAKind extends RollCategory {
		int matchingCount;

		private NOfAKind(int matchingCount) {
			super(matchingCount+"-Kind");
			this.matchingCount = matchingCount;
		}

		@Override
		public boolean matches(DiceRoll dice) {
			for (int n=1;n<=6;n++) {
				int count=dice.getCount(n);
				if (count>=matchingCount) return true;
			}
			return false;
		}

		@Override
		public int getPointsScoredIfMatches(DiceRoll dice) {
			return dice.getSum();
		}
	}

	public static RollCategory threeOfAKind=new NOfAKind(3);
	public static RollCategory fourOfAKind=new NOfAKind(4);

	public static RollCategory chance=new RollCategory("Chance") {
		@Override
		public int getPointsScoredIfMatches(DiceRoll dice) {
			return dice.getSum();
		}
	};

	public static class NumberRoll extends RollCategory {
		private static final int[][] BONUS={
				{1, 3, 4, 6, 7, 9},
				{-4, -1, 2, 6, 9, 12},
				{-7, -4, 1, 6, 11, 15},
				{-10, -7, -1, 6, 13, 18},
				{-12, -9, -3, 6, 15, 20},
				{-13, -11, -6, 6, 17, 21}};
		private int number;

		public NumberRoll(int number) {
			super(number+"s");
			this.number = number;
		}

		@Override
		public float getDiceScore(DiceRoll dice) {
			int count=dice.getCount(number);
			return count*number+BONUS[number-1][count];
		}

		@Override
		public int getPointsScoredIfMatches(DiceRoll dice) {
			return dice.getCount(number)*number;
		}
	}

}
