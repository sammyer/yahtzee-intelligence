package com.sammyer.yahtzee;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 06/04/15
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiceRoll {
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
	private static DiceRoll instance;

	protected DiceRoll() {
		unhashTable=new int[HASH_SIZE];
		permutationTable=new int[HASH_SIZE];
		for (int i=0;i< HASH_SIZE;i++) {
			int dice=doUnhashDice(i);
			unhashTable[i]=dice;
			permutationTable[i]=calculatePermutations(dice);
		}
	}

	public static DiceRoll getInstance() {
		if (instance==null) instance=new DiceRoll();
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
		int diceLeft=getNumDice(dice);
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

	public static int getNumDice(int dice) {
		int numDice=0;
		for (int i=0;i<6;i++) {
			numDice+=(dice&0xF);
			dice>>=4;
		}
		return numDice;
	}

	public static int getDiceSum(int dice) {
		int sum=0;
		for (int i=1;i<=6;i++) {
			sum+=i*(dice&0xF);
			dice>>=4;
		}
		return sum;
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
		int numDice=getNumDice(dice);
		int n=factorial(numDice);
		for (int i=0;i<6;i++) {
			int count=(dice&0xF);
			if (count>1) n/=factorial(count);
			dice>>=4;
		}
		return n;
	}

	public static int getCount(int dice, int number) {
		return (dice>>((number-1)*4))&0xF;
	}

	public static int setCount(int dice, int number, int count) {
		int offset=4*(number-1);
		int mask=0xFFFFFF-(0xF<<offset);
		return (dice&mask)+(count<<offset);
	}
	public static int incrementCount(int dice, int number) {
		int offset=4*(number-1);
		return dice+(1<<offset);
	}

	//converts a number of form 53533 to counts of each number e.g. 0x020300
	public static int fromFaceValues(int numbers) {
		int dice=0;
		for (int i=0;i<5;i++) {
			int n=numbers%10;
			numbers-=n;
			numbers/=10;
			dice=incrementCount(dice,n);
		}
		return dice;
	}

	public static int toFaceValues(int dice) {
		int numbers=0;
		int n=1;
		while (dice>0) {
			int count=dice&0xF;
			if (count==0) {
				n++;
				dice>>=4;
			} else {
				numbers*=10;
				numbers+=n;
				dice--;
			}
		}
		return numbers;
	}

	public static class CountIterator {
		private int dice;

		public CountIterator(int dice) {
			this.dice = dice;
		}

		public int next() {
			int count=dice&0xF;
			dice>>=4;
			return count;
		}

		public boolean hasNext() {
			return dice>0;
		}
	}

	public static class SubsetIterator {
		private int dice;
		private int subset=0;
		private boolean mHasNext=true;

		public SubsetIterator(int dice) {
			this.dice=dice;
		}

		public int next() {
			int currentValue=subset;

			int mask=0xF;
			int mult=1;
			mHasNext=false;
			for (int i=0;i<6;i++) {
				int maxVal=dice&mask;
				int val=subset&mask;
				if (maxVal>0) {
					if (val==maxVal) {
						subset-=val;
					} else {
						subset+=mult;
						mHasNext=true;
						break;
					}
				}
				mult<<=4;
				mask<<=4;
			}
			return currentValue;
		}

		public boolean hasNext() {
			return mHasNext;
		}
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