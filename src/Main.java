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
public class Main {
	private static String DB_PATH="C:\\Users\\sam\\Documents\\python\\yahtzee\\yahtzee.db";
	public static void main(String args[]) {
		//MainTest test=new MainTest();
		//test.test();

		Main main=new Main();
		//main.generateDatabase();
		main.game();
		//main.test();
	}

	private void game() {
		StrategyDatabase database=new StrategyDatabase();
		try {
			database.loadAll(DB_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		TextGame game=new TextGame(database);
		game.start();
	}

	private void test() {
		StrategyDatabase database=new StrategyDatabase();
		try {
			database.loadAll(DB_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i=11;i<=13;i++) {
			CombinationIterator<RollCategory> iterator=new CombinationIterator<RollCategory>(RollTypes.getRollCategories(),i);
			while (iterator.hasNext()) {
				IRollStrategy strategy=database.getStrategy(iterator.next());
				System.out.println(String.format("%s - %.2f",
						strategy.getCategoryNames(),
						strategy.getExpectedScore()));
			}
		}
	}

	private void generateDatabase() {
		StrategyCalculator calculator=new StrategyCalculator();
		long t=System.currentTimeMillis();
		StrategyDatabase strategyDatabase=calculator.generateStrategies();
		long tdiff=System.currentTimeMillis()-t;

		try {
			strategyDatabase.save(DB_PATH);
		} catch (IOException e) {
			System.out.println("Error saving database - "+e.toString());
		}
		System.out.println("Complete in "+(int)tdiff+" ms");
	}

}
