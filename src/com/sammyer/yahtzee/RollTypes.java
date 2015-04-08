package com.sammyer.yahtzee;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 06/04/15
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class RollTypes {
	public static RollCategory largeStraight=new RollCategory("Lg Straight") {
		@Override
		public float getDiceScore(int dice) {
			if ((dice&0x0FFFF0)==0x011110) return 40;
			else return 0;
		}
	};

	public static RollCategory smallStraight=new RollCategory("Sm Straight") {
		@Override
		public float getDiceScore(int dice) {
			int consecutive=0;
			DiceRoll.CountIterator counts=new DiceRoll.CountIterator(dice);
			for (int i=0;i<6;i++) {
				if (counts.next()==0) consecutive=0;
				else consecutive++;
				if (consecutive==4) return 30;
			}
			return 0;
		}
	};

	public static RollCategory yahtzee=new RollCategory("Yahtzee") {
		@Override
		public float getDiceScore(int dice) {
			DiceRoll.CountIterator counts=new DiceRoll.CountIterator(dice);
			while (counts.hasNext()) {
				if (counts.next()==5) return 50;
			}
			return 0;
		}
	};

	public static RollCategory fullHouse=new RollCategory("Full House") {
		@Override
		public float getDiceScore(int dice) {
			DiceRoll.CountIterator counts=new DiceRoll.CountIterator(dice);
			boolean hasPair=false;
			boolean hasTriple=false;
			while (counts.hasNext()) {
				int c=counts.next();
				if (c<2) continue;
				if (c==3) hasTriple=true;
				else if (c==2) hasPair=true;
				if (c==5||(hasPair&&hasTriple)) return 25;
			}
			return 0;
		}
	};

	private static class NOfAKind extends RollCategory {
		int matchingCount;

		private NOfAKind(int matchingCount) {
			super(matchingCount+"-Kind");
			this.matchingCount = matchingCount;
		}

		@Override
		public float getDiceScore(int dice) {
			boolean isMatch=false;
			int sum=0;
			for (int n=1;n<=6;n++) {
				int count=DiceRoll.getCount(dice,n);
				if (count>=matchingCount) isMatch=true;
				sum+=n*count;
			}
			if (isMatch) return sum;
			else return 0;
		}
	}

	public static RollCategory threeOfAKind=new NOfAKind(3);
	public static RollCategory fourOfAKind=new NOfAKind(4);

	public static RollCategory chance=new RollCategory("Chance") {
		@Override
		public float getDiceScore(int dice) {
			return DiceRoll.getDiceSum(dice);
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
		private boolean useBonus;

		public NumberRoll(int number, boolean useBonus) {
			super(number+"s");
			this.number = number;
			this.useBonus=useBonus;
		}

		@Override
		public float getDiceScore(int dice) {
			int count=DiceRoll.getCount(dice,number);
			if (useBonus) return count*number+BONUS[number-1][count];
			return count*number;
		}
	}

}
