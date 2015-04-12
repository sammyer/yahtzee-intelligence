import com.sammyer.yahtzee.*;
import com.sammyer.yahtzee.ui.TextGame;

import java.io.IOException;
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
		main.game();
		//main.test();
	}

	private void game() {
		ExpectedScoreDatabase database=new ExpectedScoreDatabase();
		try {
			database.load(GameConstants.DB_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		TextGame game=new TextGame(database);
		game.start();
	}

	private void test() {
		ExpectedScoreDatabase database=new ExpectedScoreDatabase();
		try {
			database.load(GameConstants.DB_PATH);
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
			expectedScoreDatabase.save(GameConstants.DB_PATH);
		} catch (IOException e) {
			System.out.println("Error saving database - "+e.toString());
		}
		System.out.println("Complete in "+(int)tdiff+" ms");
	}

}