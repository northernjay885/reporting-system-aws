package com.antra.ClientServiceSync.service;

import com.antra.ClientServiceSync.pojo.FileType;
import com.antra.ClientServiceSync.pojo.reponse.ReportVO;
import com.antra.ClientServiceSync.pojo.reponse.SqsResponse;
import com.antra.ClientServiceSync.pojo.request.ReportRequest;

import java.io.InputStream;
import java.util.List;

public interface ReportService {
    ReportVO generateReportsSync(ReportRequest request);

    void updateAsyncPDFReport(SqsResponse response);

    void updateAsyncExcelReport(SqsResponse response);

    List<ReportVO> getReportList();

    InputStream getFileBodyByReqId(String reqId, FileType type);

}
