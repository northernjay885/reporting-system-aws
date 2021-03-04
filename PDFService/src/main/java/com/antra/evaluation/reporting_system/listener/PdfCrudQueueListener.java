package com.antra.evaluation.reporting_system.listener;

import com.antra.evaluation.reporting_system.pojo.exception.CrudTypeNotFoundException;
import com.antra.evaluation.reporting_system.pojo.request.CrudRequest;
import com.antra.evaluation.reporting_system.pojo.request.CrudSNSRequest;
import com.antra.evaluation.reporting_system.service.PDFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class PdfCrudQueueListener {

    private static final Logger log = LoggerFactory.getLogger(PdfCrudQueueListener.class);

    private final PDFService pdfService;

    public PdfCrudQueueListener(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @SqsListener("${app.aws.crud.pdf.queue.name}")
    public void listen(CrudSNSRequest snsRequest) throws CrudTypeNotFoundException, FileNotFoundException {
        log.info("Get Crud snsRequest: {}", snsRequest);
        CrudRequest request = snsRequest.getCrudRequest();
        if (request.getCrudType() == null) {
            throw new CrudTypeNotFoundException();
        } else if (request.getCrudType().equals("DELETE")) {
            String fileId = request.getPdfFileId();
            pdfService.deleteFile(fileId);
            log.info("File with fileId: {} has been deleted!", fileId);
        }
    }
}
