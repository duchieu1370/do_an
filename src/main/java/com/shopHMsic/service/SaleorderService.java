package com.shopHMsic.service;

import com.shopHMsic.dto.OrderSearchModel;
import com.shopHMsic.entities.Saleorder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleorderService {
    Page<Saleorder> getListOrder(OrderSearchModel dto, Pageable pageable);
    
    Saleorder getById(int id);
    
    void updateOrderStatus(int id, Integer orderStatus, String reason) throws Exception;
    
    void deleteOrder(int id) throws Exception;

    // Compatibility methods for existing controllers
    Saleorder saveOrUpdate(Saleorder order);
    
    PagerData<Saleorder> search(OrderSearchModel searchModel);

}
