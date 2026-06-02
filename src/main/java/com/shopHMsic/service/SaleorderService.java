package com.shopHMsic.service;

import com.shopHMsic.dto.OrderSearchModel;
import com.shopHMsic.entities.Saleorder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SaleorderService extends BaseService<Saleorder>{

	@Override
	protected Class<Saleorder> clazz() {
		// TODO Auto-generated method stub
		return Saleorder.class;
	}

	public PagerData<Saleorder> search(OrderSearchModel searchModel) {
		String sql = "SELECT * FROM tbl_saleorder p WHERE 1=1";

		if (searchModel != null) {
			if (!StringUtils.isEmpty(searchModel.keyword)) {
				sql += " and (p.customer_name like '%" + searchModel.keyword + "%'" + " or p.customer_email like '%"
						+ searchModel.keyword + "%'" + " or p.customer_phone like '%" + searchModel.keyword + "%'"
						+ " or p.customer_address like '%" + searchModel.keyword + "%')";
			}
		}
		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());

	}
}
