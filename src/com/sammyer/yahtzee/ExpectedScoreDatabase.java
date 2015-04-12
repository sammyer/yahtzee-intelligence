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
public class ExpectedScoreDatabase {
	private List<RollCategory> rollCategories;
	private float[] expectedScore=new float[8192];

	public ExpectedScoreDatabase() {
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
		add(strategy.getAvailableCategories(),strategy.getExpectedScore());
	}
	void add(List<RollCategory> availableCategories, float score) {
		int idx=getIdx(rollCategories,availableCategories);
		expectedScore[idx]=score;
	}
	protected float getExpectedScore(int idx) {
		return expectedScore[idx];
	}
//----------------------------------------------------------
	public void save(String path) throws IOException {
		FileOutputStream fileStream = new FileOutputStream(path);
		DataOutputStream dataStream = new DataOutputStream(fileStream);
		try {
			for (int i=0;i<8192;i++) {
				dataStream.writeFloat(expectedScore[i]);
			}
		} catch (NullPointerException e) {
			//failed
		} finally {
			fileStream.close();
		}
	}
	public void load(String path) throws IOException {
		FileInputStream fileStream=new FileInputStream(path);
		DataInputStream dataStream = new DataInputStream(fileStream);
		try {
			for (int i=0;i<8192;i++) {
				expectedScore[i]=dataStream.readFloat();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			fileStream.close();
		}
	}

//----------------------------------------------------------

	public float getExpectedScore(List<RollCategory> categories, RollCategory categoryToExclude) {
		int idx=getIdx(categories)-getIdx(categoryToExclude);
		return getExpectedScore(idx);
	}

	public float getExpectedScore(List<RollCategory> categories) {
		int idx=getIdx(categories);
		return getExpectedScore(idx);
	}

}
