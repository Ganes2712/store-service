package org.ta.store.service;

import org.springframework.data.domain.Pageable;
import org.ta.store.aspect.StoreServiceException;
import org.ta.store.dto.*;

public interface StoreService {

    ResponseDto createProduct(StoreDto dto) throws StoreServiceException;

    StorePaginationResponse getProduct(StoreReqPageDto dto)throws StoreServiceException;

    ResponseDto getStoreList(String countryCode) throws StoreServiceException;

    ResponseDto updateProduct(StoreDto dto) throws StoreServiceException;

    ResponseDto UploadProduct(FileUploadDto dto)throws StoreServiceException;
}
