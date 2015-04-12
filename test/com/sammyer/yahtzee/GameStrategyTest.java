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
	private GameStrategy strategy;

	public void setUp() throws Exception {
		database=new ExpectedScoreDatabase();
		try {
			database.load(GameConstants.DB_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetPointsScored() throws Exception {
		strategy=new GameStrategy(database);

		//test 35 point bonus
		check(6, 66655, 18);
		select(1, 66111);
		select(2, 66222);
		select(3, 66333);
		select(4, 66444);
		select(5, 66555);
		check(6, 66555, 12);
		check(6, 66655, 53);

		//test yahtzee bonus
		select(RollTypes.yahtzee, 11111);
		check(6, 66666, 165);

		strategy=new GameStrategy(database);
		check(RollTypes.largeStraight, 11111, 0);
		check(RollTypes.yahtzee, 11111, 50);
		select(RollTypes.yahtzee,11111);
		check(RollTypes.largeStraight, 11111, 140);

		strategy=new GameStrategy(database);
		check(RollTypes.largeStraight, 11111, 0);
		check(RollTypes.yahtzee, 12345, 0);
		select(RollTypes.yahtzee,12345);
		check(RollTypes.largeStraight, 11111, 0);
	}

	private void check(RollCategory category, int diceFaces, int expectedPoints) {
		assertEquals(strategy.getPointsScored(category,DiceRoll.fromFaceValues(diceFaces)),expectedPoints);
	}
	private void check(int numberRoll, int diceFaces, int expectedPoints) {
		check(RollTypes.getNumberCategory(numberRoll), diceFaces, expectedPoints);
	}
	private void select(RollCategory category, int diceFaces) {
		strategy.selectCategory(category, DiceRoll.fromFaceValues(diceFaces));
	}
	private void select(int numberRoll, int diceFaces) {
		select(RollTypes.getNumberCategory(numberRoll),diceFaces);
	}
}
