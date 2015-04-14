package com.sammyer.yahtzee.ui;

import com.sammyer.yahtzee.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 14/04/15
 * Time: 3:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class VersusGame {
	private BufferedReader console;
	private ExpectedScoreDatabase database;

	public VersusGame(ExpectedScoreDatabase database) {
		console = new BufferedReader(new InputStreamReader(System.in));
		this.database=database;
	}

	private String readLine() {
		try {
			return console.readLine();
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			return null;
		}
	}

	private DiceRoll readDice() {
		String s=readLine();
		if (s==null||s.isEmpty()) return new DiceRoll();
		try {
			return DiceRoll.fromFaceValues(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return new DiceRoll();
		}
	}
	private RollCategory readCategory() {
		RollCategory category=null;
		while (category==null) {
			System.out.print("Choose a category: ");
			String s=readLine();
			category=RollTypes.parseCategory(s);
			if (category==null) System.out.println("Invalid category");
		}
		return category;
	}
	public void start() {
		GameStrategy playerStrategy=new GameStrategy(database);
		GameStrategy aiStrategy=new GameStrategy(database);
		DiceRoll dice;
		DiceRoll playerKeep;
		DiceRoll aiKeep;
		DiceRoll playerDice=new DiceRoll();
		DiceRoll aiDice=new DiceRoll();
		RollCategory playerCategory;
		RollCategory aiCategory;
		int round=1;
		while (!playerStrategy.getCategoriesLeft().isEmpty()) {
			System.out.println("----------ROUND "+round+" ---------------------");
			System.out.println("Categories left : "+RollCategory.getCategoryNames(playerStrategy.getCategoriesLeft()));
			dice=DiceRoll.randomRoll(5);
			System.out.println("Dice - roll 1 : "+dice.getFaceValues());
			playerDice.set(dice);
			aiDice.set(dice);

			System.out.print("Dice to keep: ");
			playerKeep=readDice();
			aiKeep=aiStrategy.getDiceToKeepFirstRoll(aiDice);
			System.out.println("Ai keeps " + aiKeep.getFaceValues());

			System.out.print("Dice roll 2: ");
			playerDice.set(playerKeep);
			aiDice.set(aiKeep);
			for (int i=0;i<5;i++) {
				dice.setRandom(1);
				if (playerDice.getNumDice()<5) playerDice.addDice(dice);
				if (aiDice.getNumDice()<5) aiDice.addDice(dice);
				System.out.print(dice.getFaceValues());
			}
			System.out.println("\nPlayer dice "+playerDice.getFaceValues()+"\t\tAI dice "+aiDice.getFaceValues());

			System.out.print("Dice to keep: ");
			playerKeep=readDice();
			aiKeep=aiStrategy.getDiceToKeepSecondRoll(aiDice);
			System.out.println("Ai keeps "+aiKeep.getFaceValues());


			System.out.print("Dice roll 3: ");
			playerDice.set(playerKeep);
			aiDice.set(aiKeep);
			for (int i=0;i<5;i++) {
				dice.setRandom(1);
				if (playerDice.getNumDice()<5) playerDice.addDice(dice);
				if (aiDice.getNumDice()<5) aiDice.addDice(dice);
				System.out.print(dice.getFaceValues());
			}
			System.out.println("\nPlayer dice "+playerDice.getFaceValues()+"\t\tAI dice "+aiDice.getFaceValues());

			playerCategory=readCategory();
			aiCategory=aiStrategy.getSuggestedCategory(aiDice);
			System.out.println("Your category: "+playerCategory.getName()+"\t\tAI Category: "+aiCategory.getName()+"\n");

			playerStrategy.selectCategory(playerCategory, playerDice);
			aiStrategy.selectCategory(aiCategory, aiDice);
			System.out.println("---");
			System.out.println(String.format("SCORE    - Player : %d\t\t\t\tAI : %d",
					playerStrategy.getTotalScore(), aiStrategy.getTotalScore()));
			System.out.println(String.format("LEFTHAND - Player : %d\t\t\t\tAI : %d",
					playerStrategy.getNumberRollScore(),aiStrategy.getNumberRollScore()));
			System.out.println(String.format("YAHTZEE  - Player : %b\t\t\t\tAI : %b",
					playerStrategy.getHasYahtzee(),aiStrategy.getHasYahtzee()));
			System.out.println();
		}
	}
}
