package com.antra.evaluation.reporting_system.listener;

import com.antra.evaluation.reporting_system.exception.CrudTypeNotFoundException;
import com.antra.evaluation.reporting_system.pojo.request.CrudRequest;
import com.antra.evaluation.reporting_system.pojo.request.CrudSNSRequest;
import com.antra.evaluation.reporting_system.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class ExcelCrudQueueListener {

    private static final Logger log = LoggerFactory.getLogger(ExcelCrudQueueListener.class);

    private final ExcelService excelService;

    public ExcelCrudQueueListener(ExcelService excelService) {
        this.excelService = excelService;
    }

    @SqsListener("${app.aws.crud.excel.queue.name}")
    public void listen(CrudSNSRequest snsRequest) throws CrudTypeNotFoundException, FileNotFoundException {
        log.info("Got Crud snsRequest: {}", snsRequest);
        CrudRequest request = snsRequest.getCrudRequest();
        if (request.getCrudType() == null) {
            throw new CrudTypeNotFoundException();
        } else if (request.getCrudType().equals("DELETE")) {
            String fileId = request.getExcelFileId();
            if (fileId != null) {
                excelService.deleteFile(fileId);
            } else {
                throw new FileNotFoundException();
            }
            log.info("File with fileId: {} has been deleted!", fileId);
        }
    }
}
