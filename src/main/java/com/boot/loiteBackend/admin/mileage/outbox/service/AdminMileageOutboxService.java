package com.boot.loiteBackend.admin.mileage.outbox.service;

public interface AdminMileageOutboxService {
    void retryOutboxEvent(Long id);
}