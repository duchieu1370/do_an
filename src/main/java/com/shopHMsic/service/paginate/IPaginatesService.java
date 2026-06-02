package com.shopHMsic.service.paginate;

import org.springframework.stereotype.Service;

import com.shopHMsic.service.PagerData;

@Service
public interface IPaginatesService {
	public PagerData GetInfoPaginates(int totalItems, int limit, int currentPage);

}
