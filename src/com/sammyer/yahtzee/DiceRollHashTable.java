package com.sammyer.yahtzee;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 06/04/15
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiceRollHashTable {
	//dice is stored as 0xABCDEF where A,B,C,D,E,F is the number of 6s, 5s, 4s, 3s, 2s, and 1s
	//found in the roll
	//it can be converted also into a densely packed hash value for array storage

	private static int[] DICE_HASH_INDEX={0,1,7,28,84,210,462}; //i.e. 7 choose x
	public static int HASH_SIZE =462;

	//lookup table hash->dice
	private int[] unhashTable;
	//lookup table hash->number of permutations of this combination of dice (since dice are unordered)
	//e.g. there is one way you can roll 1,1,1,1,1  but 120 ways you can roll a straight with one of each number 1-5
	private int[] permutationTable;
	private static DiceRollHashTable instance;

	protected DiceRollHashTable() {
		unhashTable=new int[HASH_SIZE];
		permutationTable=new int[HASH_SIZE];
		for (int i=0;i< HASH_SIZE;i++) {
			int dice=doUnhashDice(i);
			unhashTable[i]=dice;
			permutationTable[i]=calculatePermutations(dice);
		}
	}

	public static DiceRollHashTable getInstance() {
		if (instance==null) instance=new DiceRollHashTable();
		return instance;
	}

	private static int[] CHOOSE5={0,1,5,15,35,70,126,210};
	private int getOffset(int level, int diceLeft) {
		int n=7-level;
		switch (diceLeft) {
			case 0: return 0;
			case 1: return 1;
			case 2: return n;
			case 3: return n*(n+1)/2;
			case 4: return n*(n+1)*(n+2)/6;
			case 5: return CHOOSE5[n];
			default: return 0;
		}
	}

	public int hashDice(int dice) {
		int hash=0;
		int diceLeft=DiceRoll.getNumDice(dice);
		for (int i=0;i<6;i++) {
			hash+=getOffset(i,diceLeft);
			diceLeft-=(dice&0xF);
			dice>>=4;
		}
		return hash;
	}

	private int doUnhashDice(int hash) {
		int diceLeft=0;
		int prevDiceLeft=0;
		boolean firstTime=true;
		int dice=0;
		int pos=1;
		for (int i=0;i<6;i++) {
			prevDiceLeft=diceLeft;
			diceLeft=offsetToDiceLeft(hash,i);
			hash-=getOffset(i,diceLeft);
			if (firstTime) firstTime=false;
			else {
				dice+=pos*(prevDiceLeft-diceLeft);
				pos<<=4;
			}
		}
		dice+=pos*diceLeft;
		return dice;
	}

	private int offsetToDiceLeft(int x, int level) {
		for (int i=5;i>0;i--) if (x>=getOffset(level,i)) return i;
		return 0;
	}

	public int startHashIdx(int numDice) {
		return DICE_HASH_INDEX[numDice];
	}
	public int endHashIdx(int numDice) {
		return DICE_HASH_INDEX[numDice+1];
	}

	public int unhashDice(int hash) {
		return unhashTable[hash];
	}

	private int factorial(int n) {
		switch (n) {
			case 2: return 2;
			case 3: return 6;
			case 4: return 24;
			case 5: return 120;
		}
		return 1;
	}

	public int numPermutations(int hash) {
		return permutationTable[hash];
	}
	private int calculatePermutations(int dice) {
		int numDice=DiceRoll.getNumDice(dice);
		int n=factorial(numDice);
		for (int i=0;i<6;i++) {
			int count=(dice&0xF);
			if (count>1) n/=factorial(count);
			dice>>=4;
		}
		return n;
	}
}

/*
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