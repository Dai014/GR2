package com.rabiloo.custom.utils.import_file.processor;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Predicate;

public interface FileImportProcessor {

    List<String[]> readListLineValueSkippingHeader(@NotNull MultipartFile importFile,
                                                   int numberColumnWantToSelect,
                                                   Predicate<String[]> breakPredicate,
                                                   Predicate<String[]> ignorePredicate);
}
