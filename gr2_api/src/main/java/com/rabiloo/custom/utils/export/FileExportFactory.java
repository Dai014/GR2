package com.rabiloo.custom.utils.export;

import com.rabiloo.custom.utils.export.processor.CSVExportProcessor;
import com.rabiloo.custom.utils.export.processor.FileExportProcessor;

public class FileExportFactory {

    public static <T> FileExportProcessor<T> getExportProcessor(TypeFileExport typeFileExport) {
        if (typeFileExport == TypeFileExport.CSV) {
            return new CSVExportProcessor<>();
        }
//        if (typeFileExport == TypeFileExport.EXCEL) {
//            return new ExcelExportProcessor<>();
//        }
        throw new IllegalArgumentException("Invalid typeFileImport");
    }
}
