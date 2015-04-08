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
	private int[] strategies;
	private float[] strategyScores;
	private boolean cacheHeuristic=true; //for slow heuristics

	public ScoreMaximizer(ScoreHeuristic heuristic) {
		this.heuristic=heuristic;
		generateStrategies();
	}

	@Override
	public float getDiceScore(int dice) {
		return strategyScores[DiceRoll.getInstance().hashDice(dice)];
	}

	public int getDiceToKeep(int dice) {
		return strategies[DiceRoll.getInstance().hashDice(dice)];
	}

	public float getExpectedScore() {
		return expectedScoreAllRolls;
	}

	private float[] generateExpectedScores() {
		DiceRoll rolls=DiceRoll.getInstance();
		float[] expectedScores=new float[DiceRoll.HASH_SIZE];
		for (int idx=rolls.startHashIdx(5);idx<rolls.endHashIdx(5);idx++) {
			expectedScores[idx]=heuristic.getDiceScore(rolls.unhashDice(idx));
		}

		for (int numKept=0;numKept<5;numKept++) {
			int numRolled=5-numKept;
			for (int i=rolls.startHashIdx(numKept);i<rolls.endHashIdx(numKept);i++) {
				int rollCount=0;
				float totalScore=0;
				for (int j=rolls.startHashIdx(numRolled);j<rolls.endHashIdx(numRolled);j++) {
					int permutations=rolls.numPermutations(j);
					rollCount+=permutations;
					int dice=rolls.unhashDice(i)+rolls.unhashDice(j);
					if (cacheHeuristic) totalScore+=permutations*expectedScores[rolls.hashDice(dice)];
					else totalScore+=permutations*heuristic.getDiceScore(dice);
				}
				expectedScores[i]=totalScore/rollCount;
			}
		}

		return expectedScores;
	}

	private void generateStrategies() {
		DiceRoll rolls=DiceRoll.getInstance();
		float[] expectedScores=generateExpectedScores();
		strategies=new int[DiceRoll.HASH_SIZE];
		strategyScores=new float[DiceRoll.HASH_SIZE];

		float totalScore=0;
		int rollCount=0;
		DiceRoll.SubsetIterator subsets;

		for (int idx=rolls.startHashIdx(5);idx<rolls.endHashIdx(5);idx++) {
			//expectedScores[idx]=heuristic.getDiceScore(rolls.unhashDice(idx));
			subsets=new DiceRoll.SubsetIterator(rolls.unhashDice(idx));
			int bestKeepStrategy=0;
			float bestScore=-999;
			while (subsets.hasNext()) {
				int keepers=subsets.next();
				float score=expectedScores[rolls.hashDice(keepers)];
				if (score>bestScore) {
					bestScore=score;
					bestKeepStrategy=keepers;
				}
			}
			strategies[idx]=bestKeepStrategy;
			strategyScores[idx]=bestScore;

			int permutations=rolls.numPermutations(idx);
			totalScore+=permutations*bestScore;
			rollCount+=permutations;
		}
		expectedScoreAllRolls=totalScore/rollCount;
	}
}
