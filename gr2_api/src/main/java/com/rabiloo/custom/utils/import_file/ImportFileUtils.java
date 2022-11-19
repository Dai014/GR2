package com.rabiloo.custom.utils.import_file;

import com.rabiloo.custom.utils.import_file.processor.FileImportProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ImportFileUtils {

    private ImportFileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<String[]> fromFileImportWithoutIgnoreAndBreak(MultipartFile cityImportFile,
                                                                     int numberColumnWantToSelect) {
        Optional<FileImportProcessor> fileImportProcessorOptional = FileImportFactory.create(cityImportFile);
        if (fileImportProcessorOptional.isEmpty()) {
            return Collections.emptyList();
        }
        //not ignore and break when read file
        Predicate<String[]> ignorePredicate = strings -> false;
        Predicate<String[]> breakPredicate = strings -> false;
        return fileImportProcessorOptional
                .get()
                .readListLineValueSkippingHeader(cityImportFile,
                        numberColumnWantToSelect,
                        breakPredicate,
                        ignorePredicate);
    }

    public static List<String[]> fromFileImportWithoutBreak(MultipartFile cityImportFile,
                                                            int numberColumnWantToSelect,
                                                            Predicate<String[]> ignorePredicate) {
        Optional<FileImportProcessor> fileImportProcessorOptional = FileImportFactory.create(cityImportFile);
        if (fileImportProcessorOptional.isEmpty()) {
            return Collections.emptyList();
        } else {
            Predicate<String[]> breakPredicate = strings -> false;
            return fileImportProcessorOptional
                    .get()
                    .readListLineValueSkippingHeader(cityImportFile,
                            numberColumnWantToSelect,
                            breakPredicate,
                            ignorePredicate);
        }
    }

}
