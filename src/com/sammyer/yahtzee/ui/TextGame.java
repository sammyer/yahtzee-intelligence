package com.sammyer.yahtzee.ui;

import com.sammyer.yahtzee.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 09/04/15
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextGame {
	private BufferedReader console;
	private GameStrategy strategy;

	public TextGame(ExpectedScoreDatabase database) {
		this.strategy=new GameStrategy(database);
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
		for (RollCategory category:strategy.getCategoriesLeft()) {
			if (category.getName().equals(s)) return category;
		}
		return defaultCategory;
	}

	public void start() {
		int dice;
		int keepDice;
		RollCategory category;
		while (!strategy.getCategoriesLeft().isEmpty()) {
			System.out.print("Dice - roll 1 : ");
			dice=readDice();
			keepDice=strategy.getDiceToKeepFirstRoll(dice);
			System.out.println("Suggested dice to keep : " + DiceRoll.toFaceValues(keepDice));
			System.out.print("Dice - roll 2 : ");
			dice=readDice();
			keepDice=strategy.getDiceToKeepSecondRoll(dice);
			System.out.println("Suggested dice to keep : " + DiceRoll.toFaceValues(keepDice));
			System.out.print("Dice - roll 3 : ");
			dice=readDice();
			category=strategy.getSuggestedCategory(dice);
			List<RollCategory> categoriesLeft=strategy.getCategoriesLeft();
			System.out.println("Categories left=" + RollCategory.getCategoryNames(categoriesLeft));
			for (RollCategory cat:categoriesLeft) {
				System.out.println(String.format("%s%-18s pts=%d   sc=%d+%.1f+%.1f=%.1f",
						cat==category?"*":" ",
						cat.getName(),
						strategy.getPointsScored(cat,dice),
						strategy.getTotalScore(),
						strategy.getExpectedScoreRestOfGame(cat),
						cat.getDiceScore(dice),
						strategy.getExpectedFinalScore(cat,dice)
				));
			}
			System.out.println("Suggested category : " + category.getName());
			System.out.print("Category : ");
			category=readCategory(category);
			strategy.selectCategory(category, dice);
			System.out.print("Category chosen : " + category.getName());
			System.out.println("----------");
			System.out.println(String.format("SCORE : %d      LEFTHAND: %d     YAHTZEE: %b",
					strategy.getTotalScore(),strategy.getNumberRollScore(),strategy.getHasYahtzee()));
			System.out.println();
		}
	}
}
