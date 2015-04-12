package com.sammyer.yahtzee;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 06/04/15
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScoreMaximizer implements ScoreHeuristic {
	private ScoreHeuristic heuristic;
	private float expectedScoreAllRolls;
	private DiceRoll[] strategies;
	private float[] strategyScores;
	private boolean cacheHeuristic=true; //for slow heuristics

	public ScoreMaximizer(ScoreHeuristic heuristic) {
		this.heuristic=heuristic;
		generateStrategies();
	}

	@Override
	public float getDiceScore(DiceRoll dice) {
		return strategyScores[dice.getHash()];
	}

	public DiceRoll getDiceToKeep(DiceRoll dice) {
		return strategies[dice.getHash()];
	}

	public float getExpectedScore() {
		return expectedScoreAllRolls;
	}

	private float[] generateExpectedScores() {
		DiceRollHashTable diceHash=DiceRollHashTable.getInstance();
		DiceRoll roll=new DiceRoll();
		DiceRoll baseRoll=new DiceRoll();

		float[] expectedScores=new float[DiceRollHashTable.HASH_SIZE];
		for (int idx=diceHash.startHashIdx(5);idx<diceHash.endHashIdx(5);idx++) {
			roll.setFromHash(idx);
			expectedScores[idx]=heuristic.getDiceScore(roll);
		}

		for (int numKept=0;numKept<5;numKept++) {
			int numRolled=5-numKept;
			for (int i=diceHash.startHashIdx(numKept);i<diceHash.endHashIdx(numKept);i++) {
				int rollCount=0;
				float totalScore=0;
				baseRoll.setFromHash(i);

				for (int j=diceHash.startHashIdx(numRolled);j<diceHash.endHashIdx(numRolled);j++) {
					int permutations=diceHash.numPermutations(j);
					rollCount+=permutations;

					roll.setFromHash(j);
					roll.addDice(baseRoll);

					if (cacheHeuristic) totalScore+=permutations*expectedScores[roll.getHash()];
					else totalScore+=permutations*heuristic.getDiceScore(roll);
				}
				expectedScores[i]=totalScore/rollCount;
			}
		}

		return expectedScores;
	}

	private void generateStrategies() {
		DiceRollHashTable diceHash=DiceRollHashTable.getInstance();
		DiceRoll roll=new DiceRoll();
		float[] expectedScores=generateExpectedScores();
		strategies=new DiceRoll[DiceRollHashTable.HASH_SIZE];
		strategyScores=new float[DiceRollHashTable.HASH_SIZE];

		float totalScore=0;
		int rollCount=0;
		DiceRollSubsetIterator subsets;

		for (int idx=diceHash.startHashIdx(5);idx<diceHash.endHashIdx(5);idx++) {
			//expectedScores[idx]=heuristic.getDiceScore(rolls.unhashDice(idx));
			roll.setFromHash(idx);
			DiceRoll bestKeepStrategy=new DiceRoll();
			float bestScore=-999;
			DiceRollSubsetIterator subset=roll.getSubsetIterator();
			while (subset.hasNext()) {
				subset.next();
				float score=expectedScores[subset.getHash()];
				if (score>bestScore) {
					bestScore=score;
					bestKeepStrategy.set(subset);
				}
			}
			strategies[idx]=bestKeepStrategy;
			strategyScores[idx]=bestScore;

			int permutations=diceHash.numPermutations(idx);
			totalScore+=permutations*bestScore;
			rollCount+=permutations;
		}
		expectedScoreAllRolls=totalScore/rollCount;
	}
}
