import com.sammyer.yahtzee.*;
import com.sammyer.yahtzee.ui.GameSimulation;
import com.sammyer.yahtzee.ui.TextGame;
import com.sammyer.yahtzee.ui.VersusGame;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class YahtzeeMain {
	public static void main(String args[]) {
		//MainTest test=new MainTest();
		//test.test();

		YahtzeeMain main=new YahtzeeMain();
		//main.generateDatabase();
		//main.game();
		main.versus();
		//main.simulate();
		//System.out.println(main.getDataUri());
	}

	public InputStream getDataUri() {
		return getClass().getClassLoader().getResourceAsStream("yahtzee.bin");
		//return GameConstants.DB_PATH;
	}

	private String getOutputUri() {
		return null;
	}

	private void game() {
		ExpectedScoreDatabase database=new ExpectedScoreDatabase();
		try {
			database.load(getDataUri());
		} catch (Exception e) {
			e.printStackTrace();
		}
		TextGame game=new TextGame(database);
		game.start();
	}

	private void versus() {
		ExpectedScoreDatabase database=new ExpectedScoreDatabase();
		try {
			database.load(getDataUri());
		} catch (Exception e) {
			e.printStackTrace();
		}
		VersusGame game=new VersusGame(database);
		game.start();
	}

	private void simulate() {
		ExpectedScoreDatabase database=new ExpectedScoreDatabase();
		try {
			database.load(getDataUri());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameSimulation simulation=new GameSimulation(database);
		int numGames=3;
		simulation.verbose=numGames<5;
		int totalScore=0;
		for (int i=0;i<numGames;i++) {
			int score=simulation.simulate();
			System.out.print("| " + score);
			totalScore+=Math.min(score,350);
		}
		float avgScore=totalScore/(float)numGames;
		System.out.printf("\nAverage score : %.1f\n", avgScore);

	}

	private void test() {
		ExpectedScoreDatabase database=new ExpectedScoreDatabase();
		try {
			database.load(getDataUri());
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i=11;i<=13;i++) {
			CombinationIterator<RollCategory> iterator=new CombinationIterator<RollCategory>(RollTypes.getRollCategories(),i);
			while (iterator.hasNext()) {
				List<RollCategory> categories=iterator.next();
				System.out.println(String.format("%s - %.2f",
						RollCategory.getCategoryNames(categories),
						database.getExpectedScore(categories)));
			}
		}
	}

	private void generateDatabase() {
		ExpectedScoreCalculator calculator=new ExpectedScoreCalculator();
		long t=System.currentTimeMillis();
		ExpectedScoreDatabase expectedScoreDatabase =calculator.generateStrategies();
		long tdiff=System.currentTimeMillis()-t;
		try {
			expectedScoreDatabase.save(getOutputUri());
		} catch (IOException e) {
			System.out.println("Error saving database - "+e.toString());
		}
		System.out.println("\n\nComplete in "+(int)tdiff+" ms");
	}

}
