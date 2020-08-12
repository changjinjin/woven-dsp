package com.info.baymax.dsp.access.dataapi;

import java.util.List;

import com.google.common.base.Splitter;

public class GugvaTest {

	public static void main(String[] args) {
		List<String> splitToList = Splitter.on(",").splitToList("1,2,3,4,5,6,7,8,9");
		System.out.println(splitToList);
	}

}
