package org.ta.store.service.impln;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.ta.store.aspect.StoreServiceException;
import org.ta.store.dao.StoreDao;
import org.ta.store.dto.*;
import org.ta.store.service.StoreService;
import org.ta.store.util.FileService;

import java.util.List;

@Service
public class StoreServiceImpln implements StoreService {

    private static Logger log = LoggerFactory.getLogger(StoreServiceImpln.class);

    @Autowired
    StoreDao dao;

    /**
     * @param dto
     * @return
     * @throws StoreServiceException
     */
    @Override
    public ResponseDto createProduct(StoreDto dto) throws StoreServiceException {
        return dao.doCreate(dto);
    }

    /**
     * @param dto
     * @return
     * @throws StoreServiceException
     */
    @Override
    public StorePaginationResponse getProduct(StoreReqPageDto dto) throws StoreServiceException {

        Pageable page = null;
        int pageNo = dto.getPageNo() != null ? dto.getPageNo() : 0;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 100;
        String sortBy = dto.getSortBy()!=null? dto.getSortBy():"";
        String sortType = dto.getSortType()!=null? dto.getSortType():"";
        if(sortBy.isEmpty()) {
            System.out.println(pageNo+"#"+pageSize+"#");
            page = PageRequest.of(pageNo, pageSize);
        }else {
            page = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }
        return dao.getProduct(dto,page);
    }

    /**
     * @param countryCode
     * @return
     * @throws StoreServiceException
     */
    @Override
    public ResponseDto getStoreList(String countryCode) throws StoreServiceException {
        return dao.getStoreList(countryCode);
    }

    /**
     * @param dto
     * @return
     * @throws StoreServiceException
     */
    @Override
    public ResponseDto updateProduct(StoreDto dto) throws StoreServiceException {
        return dao.updateProduct(dto);
    }

    /**
     * @param dto
     * @return
     * @throws StoreServiceException
     */
    @Override
    public ResponseDto UploadProduct(FileUploadDto dto) throws StoreServiceException {

        try {
            log.debug("upload product#start");
            FileService fs = new FileService();
            List<StoreDto> sl = fs.csvToStoreDto(dto.getFile().getInputStream());
            return dao.uploadProduct(sl);
        }catch (Exception e) {
            log.error("UplaodProduct#"+ExceptionUtils.getStackTrace(e));
            if(e.getMessage().contains("Data Validation Failed"))
            {
                return new ResponseDto(false, HttpStatus.EXPECTATION_FAILED.value(), "Data Validation Failed");
            }
            log.error("Exception#UploadProduct#"+ ExceptionUtils.getStackTrace(e));
            throw new StoreServiceException(e);
        }

    }
}
