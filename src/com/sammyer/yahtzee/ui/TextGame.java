package com.sammyer.yahtzee.ui;

import com.sammyer.yahtzee.DiceRoll;
import com.sammyer.yahtzee.RollCategory;
import com.sammyer.yahtzee.RollTypes;
import com.sammyer.yahtzee.StrategyDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 09/04/15
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextGame {
	private StrategyDatabase database;
	private List<RollCategory> categoriesLeft;
	private BufferedReader console;

	public TextGame(StrategyDatabase database) {
		this.database = database;
		console = new BufferedReader(new InputStreamReader(System.in));
	}

	private String readLine() {
		try {
			return console.readLine();
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			return null;
		}
	}

	private int readDice() {
		String s=readLine();
		if (s==null) return 0;
		return DiceRoll.fromFaceValues(Integer.parseInt(s));
	}

	private RollCategory readCategory(RollCategory defaultCategory) {
		String s=readLine();
		if (s==null||s.isEmpty()) return defaultCategory;
		for (RollCategory category:categoriesLeft) {
			if (category.getName().equals(s)) return category;
		}
		return defaultCategory;
	}

	public void start() {
		categoriesLeft= RollTypes.getRollCategories();
		int dice;
		int keepDice;
		RollCategory category;
		int score=0;
		while (!categoriesLeft.isEmpty()) {
			System.out.print("Dice - roll 1 : ");
			dice=readDice();
			keepDice=database.getDiceToKeepFirstRoll(categoriesLeft,dice);
			System.out.println("Suggested dice to keep : " + DiceRoll.toFaceValues(keepDice));
			System.out.print("Dice - roll 2 : ");
			dice=readDice();
			keepDice=database.getDiceToKeepSecondRoll(categoriesLeft,dice);
			System.out.println("Suggested dice to keep : " + DiceRoll.toFaceValues(keepDice));
			System.out.print("Dice - roll 3 : ");
			dice=readDice();
			category=database.getBestCategory(categoriesLeft,dice);
			System.out.println("Categories left="+RollCategory.getCategoryNames(categoriesLeft));
			for (RollCategory cat:categoriesLeft) {
				System.out.println(String.format("%s%-25s --%.1f+%.1f=%.1f",
						cat==category?"*":" ",
						cat.getName(),
						database.getExpectedScore(categoriesLeft,cat),
						cat.getDiceScore(dice),
						database.getExpectedScore(categoriesLeft,cat)+cat.getDiceScore(dice)
				));
			}
			System.out.println("Suggested category : "+category.getName());
			System.out.print("Category : ");
			category=readCategory(category);
			score+=category.getPointsScored(dice);
			System.out.print("Category chosen : "+category.getName());
			categoriesLeft.remove(category);
			System.out.println("----------");
			System.out.println("SCORE : "+score);
			System.out.println();
		}
	}
}
