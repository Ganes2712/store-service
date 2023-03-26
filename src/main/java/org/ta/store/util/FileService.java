package org.ta.store.util;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.ta.store.aspect.StoreServiceException;
import org.ta.store.dto.StoreDto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileService {

    private static Logger log = LoggerFactory.getLogger(FileService.class);

    public static String TYPE = "text/csv";

    static String[] HEADERs = { "countryCode","sid","sku","prodName","price"};

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        if (file.getSize()==0|| file.isEmpty()) {
            return false;
        }
        return true;
    }

    public static List<StoreDto> csvToStoreDto(InputStream is) throws StoreServiceException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<StoreDto> storeList = new ArrayList<StoreDto>();

            if(dataValidation(csvParser)) {

                Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                for (CSVRecord csvRecord : csvRecords) {
                    StoreDto store = new StoreDto();

                    store.setCountryCode(csvRecord.get("countryCode"));
                    store.setSid(Long.valueOf(csvRecord.get("sid")));
                    store.setSku(csvRecord.get("sku"));
                    store.setProdName(csvRecord.get("prodName"));
                    store.setPrice(Long.valueOf(csvRecord.get("price")));
                    store.setCreatedBy(1001l);

                    storeList.add(store);
                }
                log.info("storeList#"+storeList.size());

                if(storeList.size()==0)
                {
                    throw new StoreServiceException("Data Validation Failed");
                }
            }else{
                throw new StoreServiceException("Data Validation Failed");
            }
            return storeList;
        } catch (Exception e) {
            log.error("Exception#csvToDto#"+ ExceptionUtils.getStackTrace(e));
            throw new StoreServiceException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private static boolean dataValidation(CSVParser csvParser)
    {
        if(!headerNameCheck(csvParser)){
            return false;
        }
/*        if(!rowLenCheck(csvParser)){
            return false;
        }*/
        return true;
    }

    private static boolean rowLenCheck(CSVParser csvParser)
    {
        try {
            return (csvParser.getRecords().stream().count()>1l);
        }catch (Exception e) {

        }
        return false;
    }

    private static boolean headerNameCheck(CSVParser csvParser)
    {
        boolean isExist = false;
        List<String> headNameList = csvParser.getHeaderNames();
        List<String> csvNames = Arrays.asList(HEADERs);
        for (String s:csvNames)
        {
            if(headNameList.contains(s)){
                return true;
            }
        }
        return isExist;
    }




}
