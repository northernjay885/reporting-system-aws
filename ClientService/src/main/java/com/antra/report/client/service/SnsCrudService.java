package com.antra.report.client.service;

import com.antra.report.client.pojo.request.CrudRequest;

public interface SnsCrudService {
    void sendCrudNotification(CrudRequest crudRequest);
}
