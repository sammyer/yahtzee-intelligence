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
 * Date: 09/04/15
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextGame {
	private BufferedReader console;
	private GameStrategy strategy;
	private static final int MAX_CATS_TO_SHOW=4;

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

	private DiceRoll readDice() {
		String s=readLine();
		if (s==null) return new DiceRoll();
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
		DiceRoll dice;
		DiceRoll keepDice;
		RollCategory category;
		while (!strategy.getCategoriesLeft().isEmpty()) {
			System.out.print("Dice - roll 1 : ");
			dice=readDice();
			keepDice=strategy.getDiceToKeepFirstRoll(dice);
			System.out.println("Suggested dice to keep : " + keepDice.getFaceValues());
			System.out.print("Dice - roll 2 : ");
			dice=readDice();
			keepDice=strategy.getDiceToKeepSecondRoll(dice);
			System.out.println("Suggested dice to keep : " + keepDice.getFaceValues());
			System.out.print("Dice - roll 3 : ");
			dice=readDice();
			category=strategy.getSuggestedCategory(dice);
			List<RollCategory> categoriesLeft=strategy.getCategoriesLeft();
			final DiceRoll sortDice=dice;
			Collections.sort(categoriesLeft,new Comparator<RollCategory>() {
				@Override
				public int compare(RollCategory o1, RollCategory o2) {
					return Float.compare(
							strategy.getExpectedFinalScore(o2,sortDice),
							strategy.getExpectedFinalScore(o1,sortDice));
				}
			});
			System.out.println("Categories left=" + RollCategory.getCategoryNames(categoriesLeft));

			int i=0;
			for (RollCategory cat:categoriesLeft) {
				if (i==MAX_CATS_TO_SHOW) break;
				i++;

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
