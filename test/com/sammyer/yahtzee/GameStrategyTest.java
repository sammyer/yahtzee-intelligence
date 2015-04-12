package com.sammyer.yahtzee;

import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 12/04/15
 * Time: 12:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameStrategyTest extends TestCase {
	private ExpectedScoreDatabase database;

	public void setUp() throws Exception {
		database=new ExpectedScoreDatabase();
		try {
			database.load(GameConstants.DB_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetPointsScored() throws Exception {
		GameStrategy strategy=new GameStrategy(database);

		//test 35 point bonus
		assertEquals(strategy.getPointsScored(RollTypes.getNumberCategory(6),0x320000),18);
		strategy.selectCategory(RollTypes.getNumberCategory(1),0x200003);
		strategy.selectCategory(RollTypes.getNumberCategory(2),0x200030);
		strategy.selectCategory(RollTypes.getNumberCategory(3),0x200300);
		strategy.selectCategory(RollTypes.getNumberCategory(4),0x203000);
		strategy.selectCategory(RollTypes.getNumberCategory(5),0x230000);
		assertEquals(strategy.getPointsScored(RollTypes.getNumberCategory(6),0x230000),12);
		assertEquals(strategy.getPointsScored(RollTypes.getNumberCategory(6),0x320000),53);

		//test yahtzee bonus
		strategy.selectCategory(RollTypes.yahtzee,5);
		assertEquals(strategy.getPointsScored(RollTypes.getNumberCategory(6),0x500000),165);

		strategy=new GameStrategy(database);
		assertEquals(strategy.getPointsScored(RollTypes.largeStraight,5),0);
		assertEquals(strategy.getPointsScored(RollTypes.yahtzee,5),50);
		strategy.selectCategory(RollTypes.yahtzee,5);
		assertEquals(strategy.getPointsScored(RollTypes.largeStraight,5),140);

		strategy=new GameStrategy(database);
		assertEquals(strategy.getPointsScored(RollTypes.largeStraight,5),0);
		assertEquals(strategy.getPointsScored(RollTypes.yahtzee,0x011111),0);
		strategy.selectCategory(RollTypes.yahtzee,0x011111);
		assertEquals(strategy.getPointsScored(RollTypes.largeStraight,5),0);
	}
}
