package com.devpro.shop16.service.paginate;

import org.springframework.stereotype.Service;

import com.devpro.shop16.service.PagerData;

@Service
public interface IPaginatesService {
	public PagerData GetInfoPaginates(int totalItems, int limit, int currentPage);

}
