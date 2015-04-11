package com.sammyer.yahtzee;

import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScoreMaximizerTest extends TestCase {
	private ScoreMaximizer sixes;
	private ScoreMaximizer fullHouse;
	private ScoreMaximizer yahtzee;

	public void setUp() throws Exception {
		long t1,t2;
		t1=System.currentTimeMillis();
		sixes=new ScoreMaximizer(new RollTypes.NumberRoll(6));
		sixes=new ScoreMaximizer(sixes);
;		t2=System.currentTimeMillis();
		System.out.println("Sixes - time=" + (t2 - t1) + "ms");
		t1=System.currentTimeMillis();
		fullHouse=new ScoreMaximizer(RollTypes.fullHouse);
		fullHouse=new ScoreMaximizer(fullHouse);
		t2=System.currentTimeMillis();
		System.out.println("Full house - time=" + (t2 - t1) + "ms");
		t1=System.currentTimeMillis();
		yahtzee=new ScoreMaximizer(RollTypes.yahtzee);
		yahtzee=new ScoreMaximizer(yahtzee);
		t2=System.currentTimeMillis();
		System.out.println("Yahtzee - time=" + (t2 - t1) + "ms");
	}

	public void tearDown() throws Exception {

	}

	public void testGetDiceScore() throws Exception {
		System.out.println("\nSixes dice score 34466 ="+sixes.getDiceScore(0x202100));
		System.out.println("Full house dice score 34466 ="+fullHouse.getDiceScore(0x202100));
		System.out.println("Yahtzee dice score 11116 =" + yahtzee.getDiceScore(0x100004));
	}

	public void testGetDiceToKeep() throws Exception {
		assertEquals(sixes.getDiceToKeep(0x202100),0x200000);
		assertEquals(fullHouse.getDiceToKeep(0x202100),0x202000);
		assertEquals(yahtzee.getDiceToKeep(0x100004),0x000004);
	}

	public void testGetExpectedScore() throws Exception {
		System.out.println("\nSixes expected score="+sixes.getExpectedScore());
		System.out.println("Full house expected score="+fullHouse.getExpectedScore());
		System.out.println("Yahtzee expected score="+yahtzee.getExpectedScore());
	}
}
