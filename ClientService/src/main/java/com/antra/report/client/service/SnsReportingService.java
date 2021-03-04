package com.antra.report.client.service;

import com.antra.report.client.pojo.request.ReportRequest;

public interface SnsReportingService {
    void sendReportNotification(ReportRequest request);
}
