package org.ta.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.ta.store.aspect.StoreServiceException;
import org.ta.store.dto.*;
import org.ta.store.service.StoreService;

@RestController
@RequestMapping("store")
public class StoreController {
    private static Logger log = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    StoreService service;


    /**
     * @param dto
     * this is method is used for create product/store details
     * @return ResponseDto
     * @throws StoreServiceException
     */
    @PostMapping("/create")
    public ResponseDto createProduct(@RequestBody StoreDto dto) throws StoreServiceException {
        log.debug("createProduct#"+dto);
        return service.createProduct(dto);
    }


    /**
     * @param dto
     * Added pagination and sort functionality
     * if value is not in dto then default value used
     * @return StorePaginationResponse
     * @throws StoreServiceException
     */
    @PostMapping(value = "/getProduct",produces = MediaType.APPLICATION_JSON_VALUE)
    public StorePaginationResponse getProduct(@RequestBody StoreReqPageDto dto) throws StoreServiceException {
        log.debug("getProduct#"+dto);
        if(dto!=null) {
            return service.getProduct(dto);
        }else{
            throw new StoreServiceException("Request is Empty or Invalid");
        }
    }

    /**
     * @param countryCode
     * fetch product details based on country code
     * @return ResponseDto
     * @throws StoreServiceException
     */
    @GetMapping("/getStoreList")
    public ResponseDto getStoreIdList(@RequestParam String countryCode) throws StoreServiceException {
        log.debug("getStoreIdList#"+countryCode);
        if(countryCode!=null) {
            return service.getStoreList(countryCode);
        }else{
            throw new StoreServiceException("Request is Empty or Invalid");
        }
    }


    @PostMapping("/update")
    public ResponseDto updateProduct(@RequestBody StoreDto dto) throws StoreServiceException {
        log.debug("updateProduct#"+dto);
        if(dto!=null) {
            return service.updateProduct(dto);
        }else{
            throw new StoreServiceException("Request is Empty or Invalid");
        }
    }

    /**
     * @param dto
     * update upload functionality
     * dao layer implemented in JDBC batch process instead of JPA Data
     * @return ResponseDto
     * @throws StoreServiceException
     */
    @PostMapping("/upload")
    public ResponseDto UploadProduct(@ModelAttribute FileUploadDto dto) throws StoreServiceException {
        log.debug("UploadProduct#"+dto);
        if(dto!=null) {
            return service.UploadProduct(dto);
        }else{
            throw new StoreServiceException("Request is Empty or Invalid");
        }
    }
}
