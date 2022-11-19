package com.rabiloo.custom.utils.export.processor;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

public class CSVExportProcessor<T> implements FileExportProcessor<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVExportProcessor.class);

    private CSVWriter prepareCSVResponse(HttpServletResponse response,
                                         String fileName,
                                         String charSetEncoding) throws IOException {
        response.setContentType("application/CSV");
        response.setCharacterEncoding(charSetEncoding);
        String headerForFile = "attachment; filename=\"" + generateFileNameWithTime(fileName) + ".csv\"";
        response.setHeader("Content-Disposition", headerForFile);
        response.setHeader("Content-type", "application/csv; charset=utf-8");

        // Tell excel read csv as UTF-8
        response.getWriter().write('\ufeff');
        return new CSVWriter(response.getWriter(), CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
    }

    @Override
    public void exportWithHeader(HttpServletResponse response,
                                 String[] lineHeader,
                                 Collection<T> listDataToWrite,
                                 Function<T, String[]> convertFunc,
                                 String fileName,
                                 String charSetEncoding) {
        try {
            CSVWriter csvWriter = prepareCSVResponse(response, fileName, charSetEncoding);
            csvWriter.writeNext(lineHeader);
            writeToCSV(csvWriter, listDataToWrite, convertFunc);
        } catch (IOException e) {
            LOGGER.error("ERROR when export csv : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    public void exportWithoutHeader(HttpServletResponse response,
                                    Collection<T> listDataToWrite,
                                    Function<T, String[]> convertFunc,
                                    String fileName,
                                    String charSetEncoding) {
        try {
            CSVWriter csvWriter = prepareCSVResponse(response, fileName, charSetEncoding);
            writeToCSV(csvWriter, listDataToWrite, convertFunc);
        } catch (IOException e) {
            LOGGER.error("ERROR when export csv : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void writeToCSV(CSVWriter csvWriter,
                            Collection<T> listDataToWrite,
                            Function<T, String[]> convertFunc) throws IOException {
        listDataToWrite
                .stream()
                .map(convertFunc)
                .forEach(csvWriter::writeNext);
        csvWriter.close();

    }
}
