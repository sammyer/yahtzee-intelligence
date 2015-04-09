package com.sammyer.yahtzee;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 08/04/15
 * Time: 9:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class StrategyDatabase {
	private List<RollCategory> rollCategories;
	private StrategyDatabaseEntry[] strategies=new StrategyDatabaseEntry[8192];

	public StrategyDatabase() {
		rollCategories=RollTypes.getRollCategories();
	}
//----------------------------------------------------------

	public static int getIdx(List<RollCategory> allCategories, List<RollCategory> availableCategories) {
		int idx=0;
		for (RollCategory category:availableCategories) idx+=getIdx(allCategories,category);
		return idx;
	}

	private static int getIdx(List<RollCategory> allCategories, RollCategory category) {
		return 1<<allCategories.indexOf(category);
	}

	private int getIdx(List<RollCategory> availableCategories) {
		return getIdx(rollCategories,availableCategories);
	}
	private int getIdx(RollCategory category) {
		return getIdx(rollCategories,category);
	}

	public static List<RollCategory> getCategories(List<RollCategory> allCategories, int index) {
		List<RollCategory> categories=new ArrayList<RollCategory>();
		for (RollCategory category:allCategories) {
			if ((index&1)!=0) categories.add(category);
			index>>=1;
		}
		return categories;
	}
//----------------------------------------------------------
	public void add(RollStrategy strategy) {
		int idx=getIdx(rollCategories,strategy.getAvailableCategories());
		strategies[idx]=new StrategyDatabaseEntry(strategy);
	}
	private IRollStrategy getStrategy(int idx) {
		return strategies[idx];
	}
//----------------------------------------------------------
	public void save(String path) throws IOException {
		FileOutputStream stream = new FileOutputStream(path);
		try {
			for (int i=1;i<8192;i++) {
				stream.write(strategies[i].toBytes());
			}
		} catch (NullPointerException e) {
			//failed
		} finally {
			stream.close();
		}
	}
	public void loadAll(String path) throws IOException {
		FileInputStream stream=new FileInputStream(path);
		byte[] data=new byte[StrategyDatabaseEntry.BYTE_SIZE];
		try {
			for (int i=1;i<8192;i++) {
				stream.read(data);
				strategies[i]=new StrategyDatabaseEntry(data,getCategories(rollCategories,i));
			}
		} catch (NullPointerException e) {
			//failed
		} finally {
			stream.close();
		}
	}

//----------------------------------------------------------

	public IRollStrategy getStrategy(List<RollCategory> categories) {
		return getStrategy(getIdx(rollCategories, categories));
	}
	public IRollStrategy getStrategy(RollCategory category) {
		return getStrategy(getIdx(rollCategories, category));
	}
	public IRollStrategy getStrategy(RollCategory... categories) {
		return getStrategy(Arrays.asList(categories));
	}

	public float getExpectedScore(List<RollCategory> categories, RollCategory categoryToExclude) {
		int idx=getIdx(categories)-getIdx(categoryToExclude);
		return getStrategy(idx).getExpectedScore();
	}

	public float getExpectedScore(List<RollCategory> categories) {
		return getStrategy(categories).getExpectedScore();
	}

	public int getDiceToKeepFirstRoll(List<RollCategory> categories, int dice) {
		return getStrategy(categories).getDiceToKeepFirstRoll(dice);
	}
	public int getDiceToKeepSecondRoll(List<RollCategory> categories, int dice) {
		return getStrategy(categories).getDiceToKeepFirstRoll(dice);
	}
	public RollCategory getBestCategory(List<RollCategory> categories, int dice) {
		return getStrategy(categories).getBestCategory(dice);
	}
}
