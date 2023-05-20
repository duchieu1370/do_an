package com.devpro.shop16.service.paginate;

import org.springframework.stereotype.Service;

import com.devpro.shop16.service.PagerData;

@Service
public class PaginateServiceImpl {
	public PagerData GetInfoPaginates(int totalData, int limit, int currentPage) {
	PagerData paginate = new PagerData();
	paginate.setCurrentPage(CheckCurrentPage(currentPage, paginate.getCurrentPage()));
	paginate.setLimit(limit);
		
	paginate.setTotalItems(SetInfoTotalItems(totalData, limit));
	
	int start = FindStart(paginate.getCurrentPage(), limit);
	paginate.setStart(start);
	int end = FindEnd(paginate.getStart(), limit, totalData);
	paginate.setEnd(end);
	return paginate;
	}

	private int FindEnd(int start, int limit, int totalData) {
		// TODO Auto-generated method stub
		return start + limit > totalData ?  totalData : (start + limit) - 1;
	}

	private int FindStart(int currentPage, int limit) {
		// TODO Auto-generated method stub
		return ((currentPage - 1 ) * limit ) + 1;
	}

	private int SetInfoTotalItems(int totalData, int limit) {
		int totalItems = 0;
		totalItems = totalData / limit;
		totalItems = totalItems * limit < totalData ? totalItems + 1 : totalItems;
		return totalItems;
	}
	
	public int CheckCurrentPage(int currentPage, int totalItems) {
		if (currentPage < 1) {
			return 1;
		}
		if(currentPage > totalItems) {
			return totalItems;
		}
		return currentPage;
	}
}
