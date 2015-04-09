import com.sammyer.yahtzee.*;

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
	public static void main(String args[]) {
		//MainTest test=new MainTest();
		//test.test();

		Main main=new Main();
		main.generateDatabase();
	}

	private void generateDatabase() {
		StrategyCalculator calculator=new StrategyCalculator();
		long t=System.currentTimeMillis();
		StrategyDatabase strategyDatabase=calculator.generateStrategies();
		long tdiff=System.currentTimeMillis()-t;

		try {
			strategyDatabase.save("C:\\Users\\sam\\Documents\\python\\yahtzee\\yahtzee.db");
		} catch (IOException e) {
			System.out.println("Error saving database - "+e.toString());
		}
		System.out.println("Complete in "+(int)tdiff+" ms");
	}

}
