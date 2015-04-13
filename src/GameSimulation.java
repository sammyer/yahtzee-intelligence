import com.sammyer.yahtzee.DiceRoll;
import com.sammyer.yahtzee.ExpectedScoreDatabase;
import com.sammyer.yahtzee.GameStrategy;
import com.sammyer.yahtzee.RollCategory;
import sun.rmi.runtime.Log;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 12/04/15
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameSimulation {
	private ExpectedScoreDatabase database;
	public boolean verbose=true;

	public GameSimulation(ExpectedScoreDatabase database) {
		this.database = database;
	}

	public int simulate() {
		GameStrategy strategy=new GameStrategy(database);
		DiceRoll dice;
		if (verbose) System.out.println("\n-------------------------------------");
		while (!strategy.getCategoriesLeft().isEmpty()) {
			dice= rollDice(strategy);
			RollCategory category=strategy.getSuggestedCategory(dice);
			int score=strategy.getPointsScored(category,dice);
			strategy.selectCategory(category, dice);
			if (verbose) {
				System.out.printf("Choose category : %s (%d)  Total=%d  Leftside=%d\n\n",
						category,score,strategy.getTotalScore(),strategy.getNumberRollScore());
			}
		}
		return strategy.getTotalScore();
	}

	private DiceRoll rollDice(GameStrategy strategy) {
		DiceRoll dice;
		DiceRoll keepers;
		int numDiceToKeep;

		//first roll
		dice=DiceRoll.randomRoll(5);
		keepers=strategy.getDiceToKeepFirstRoll(dice);
		if (verbose) System.out.printf("First roll: %d  --  keep %d\n",dice.getFaceValues(),keepers.getFaceValues());
		if (keepers.getNumDice()==5) return dice;
		else {
			dice.setRandom(5 - keepers.getNumDice());
			dice.addDice(keepers);
		}

		//second roll;
		keepers=strategy.getDiceToKeepSecondRoll(dice);
		if (verbose) System.out.printf("Second roll: %d  --  keep %d\n",dice.getFaceValues(),keepers.getFaceValues());
		if (keepers.getNumDice()==5) return dice;
		else {
			dice.setRandom(5-keepers.getNumDice());
			dice.addDice(keepers);
			if (verbose) System.out.printf("Third roll: %d\n",dice.getFaceValues());
			return dice;
		}
	}
}
