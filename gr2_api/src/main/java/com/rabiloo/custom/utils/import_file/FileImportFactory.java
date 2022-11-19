package com.rabiloo.custom.utils.import_file;

import com.rabiloo.custom.utils.import_file.processor.CSVImportProcessor;
import com.rabiloo.custom.utils.import_file.processor.FileImportProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class FileImportFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileImportFactory.class);

    public static Optional<FileImportProcessor> create(MultipartFile importFile) {
        String fileName = importFile.getOriginalFilename();
        if (fileName == null) {
            LOGGER.error("file name for importing is null");
            return Optional.empty();
        }

        if (fileName.endsWith(".csv")) {
            return Optional.of(new CSVImportProcessor());
        }
//        if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
//            return Optional.of(new ExcelImportProcessor());
//        }

        LOGGER.error("invalid file type for importing");
        return Optional.empty();
    }
}
