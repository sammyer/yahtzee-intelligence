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
public class DiceRollTest extends TestCase {
	private DiceRoll rolls;

	public void setUp() throws Exception {
		rolls=DiceRoll.getInstance();
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

	public void testGetNumDice() throws Exception {
		assertEquals(DiceRoll.getNumDice(0),0);
		assertEquals(DiceRoll.getNumDice(1),1);
		assertEquals(DiceRoll.getNumDice(0x100000),1);
		assertEquals(DiceRoll.getNumDice(0x003000),3);
		assertEquals(DiceRoll.getNumDice(0x100202),5);
	}

	public void testGetDiceSum() throws Exception {
		assertEquals(DiceRoll.getDiceSum(0),0);
		assertEquals(DiceRoll.getDiceSum(1),1);
		assertEquals(DiceRoll.getDiceSum(0x100000),6);
		assertEquals(DiceRoll.getDiceSum(0x003000),12);
		assertEquals(DiceRoll.getDiceSum(0x100202),14);
	}

	public void testUnhashDice() throws Exception {
		for (int hash=0;hash<462;hash++) {
			int dice=rolls.unhashDice(hash);
			assertEquals(String.format("Hash not reversed  %d - %x",hash,dice),hash,rolls.hashDice(dice));
		}
	}

	public void testNumPermutations() throws Exception {
		assertEquals(rolls.numPermutations(rolls.hashDice(0)),1);
		assertEquals(rolls.numPermutations(rolls.hashDice(1)),1);
		assertEquals(rolls.numPermutations(rolls.hashDice(0x111110)),120);
		assertEquals(rolls.numPermutations(rolls.hashDice(0x005000)),1);
		assertEquals(rolls.numPermutations(rolls.hashDice(0x100202)),30);
	}

	public void testGetCount() throws Exception {
		assertEquals(DiceRoll.getCount(0x101030, 2),3);
	}

	public void testSetCount() throws Exception {
		assertEquals(DiceRoll.setCount(0x101030, 4, 2),0x102030);
	}

	public void testIncrementCount() throws Exception {
		assertEquals(DiceRoll.incrementCount(0x101030, 4),0x102030);
	}

	public void testFromFaceValues() throws Exception {
		assertEquals(DiceRoll.fromFaceValues(53553),0x030200);
		assertEquals(DiceRoll.fromFaceValues(11111),0x000005);
		assertEquals(DiceRoll.fromFaceValues(63452),DiceRoll.fromFaceValues(23456));
	}

	public void testToFaceValues() throws Exception {
		assertEquals(DiceRoll.toFaceValues(0x030200),33555);
		assertEquals(DiceRoll.toFaceValues(0x000005),11111);
		assertEquals(DiceRoll.toFaceValues(DiceRoll.fromFaceValues(63452)),23456);
	}

	public void testCountIterator() throws Exception {
		DiceRoll.CountIterator counts=new DiceRoll.CountIterator(0x210543);
		for (int i=0;i<6;i++) assertEquals(counts.next(), (3 + i) % 6);
	}

	public void testSubsetIterator() throws Exception {
		DiceRoll.SubsetIterator subsets=new DiceRoll.SubsetIterator(0x100202);
		List<Integer> subsetList=new ArrayList<Integer>();
		while (subsets.hasNext()&&subsetList.size()<100) subsetList.add(subsets.next());
		assertEquals(subsetList.size(),18);
		assertTrue(subsetList.contains(0));
		assertTrue(subsetList.contains(1));
		assertTrue(subsetList.contains(2));
		assertTrue(subsetList.contains(0x100));
		assertTrue(subsetList.contains(0x101));
		assertTrue(subsetList.contains(0x102));
		assertTrue(subsetList.contains(0x200));
		assertTrue(subsetList.contains(0x201));
		assertTrue(subsetList.contains(0x202));
		assertTrue(subsetList.contains(0x100000));
		assertTrue(subsetList.contains(0x100001));
		assertTrue(subsetList.contains(0x100002));
		assertTrue(subsetList.contains(0x100100));
		assertTrue(subsetList.contains(0x100101));
		assertTrue(subsetList.contains(0x100102));
		assertTrue(subsetList.contains(0x100200));
		assertTrue(subsetList.contains(0x100201));
		assertTrue(subsetList.contains(0x100202));
	}
}
