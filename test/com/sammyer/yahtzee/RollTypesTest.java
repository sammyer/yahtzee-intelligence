package com.sammyer.yahtzee;

import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class RollTypesTest extends TestCase {
	private void check(ScoreHeuristic type, int numbers, float score) throws Exception {
		assertEquals(type.getDiceScore(DiceRoll.fromFaceValues(numbers)),score);
	}
	public void testLargeStraight() throws Exception {
		check(RollTypes.largeStraight,23456,40);
		check(RollTypes.largeStraight,23451,40);
		check(RollTypes.largeStraight, 23416, 0);
	}
	public void testSmallStraight() throws Exception {
		check(RollTypes.smallStraight,23456,30);
		check(RollTypes.smallStraight,12344,30);
		check(RollTypes.smallStraight, 12456, 0);
	}
	public void testYahtzee() throws Exception {
		check(RollTypes.yahtzee,23456,0);
		check(RollTypes.yahtzee,66665,0);
		check(RollTypes.yahtzee, 44444, 50);
	}
	public void testFullHouse() throws Exception {
		check(RollTypes.fullHouse,61616,25);
		check(RollTypes.fullHouse,22334,0);
		check(RollTypes.fullHouse,22333,25);
		check(RollTypes.fullHouse,23333,0);
		check(RollTypes.fullHouse, 33333, 25);
	}
	public void testThreeOfAKind() throws Exception {
		check(RollTypes.threeOfAKind,22334,0);
		check(RollTypes.threeOfAKind,61616,20);
		check(RollTypes.threeOfAKind,33332,14);
		check(RollTypes.threeOfAKind, 33333, 15);
	}
	public void testFourOfAKind() throws Exception {
		check(RollTypes.fourOfAKind,22334,0);
		check(RollTypes.fourOfAKind,61616,0);
		check(RollTypes.fourOfAKind,33332,14);
		check(RollTypes.fourOfAKind, 33333, 15);
	}
	public void testChance() throws Exception {
		check(RollTypes.chance,65456,26);
		check(RollTypes.chance, 11111, 5);
	}
	public void testNumbers() throws Exception {
		ScoreHeuristic roll;
		roll=new RollTypes.NumberRoll(5,false);
		check(roll,65456,10);
		check(roll, 11111, 0);
		roll=new RollTypes.NumberRoll(1,false);
		check(roll,65456,0);
		check(roll, 11111, 5);
		roll=new RollTypes.NumberRoll(6,true);
		check(roll,65456,6);
		check(roll, 11111, -13);
		roll=new RollTypes.NumberRoll(1,true);
		check(roll,65456,1);
		check(roll, 11111, 14);
	}

}

/*

package com.sammyer.yahtzee;

public class RollTypes {


	public static class NumberRoll implements ScoreHeuristic {
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


*/