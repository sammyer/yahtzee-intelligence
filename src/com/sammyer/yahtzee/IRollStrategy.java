package com.sammyer.yahtzee;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 08/04/15
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IRollStrategy {
	public List<RollCategory> getAvailableCategories();
	public String getCategoryNames();

	//expected score before first roll
	public float getExpectedScore();
	public int getDiceToKeepFirstRoll(int dice);
	//get dice to keep after second roll
	public int getDiceToKeepSecondRoll(int dice);
	//get category to choose after third roll
	public RollCategory getBestCategory(int dice);

}
