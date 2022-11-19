package com.rabiloo.custom.utils.import_file.processor;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.rabiloo.custom.utils.EncodingUtils;
import com.rabiloo.custom.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Service
public class CSVImportProcessor implements FileImportProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVImportProcessor.class);

    @Override
    public List<String[]> readListLineValueSkippingHeader(@NotNull MultipartFile csvFile,
                                                          int numberColumnWantToSelect,
                                                          Predicate<String[]> breakPredicate,
                                                          Predicate<String[]> ignorePredicate) {

        try {
            List<String[]> listResult = new ArrayList<>();
            String charsetStr = EncodingUtils.detectEncoding(csvFile.getInputStream());
            Charset charset;
            if (charsetStr == null) {
                charset = StandardCharsets.UTF_8;
            } else {
                charset = Charset.forName(charsetStr);
            }

            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream(), charset))
                    .withSkipLines(1)
                    .build();

            String[] lineValue;
            while ((lineValue = reader.readNext()) != null) {
                if (breakPredicate.test(lineValue)) {
                    break;
                }
                if (!ignorePredicate.test(lineValue)) {
                    String[] validLineValue = StringUtils.autoFillArrayIfNotFitSize(numberColumnWantToSelect, lineValue);
                    listResult.add(validLineValue);
                }
            }
            reader.close();
            return listResult;
        } catch (IOException e) {
            LOGGER.info("exception when reading csv file: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

}
