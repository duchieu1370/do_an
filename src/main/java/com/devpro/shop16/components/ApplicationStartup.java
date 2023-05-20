package com.devpro.shop16.components;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.devpro.shop16.entities.Categories;
import com.devpro.shop16.entities.Product;
import com.devpro.shop16.service.CategoriesService;
import com.github.slugify.Slugify;

@Component
public class ApplicationStartup  implements ApplicationListener<ApplicationReadyEvent>{
	
	@Autowired //inject 1 bean vào trong 1 bean khác
	private CategoriesService categoriesService;
	
	

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		// TODO Auto-generated method stub
		System.out.println("-------");
//		this.seedPythonCategory();
		System.out.println("-------");
	}
	
//	private void seedPythonCategory() {
//		//kiểm tra xem đã có python trong bảng tbl_category chưa?
//		Categories pythonCate = categoriesService.getOneByNativeSQL("SELECT * FROM tbl_category WHERE Name = 'Toy'");
//		
//		//nếu không có thì thêm mới
//		if(pythonCate == null) {//tạo mới
//			pythonCate = new Categories();
//			pythonCate.setName("Toy");
//			pythonCate.setDescription("Toy");
//			
//			//thêm ds sản phẩm cho python category
//			for(int i = 0; i < 20; i++) {
//				Product p = new Product();
//				p.setTitle("Toy Title" + i);
//				p.setShortDes("Toy ShortDes" + i);
//				p.setDetails("Toy Details" + i);
//				p.setPrice(new BigDecimal("1000"));
//				p.setCreatedDate(new Date());
//				
//				//gọi hàm ultility trong 1-N
//				pythonCate.addProduct(p);
//			}
//			//ngược lại chỉnh sửa ngày
//		} else {
//			pythonCate.setUpdatedDate(new Date());
//			pythonCate.setSeo(new Slugify().slugify("Gao") + System.currentTimeMillis());
//		}
//		
//		categoriesService.saveOrUpdate(pythonCate);
//	}

}
