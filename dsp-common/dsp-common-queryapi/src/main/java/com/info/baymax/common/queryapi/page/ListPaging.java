package com.info.baymax.common.queryapi.page;

import java.util.ArrayList;
import java.util.List;

public class ListPaging<T> {
	private final List<T> list;

	public ListPaging(List<T> list) {
		this.list = list;
	}

	public IPage<T> page(int pageNum, int pageSize) {
		if (list == null || list.isEmpty()) {
			return IPage.<T>of(pageNum, pageSize, list.size(), new ArrayList<T>());
		}
		pageSize = getPageSize(pageSize);
		int totalPage = getTotalPage(pageSize);
		pageNum = getPageNum(pageNum, totalPage);
		int fromIndex = (pageNum - 1) * pageSize;
		int toIndex = (pageNum - 1) * pageSize + pageSize;
		if (toIndex > list.size()) {
			toIndex = list.size();
		}
		return IPage.<T>of(pageNum, pageSize, list.size(), list.subList(fromIndex, toIndex));
	}

	public IPage<T> offset(int offset, int limit) {
		if (offset <= 0) {
			offset = 0;
		}
		limit = getPageSize(limit);
		return page(offset / limit, limit);
	}

	private int getPageNum(int pageNum, int totalPage) {
		if (pageNum <= 0) {
			pageNum = 1;
		}

		if (pageNum > totalPage) {
			pageNum = totalPage;
		}
		return pageNum;
	}

	private int getPageSize(int pageSize) {
		if (pageSize <= 0) {
			pageSize = 10;
		}
		return pageSize;
	}

	private int getTotalPage(int pageSize) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		return (int) Math.ceil((double) list.size() / getPageSize(pageSize));
	}

	// public static void main(String[] args) {
	// List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
	// 22, 23, 24, 25, 26, 27, 28, 29, 30);
	// ListPaging<Integer> listPaging = new ListPaging<Integer>(list);
	// IPage<Integer> page = listPaging.page(4, 10);
	// System.out.println(page.getList());
	// IPage<Integer> page = listPaging.offset(0, 10);
	// System.out.println(page.getList());
	// }

}
