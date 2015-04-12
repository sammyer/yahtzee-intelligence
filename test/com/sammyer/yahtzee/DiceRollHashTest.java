package com.sammyer.yahtzee;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiceRollHashTest extends TestCase {
	private DiceRollHashTable rolls;

	public void setUp() throws Exception {
		rolls=DiceRollHashTable.getInstance();
	}

	public void tearDown() throws Exception {

	}

	public void testHashDice() throws Exception {
		//for all valid rolls of 5 dice, check hash is valid and there are no collisions
		boolean[] collision=new boolean[462];
		for (int i=0;i<46656;i++) {
			int x=i;
			int numDice=0;
			int dice=0;
			for (int j=0;j<6;j++) {
				int n=x%6;
				x-=n;
				x/=6;
				numDice+=n;
				dice<<=4;
				dice+=n;
				if (numDice>5) break;
			}
			if (numDice>5) continue;
			int hash=rolls.hashDice(dice);
			assertTrue(String.format("Hash outside boundaries %x - %d",dice,hash),hash>=0&&hash<462);
			assertFalse(String.format("Hash collision %x - %d",dice,hash),collision[hash]);
			collision[hash]=true;
		}
	}


	public void testUnhashDice() throws Exception {
		for (int hash=0;hash<462;hash++) {
			int dice=rolls.unhashDice(hash);
			assertEquals(String.format("Hash not reversed  %d - %x", hash, dice), hash, rolls.hashDice(dice));
		}
	}

	public void testNumPermutations() throws Exception {
		assertEquals(rolls.numPermutations(rolls.hashDice(0)),1);
		assertEquals(rolls.numPermutations(rolls.hashDice(1)),1);
		assertEquals(rolls.numPermutations(rolls.hashDice(0x111110)),120);
		assertEquals(rolls.numPermutations(rolls.hashDice(0x005000)),1);
		assertEquals(rolls.numPermutations(rolls.hashDice(0x100202)),30);
	}
}
