package com.sammyer.yahtzee;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 06/04/15
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiceRoll {
	protected int diceCounts;
	//diceCounts is stored as 0xABCDEF where A,B,C,D,E,F is the number of 6s, 5s, 4s, 3s, 2s, and 1s
	//found in the roll
	//it can be converted also into a densely packed hash value for array storage

	public DiceRoll(int diceCounts) {
		this.diceCounts=diceCounts;
	}
	public DiceRoll() {
		diceCounts=0;
	}

	public static DiceRoll fromHash(int hash) {
		DiceRoll roll=new DiceRoll();
		roll.setFromHash(hash);
		return roll;
	}
	public static DiceRoll fromFaceValues(int numbers) {
		DiceRoll dice=new DiceRoll();
		dice.setFromFaceValues(numbers);
		return dice;
	}
	public static DiceRoll randomRoll(int numDice) {
		DiceRoll dice=new DiceRoll();
		dice.setRandom(numDice);
		return dice;
	}

	public int getHash() {
		return DiceRollHashTable.getInstance().hashDice(diceCounts);
	}

	public void setFromHash(int hash) {
		diceCounts=DiceRollHashTable.getInstance().unhashDice(hash);
	}

	public int getNumDice() {
		return getNumDice(diceCounts);
	}

	public int getSum() {
		return getDiceSum(diceCounts);
	}

	public static int getNumDice(int diceCounts) {
		int numDice=0;
		for (int i=0;i<6;i++) {
			numDice+=(diceCounts&0xF);
			diceCounts>>=4;
		}
		return numDice;
	}

	public static int getDiceSum(int diceCounts) {
		int sum=0;
		for (int i=1;i<=6;i++) {
			sum+=i*(diceCounts&0xF);
			diceCounts>>=4;
		}
		return sum;
	}

	public int getDiceCounts() {
		return diceCounts;
	}

	public void setDiceCounts(int diceCounts) {
		this.diceCounts=diceCounts;
	}

	public void clear() {
		diceCounts=0;
	}

	public int getCount(int number) {
		return (diceCounts>>((number-1)*4))&0xF;
	}

	public void setCount(int number, int count) {
		int offset=4*(number-1);
		int mask=0xFFFFFF-(0xF<<offset);
		diceCounts=(diceCounts&mask)+(count<<offset);
	}
	public void incrementCount(int number) {
		int offset=4*(number-1);
		diceCounts+=(1<<offset);
	}
	public void addDice(DiceRoll otherRoll) {
		diceCounts+=otherRoll.diceCounts;
	}


	//converts a number of form 53533 to counts of each number e.g. 0x020300
	public void setFromFaceValues(int numbers) {
		clear();
		while (numbers>0) {
			int n=numbers%10;
			numbers-=n;
			numbers/=10;
			if (n>0) incrementCount(n);
		}
	}

	public int getFaceValues() {
		int numbers=0;
		int n=1;
		int counts=diceCounts;
		while (counts>0) {
			int count=counts&0xF;
			if (count==0) {
				n++;
				counts>>=4;
			} else {
				numbers*=10;
				numbers+=n;
				counts--;
			}
		}
		return numbers;
	}

	public void setRandom(int numDice) {
		clear();
		for (int i=0;i<numDice;i++) {
			int n=(int)Math.floor(Math.random()*6)+1;
			incrementCount(n);
		}
	}

	public DiceRollSubsetIterator getSubsetIterator() {
		return new DiceRollSubsetIterator(this);
	}

	public DiceRoll clone() {
		return new DiceRoll(diceCounts);
	}

	public void set(DiceRoll dice) {
		diceCounts=dice.getDiceCounts();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DiceRoll diceRoll = (DiceRoll) o;

		if (diceCounts != diceRoll.diceCounts) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return diceCounts;
	}
}

/*
alternate hash function

def hd5(a):
	while len(a)<5: a.insert(0,0)
	x=0
	for i in range(5):
		x+=choose(10-i-a[i],5-i)
	return x


def hdrev(x):
	a=[]
	for i in range(5):
		for j in range(7):
			v=choose(10-i-j,5-i)
			if x>=v:
				a.append(j)
				x-=v
				break
*/