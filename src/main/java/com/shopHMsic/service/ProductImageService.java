package com.shopHMsic.service;

import org.springframework.stereotype.Service;

import com.shopHMsic.entities.ProductImage;

@Service
public class ProductImageService extends BaseService<ProductImage>{

	@Override
	protected Class<ProductImage> clazz() {
		// TODO Auto-generated method stub
		return ProductImage.class;
	}

}
