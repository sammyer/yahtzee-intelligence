package com.sammyer.yahtzee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 07/04/15
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CombinationIterator<T> implements Iterator<List<T>> {
	private List<T> items;
	private int numItems;
	private int subsetSize;
	private int[] idxs;
	private boolean mHasNext=true;

	public CombinationIterator(List<T> items, int subsetSize) {
		this.items = items;
		this.subsetSize = subsetSize;
		numItems=items.size();
		idxs=new int[subsetSize];
		for (int i=0;i<subsetSize;i++) idxs[i]=i;
	}

	@Override
	public boolean hasNext() {
		return mHasNext;
	}

	@Override
	public List<T> next() {
		if (!mHasNext) return null;
		List<T> subset=new ArrayList<T>(subsetSize);
		for (int i=0;i<subsetSize;i++) subset.add(items.get(idxs[i]));
		incrementIndex();
		return subset;
	}

	private void incrementIndex() {
		incrementIndex(idxs.length-1);
	}

	private void incrementIndex(int pos) {
		int maxValue=numItems-subsetSize+pos;
		if (idxs[pos]==maxValue) {
			if (pos==0) mHasNext=false;
			else {
				incrementIndex(pos-1);
				idxs[pos]=idxs[pos-1]+1;
			}
		} else idxs[pos]++;
	}

	@Override
	public void remove() {}
}
