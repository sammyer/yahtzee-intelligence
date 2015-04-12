package com.sammyer.yahtzee;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;

/**
* Created with IntelliJ IDEA.
* User: sam
* Date: 12/04/15
* Time: 3:47 PM
* To change this template use File | Settings | File Templates.
*/
public class DiceRollSubsetIterator extends DiceRoll implements Iterator<DiceRoll> {
	private DiceRoll dice;
	private boolean emptySubset=true;

	public DiceRollSubsetIterator(DiceRoll dice) {
		this.dice=dice;
	}

	public DiceRoll next() {
		if (emptySubset) {
			clear();
			emptySubset=false;
			return this;
		}
		int mask=0xF;
		int mult=1;
		int parentCounts=dice.getDiceCounts();
		for (int i=0;i<6;i++) {
			int maxVal=parentCounts&mask;
			int val=diceCounts&mask;
			if (maxVal>0) {
				if (val==maxVal) {
					diceCounts-=val;
				} else {
					diceCounts+=mult;
					break;
				}
			}
			mult<<=4;
			mask<<=4;
		}
		return this;
	}

	public boolean hasNext() {
		return emptySubset||(diceCounts!=dice.getDiceCounts());
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}
}
