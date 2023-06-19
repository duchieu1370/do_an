package com.devpro.shop16.service;

import com.devpro.shop16.dto.OrderSearchModel;
import com.devpro.shop16.entities.Saleorder;
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
