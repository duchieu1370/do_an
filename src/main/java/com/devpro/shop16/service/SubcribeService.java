package com.devpro.shop16.service;

import com.devpro.shop16.repository.CheckEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.devpro.shop16.dto.SubcribeSearchModel;

import com.devpro.shop16.entities.Subcribe;

@Service
public class SubcribeService extends BaseService<Subcribe> {



	@Override
	protected Class<Subcribe> clazz() {
		// TODO Auto-generated method stub
		return Subcribe.class;
	}

	public PagerData<Subcribe> search(SubcribeSearchModel searchModel) {

		// khởi tạo câu lệnh
		String sql = "SELECT * FROM tbl_subcribe p WHERE 1=1";

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
