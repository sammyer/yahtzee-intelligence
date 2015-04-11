package com.sammyer.yahtzee;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 08/04/15
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class StrategyDatabaseEntry implements IRollStrategy {
	private List<RollCategory> availableCategories;
	private float expectedScore;
	private short[] keepFirstRollHashed;
	private short[] keepSecondRollHashed;
	private byte[] categoryToChooseIndex;

	private int arrayLen;
	private int hashOffset;
	private DiceRoll rolls;
	private List<RollCategory> rollCategories;

	public static int BYTE_SIZE=1264;

	private StrategyDatabaseEntry() {
		rolls=DiceRoll.getInstance();
		rollCategories=RollTypes.getRollCategories();
		hashOffset=rolls.startHashIdx(5);
		arrayLen=rolls.endHashIdx(5)-hashOffset;

		keepFirstRollHashed=new short[arrayLen];
		keepSecondRollHashed=new short[arrayLen];
		categoryToChooseIndex=new byte[arrayLen];
	}

	public StrategyDatabaseEntry(RollStrategy strategy) {
		this();

		availableCategories=strategy.getAvailableCategories();
		expectedScore=strategy.getExpectedScore();

		for (int i=0;i<arrayLen;i++) {
			int dice=rolls.unhashDice(i+hashOffset);
			keepFirstRollHashed[i]=(short)rolls.hashDice(strategy.getDiceToKeepFirstRoll(dice));
			keepSecondRollHashed[i]=(short)rolls.hashDice(strategy.getDiceToKeepSecondRoll(dice));
			categoryToChooseIndex[i]=(byte)rollCategories.indexOf(strategy.getBestCategory(dice));
		}
	}

	public StrategyDatabaseEntry(byte[] data, List<RollCategory> availableCategories) {
		this();
		this.availableCategories=availableCategories;
		setFromBytes(data);
	}

	@Override
	public List<RollCategory> getAvailableCategories() {
		return availableCategories;
	}

	@Override
	public String getCategoryNames() {
		return RollCategory.getCategoryNames(availableCategories);
	}

	@Override
	public float getExpectedScore() {
		return expectedScore;
	}

	private int diceToIdx(int dice) {
		return rolls.hashDice(dice)-hashOffset;
	}

	@Override
	public int getDiceToKeepFirstRoll(int dice) {
		return (int)rolls.unhashDice(keepFirstRollHashed[diceToIdx(dice)]);
	}

	@Override
	public int getDiceToKeepSecondRoll(int dice) {
		return (int)rolls.unhashDice(keepSecondRollHashed[diceToIdx(dice)]);
	}

	@Override
	public RollCategory getBestCategory(int dice) {
		int categoryId=(int)categoryToChooseIndex[diceToIdx(dice)];
		return rollCategories.get(categoryId);
	}

	//--------------------------------------------------------------

	public byte[] toBytes() {
		ByteBuffer buffer=ByteBuffer.allocate(BYTE_SIZE);
		buffer.putFloat(expectedScore);
		for (int i=0;i<arrayLen;i++) buffer.putShort(keepFirstRollHashed[i]);
		for (int i=0;i<arrayLen;i++) buffer.putShort(keepSecondRollHashed[i]);
		buffer.put(categoryToChooseIndex);
		return buffer.array();
	}

	private void setFromBytes(byte[] data) {
		ByteBuffer buffer=ByteBuffer.wrap(data);

		expectedScore=buffer.getFloat();
		for (int i=0;i<arrayLen;i++) keepFirstRollHashed[i]=buffer.getShort();
		for (int i=0;i<arrayLen;i++) keepSecondRollHashed[i]=buffer.getShort();
		buffer.get(categoryToChooseIndex);
	}
}
