package com.sammyer.yahtzee;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 12/04/15
 * Time: 12:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExpectedScoreDatabaseTest extends TestCase {
	private List<RollCategory> rollCategories=RollTypes.getRollCategories();
	private ExpectedScoreDatabase database;

	public void setUp() throws Exception {
		database=new ExpectedScoreDatabase();
	}

	public void testGetIdx() throws Exception {
		assertEquals(idx(new ArrayList<RollCategory>()),0);
		assertEquals(idx(rollCategories),8191);
		assertEquals(idx(Arrays.asList(rollCategories.get(0), rollCategories.get(1))),3);
		assertEquals(idx(Arrays.asList(rollCategories.get(12), rollCategories.get(7),rollCategories.get(9))),4096+512+128);
	}

	private List<RollCategory> catSubset(int... idxs) {
		List<RollCategory> cats=new ArrayList<RollCategory>();
		for (int i=0;i<idxs.length;i++) cats.add(rollCategories.get(idxs[i]));
		return cats;
	}

	private int idx(List<RollCategory> cats) {
		return ExpectedScoreDatabase.getIdx(rollCategories,cats);
	}

	public void testGetCategories() throws Exception {
		assertTrue(ExpectedScoreDatabase.getCategories(rollCategories,0).isEmpty());
		List<RollCategory> cats=ExpectedScoreDatabase.getCategories(rollCategories,6);
		assertEquals(cats.size(),2);
		assertTrue(cats.contains(rollCategories.get(1)));
		assertTrue(cats.contains(rollCategories.get(2)));
	}

	public void testAdd() throws Exception {
		List<RollCategory> cats=Arrays.asList(rollCategories.get(1),rollCategories.get(5),rollCategories.get(7));
		float val=2.34f;
		database.add(cats,val);
		assertEquals(database.getExpectedScore(cats),val);
	}

	public void testSave() throws Exception {
		float val=2.34f;
		for (int i=0;i<8192;i++) {
			database.add(ExpectedScoreDatabase.getCategories(rollCategories,i),val*i);
		}
		database.save(GameConstants.TEST_DB_PATH);
		ExpectedScoreDatabase database2=new ExpectedScoreDatabase();
		database2.load(GameConstants.TEST_DB_PATH);
		for (int i=0;i<8192;i++) {
			assertEquals(database2.getExpectedScore(i),val*i);
		}
	}

	private class MockRollStrategy extends RollStrategy {
		private MockRollStrategy() {
			super(database, rollCategories);
		}
	}
}
