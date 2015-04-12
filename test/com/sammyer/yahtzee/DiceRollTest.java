package com.sammyer.yahtzee;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 12/04/15
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiceRollTest extends TestCase {

	public void testGetHash() throws Exception {
		DiceRoll dice=new DiceRoll();
		DiceRoll dice2=new DiceRoll();
		for (int i=0;i<DiceRollHashTable.HASH_SIZE;i++) {
			dice.setFromHash(i);
			dice2.set(dice);
			assertEquals(dice2.getHash(),i);
		}
	}

	public void testGetNumDice() throws Exception {
		assertEquals(new DiceRoll().getNumDice(),0);
		assertEquals(new DiceRoll(1).getNumDice(),1);
		assertEquals(DiceRoll.fromFaceValues(6).getNumDice(),1);
		assertEquals(DiceRoll.fromFaceValues(444).getNumDice(),3);
		assertEquals(DiceRoll.fromFaceValues(16361).getNumDice(),5);
	}

	public void testGetSum() throws Exception {
		assertEquals(new DiceRoll().getSum(),0);
		assertEquals(new DiceRoll(1).getSum(),1);
		assertEquals(DiceRoll.fromFaceValues(6).getSum(),6);
		assertEquals(DiceRoll.fromFaceValues(444).getSum(),12);
		assertEquals(DiceRoll.fromFaceValues(16361).getSum(),17);
	}

	public void testGetCount() throws Exception {
		DiceRoll dice=DiceRoll.fromFaceValues(24622);
		assertEquals(dice.getCount(2),3);
		assertEquals(dice.getCount(3),0);
		assertEquals(dice.getCount(4),1);
	}

	public void testSetCount() throws Exception {
		DiceRoll dice=DiceRoll.fromFaceValues(24622);
		dice.setCount(2,1);
		assertEquals(dice,DiceRoll.fromFaceValues(246));
	}

	public void testIncrementCount() throws Exception {
		DiceRoll dice=DiceRoll.fromFaceValues(24);
		dice.incrementCount(2);
		dice.incrementCount(2);
		dice.incrementCount(5);
		assertEquals(dice,DiceRoll.fromFaceValues(22245));
	}

	public void testAddDice() throws Exception {
		DiceRoll dice1=DiceRoll.fromFaceValues(13);
		DiceRoll dice2=DiceRoll.fromFaceValues(23);
		dice1.addDice(dice2);
		assertEquals(dice1,DiceRoll.fromFaceValues(1233));
		dice2.clear();
		dice1.addDice(dice2);
		assertEquals(dice1, DiceRoll.fromFaceValues(1233));
	}

	public void testSetFromFaceValues() throws Exception {
		DiceRoll dice=new DiceRoll();
		dice.setFromFaceValues(53553);
		assertEquals(dice.getDiceCounts(),0x030200);
		assertEquals(DiceRoll.fromFaceValues(11111), new DiceRoll(0x000005));
		assertEquals(DiceRoll.fromFaceValues(63452),DiceRoll.fromFaceValues(23456));
	}

	public void testGetFaceValues() throws Exception {
		assertEquals(new DiceRoll(0x030200).getFaceValues(),33555);
		assertEquals(new DiceRoll(0x000005).getFaceValues(),11111);
		assertEquals(DiceRoll.fromFaceValues(63452).getFaceValues(),23456);
	}

	public void testSetRandom() throws Exception {
		DiceRoll dice=new DiceRoll();
		dice.setRandom(4);
		assertEquals(dice.getNumDice(),4);

		//ensure dice are not always the same
		int counts=dice.getDiceCounts();
		boolean allEqual=true;
		for (int i=0;i<10;i++) {
			dice.setRandom(4);
			if (dice.getDiceCounts()!=counts) allEqual=false;
		}
		assertFalse(allEqual);
	}


	public void testSubsetIterator() throws Exception {
		DiceRollSubsetIterator subsets=DiceRoll.fromFaceValues(11336).getSubsetIterator();
		List<Integer> subsetList=new ArrayList<Integer>();
		while (subsets.hasNext()&&subsetList.size()<100) subsetList.add(subsets.next().getFaceValues());
		assertEquals(subsetList.size(),18);
		assertTrue(subsetList.contains(0));
		assertTrue(subsetList.contains(1));
		assertTrue(subsetList.contains(3));
		assertTrue(subsetList.contains(6));
		assertTrue(subsetList.contains(11));
		assertTrue(subsetList.contains(13));
		assertTrue(subsetList.contains(16));
		assertTrue(subsetList.contains(33));
		assertTrue(subsetList.contains(36));
		assertTrue(subsetList.contains(113));
		assertTrue(subsetList.contains(116));
		assertTrue(subsetList.contains(133));
		assertTrue(subsetList.contains(136));
		assertTrue(subsetList.contains(336));
		assertTrue(subsetList.contains(1133));
		assertTrue(subsetList.contains(1136));
		assertTrue(subsetList.contains(1336));
		assertTrue(subsetList.contains(11336));
	}

	public void testClone() throws Exception {
		DiceRoll dice1=DiceRoll.fromFaceValues(1443);
		DiceRoll dice2=dice1.clone();
		assertEquals(dice1,dice2);
		assertFalse(dice1==dice2);
	}

	public void testSet() throws Exception {
		DiceRoll dice1=DiceRoll.fromFaceValues(13);
		DiceRoll dice2=DiceRoll.fromFaceValues(23);
		dice1.set(dice2);
		assertEquals(dice1,dice2);
	}

	private void assertEquals(DiceRoll dice1, DiceRoll dice2) {
		//assertEquals(dice1.getDiceCounts(),dice2.getDiceCounts());
		assertEquals(dice1.getFaceValues(),dice2.getFaceValues());
	}
}
