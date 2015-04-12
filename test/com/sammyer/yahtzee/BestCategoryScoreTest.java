package com.sammyer.yahtzee;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 09/04/15
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class BestCategoryScoreTest extends TestCase {
	ExpectedScoreDatabase database;
	List<RollCategory> rollCategories;
	RollCategory cat1;
	RollCategory cat2;

	public void setUp() throws Exception {
		rollCategories=RollTypes.getRollCategories();
		cat1=RollTypes.smallStraight;
		cat2=RollTypes.largeStraight;
	}

	public void tearDown() throws Exception {

	}

	public void testGetDiceScoreEstimated() throws Exception {
		database=new MockExpectedScoreDatabase(cat1,cat2,10,10);
		assertEquals(getScore(23456),50f);
		database=new MockExpectedScoreDatabase(cat1,cat2,50,10);
		assertEquals(getScore(23456),90f);
		database=new MockExpectedScoreDatabase(cat1,cat2,10,50);
		assertEquals(getScore(23456),80f);

		database=new MockExpectedScoreDatabase(cat1,cat2,10,10);
		assertEquals(getScore(52345),40f);
		database=new MockExpectedScoreDatabase(cat1,cat2,60,10);
		assertEquals(getScore(52345),60f);
	}

	private float getScore(int dice) {
		List<RollCategory> cats=new ArrayList<RollCategory>();
		cats.add(cat1);
		cats.add(cat2);
		BestCategoryScore chooser=new BestCategoryScore(database,cats);
		return chooser.getDiceScore(DiceRoll.fromFaceValues(dice));
	}

	public void testGetBestCategory() throws Exception {
		database=new MockExpectedScoreDatabase(cat1,cat2,10,10);
		assertEquals(getCategory(23456),cat2);
		database=new MockExpectedScoreDatabase(cat1,cat2,50,10);
		assertEquals(getCategory(23456),cat2);
		database=new MockExpectedScoreDatabase(cat1,cat2,10,50);
		assertEquals(getCategory(23456),cat1);

		database=new MockExpectedScoreDatabase(cat1,cat2,10,10);
		assertEquals(getCategory(52345),cat1);
		database=new MockExpectedScoreDatabase(cat1,cat2,50,10);
		assertEquals(getCategory(52345),cat2);
	}

	private RollCategory getCategory(int dice) {
		List<RollCategory> cats=new ArrayList<RollCategory>();
		cats.add(cat1);
		cats.add(cat2);
		BestCategoryScore chooser=new BestCategoryScore(database,cats);
		return chooser.getBestCategory(DiceRoll.fromFaceValues(dice));
	}

	/**
	 * Created with IntelliJ IDEA.
	 * User: sam
	 * Date: 09/04/15
	 * Time: 9:17 PM
	 * To change this template use File | Settings | File Templates.
	 */
	private static class MockExpectedScoreDatabase extends ExpectedScoreDatabase {
		private RollCategory cat1;
		private RollCategory cat2;
		private float score1;
		private float score2;

		private MockExpectedScoreDatabase(RollCategory cat1, RollCategory cat2, float score1, float score2) {
			this.cat1 = cat1;
			this.cat2 = cat2;
			this.score1 = score1;
			this.score2 = score2;
		}

		@Override
		public float getExpectedScore(List<RollCategory> categories) {
			if (categories.size()==0) return 0;
			else if (categories.size()==1) {
				RollCategory cat=categories.get(0);
				if (cat==cat1) return score1;
				else if (cat==cat2) return score2;
				else assertFalse("Unrecognized category",true);
			}
			assertTrue("Expected max two categories",categories.size()<=2);
			return 0;
		}

		@Override
		public float getExpectedScore(List<RollCategory> categories, RollCategory categoryToExclude) {
			assertTrue("Category inside??",categories.contains(categoryToExclude));
			if (categories.size()==1) return 0;
			assertTrue("Expected two categories",categories.size()==2);
			List<RollCategory> cats=new ArrayList<RollCategory>(categories);
			cats.remove(categoryToExclude);
			RollCategory cat=cats.get(0);
			if (cat==cat1) return score1;
			else if (cat==cat2) return score2;
			else assertFalse("Unrecognized category",true);
			return 0;
		}
	}
}
