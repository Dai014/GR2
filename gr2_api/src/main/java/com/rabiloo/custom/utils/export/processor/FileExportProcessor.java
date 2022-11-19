package com.rabiloo.custom.utils.export.processor;

import com.rabiloo.custom.utils.DateUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

public interface FileExportProcessor<T> {

    void exportWithHeader(HttpServletResponse response,
                          String[] lineHeader,
                          Collection<T> listDataToWrite,
                          Function<T, String[]> convertFunc,
                          String fileName,
                          String charSetEncoding);

    void exportWithoutHeader(HttpServletResponse response,
                             Collection<T> listDataToWrite,
                             Function<T, String[]> convertFunc,
                             String fileName,
                             String charSetEncoding);

    default String generateFileNameWithTime(String fileName) {
        return fileName + DateUtils.getDateFormatted(new Date(), "yyyyMMddHHmm");
    }

}
