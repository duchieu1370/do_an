package com.devpro.shop16.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.devpro.shop16.dto.OrderSearchModel;
import com.devpro.shop16.dto.SubcribeSearchModel;
import com.devpro.shop16.entities.Saleorder;
import com.devpro.shop16.entities.Subcribe;

@Service
public class SaleorderService extends BaseService<Saleorder>{

	@Override
	protected Class<Saleorder> clazz() {
		// TODO Auto-generated method stub
		return Saleorder.class;
	}

	public PagerData<Saleorder> search(OrderSearchModel searchModel) {

		// khởi tạo câu lệnh
		String sql = "SELECT * FROM tbl_saleorder p WHERE 1=1";

		if (searchModel != null) {

			// tim kiem san pham theo seachText
			if (!StringUtils.isEmpty(searchModel.keyword)) {
				sql += " and (p.email like '%" + searchModel.keyword + "%')";
			}
		}

		// chi lay san pham chua xoa
//				sql += " and p.status=1 ";
		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());

	}
}
