package com.shopHMsic.service;

import com.shopHMsic.entities.Saleorder;

public interface EmailService {
    void sendOrderConfirmation(Saleorder order);
}
