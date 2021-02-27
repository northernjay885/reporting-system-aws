package com.antra.evaluation.reporting_system;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class PDFTest {

    @Test
    public void findFile() throws FileNotFoundException {
        File f = new File("Coffee_Landscape.jasper");
        System.out.println(f.exists());

        File file = ResourceUtils.getFile("classpath:Coffee_Landscape.jasper");
        System.out.println(file.exists());
    }

    @Test
    public void pdf() {
//        String jasperFileName = "CL.jasper";
//        Map<String, Object> parameters = new HashMap<>();
//
//        parameters.put("content_str", "123");
//
//        List<Object> itemList = List.of("string");
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemList);
//
//        try {
//            JasperPrint jprint = JasperFillManager.fillReport(jasperFileName, parameters,dataSource);
//            File temp = new File("test.pdf");
//            JasperExportManager.exportReportToPdfFile(jprint, temp.getAbsolutePath());
//
//        } catch (JRException e) {
//            throw new PDFGenerationException();
//        }
    }
}
