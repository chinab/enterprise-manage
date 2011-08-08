package com.juduowang.utils;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

public class CollectionUtil {
	@SuppressWarnings("rawtypes")
	public static Object first(Collection c) {

		Object object = null;
		try {
			if (c.size() > 0)
				object = CollectionUtils.get(c, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}
}
