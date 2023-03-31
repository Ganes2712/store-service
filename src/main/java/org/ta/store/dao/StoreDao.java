package org.ta.store.dao;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.ta.store.aspect.StoreServiceException;
import org.ta.store.dto.ResponseDto;
import org.ta.store.dto.StoreDto;
import org.ta.store.dto.StorePaginationResponse;
import org.ta.store.dto.StoreReqPageDto;
import org.ta.store.entity.LinkCountryStore;
import org.ta.store.entity.Store;
import org.ta.store.repo.LinkCountryStoreRepo;
import org.ta.store.repo.StoreRepo;
import org.ta.store.util.ProdSpecification;
import org.ta.store.util.SearchCriteria;
import org.ta.store.util.SearchOperation;

import javax.sql.DataSource;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class StoreDao {
    private static Logger log = LoggerFactory.getLogger(StoreDao.class);

    private static final String Query="insert into ta_store" +
            "(sid,country_code,sku,prod_name,price,created_by,created_date)" +
            "values(?,?,?,?,?,?,?)";

    @Autowired
    StoreRepo repo;

    @Autowired
    LinkCountryStoreRepo linkRepo;

    @Autowired
    DataSource datasource;

    /**
     * @param dto
     * @return
     * @throws StoreServiceException
     */
    public ResponseDto doCreate(StoreDto dto)throws StoreServiceException
    {
        Store s = covertStoreDtoToEntity(dto);
        Store resStore = repo.save(s);
        ResponseDto res = new ResponseDto();
        res.setCode(HttpStatus.OK.value());
        res.setData("Data Saved Successful# RefId:"+resStore.getId());
        res.setStatus(true);
        return res;
    }

    private Store covertStoreDtoToEntity(StoreDto dto)
    {
        Store s = new Store();
        s.setSid(dto.getSid());
        s.setSku(dto.getSku());
        s.setCountryCode(dto.getCountryCode());
        s.setCreatedDate(LocalDateTime.now());
        s.setCreatedBy(dto.getCreatedBy());
        s.setPrice(dto.getPrice());
        s.setProdName(dto.getProdName());

        return s;
    }

    /**
     * @param dto
     * @param page
     * dynamic query form implemented
     * based on query condition
     * return the list of product details
     * @return StorePaginationResponse
     * @throws StoreServiceException
     */
    public StorePaginationResponse getProduct(StoreReqPageDto dto, Pageable page) throws StoreServiceException {
        try {
         //   List<String> kvoList = dto.getKvo();
            String kvo = dto.getKvo();
            ProdSpecification ps = new ProdSpecification();
            /*for (String kvo:kvoList)
            {
                String kvoArr[] = kvo.split("~");
              // ps.add(new SearchCriteria(kvoArr[0], kvoArr[1], SearchOperation.valueOf(kvoArr[2])));
            }*/
            String kvoArr[] = kvo.split("~");
            ps.add(new SearchCriteria(kvoArr[0], kvoArr[1], SearchOperation.valueOf(kvoArr[2])));
            Page<Store> pageData = repo.findAll(ps,page);
            StorePaginationResponse response = new StorePaginationResponse();
            response.setTotalElements(pageData.getTotalElements());
            response.setProductList(pageData.getContent().stream().map(this::getDtoFromEntity).collect(Collectors.toList()));
            return response;
        }catch (Exception e) {
            log.error("Exception#UploadProduct#"+ ExceptionUtils.getStackTrace(e));
            throw new StoreServiceException(e);
        }

    }

    private StoreDto getDtoFromEntity(Store dto)
    {
        StoreDto s = new StoreDto();
        s.setId(dto.getId());
        s.setCountryCode(dto.getCountryCode());
        s.setSid(dto.getSid());
        s.setSku(dto.getSku());
        s.setDate(dto.getCreatedDate());
        s.setCreatedBy(dto.getCreatedBy());
        s.setPrice(dto.getPrice());
        s.setProdName(dto.getProdName());

 /*       if(dto.getStoreList()!=null){
           // s.setStoreIdList(dto.getStoreList().stream().map(e->e.getSid()).collect(Collectors.toSet()));
        }else {
         //   s.setStoreIdList(new HashSet<>());
        }*/
        return s;
    }

    public ResponseDto getStoreList(String countryCode) throws StoreServiceException{
        ResponseDto dto = new ResponseDto();
        Set<LinkCountryStore> storeList = linkRepo.findByCc(countryCode);

        if(storeList!=null){
            dto.setStatus(true);
            dto.setData(storeList.stream().map(e->e.getSid()).collect(Collectors.toSet()));
            dto.setCode(HttpStatus.OK.value());
        }else{
            dto.setStatus(false);
            dto.setData(new HashSet<>());
            dto.setCode(HttpStatus.OK.value());
        }
        return dto;
    }

    /**
     * @param storeDto
     * update price details
     * @return ResponseDto
     */
    public ResponseDto updateProduct(StoreDto storeDto)
    {
        ResponseDto dto = new ResponseDto();
        Optional<Store> opData = repo.findById(storeDto.getId());

        if(opData!=null && opData.isPresent())
        {
            Store s = opData.get();

/*            s.setSid(storeDto.getSid());
            s.setCountryCode(storeDto.getCountryCode());
            s.setSku(storeDto.getSku());
            s.setProdName(storeDto.getProdName());*/
            s.setPrice(storeDto.getPrice()); // update only price
            s.setCreatedBy(storeDto.getCreatedBy());
            s.setCreatedDate(LocalDateTime.now());

            repo.save(s);

            dto.setStatus(true);
            dto.setData("Data Updated Successfully");
            dto.setCode(HttpStatus.OK.value());

        }else {
            dto.setStatus(false);
            dto.setData("Error in Process");
            dto.setCode(HttpStatus.OK.value());
        }

        return dto;
    }

    /**
     * @param sl
     * file process
     * upload product details
     * @return ResponseDto
     * @throws StoreServiceException
     */
    public ResponseDto uploadProduct(List<StoreDto> sl) throws Exception {
        ResponseDto dto = new ResponseDto();
        // this needs to be change in JDBC prepared stmt
        /*for (StoreDto s:sl) {
            Store e = covertStoreDtoToEntity(s);
            repo.save(e);
        }*/
        CopyOnWriteArrayList<StoreDto> cw = new CopyOnWriteArrayList(sl);
        try(Connection con = datasource.getConnection();
            PreparedStatement ps = con.prepareStatement(Query);)
        {
            int i=0;
                for (StoreDto s:cw)
                {
                    log.info("batch#"+s);
                    //sid,country_code,sku,prod_name,price,created_by,createdDate
                    ps.setLong(1,s.getSid());
                    ps.setString(2,s.getCountryCode());
                    ps.setString(3,s.getSku());
                    ps.setString(4,s.getProdName());
                    ps.setLong(5,s.getPrice());
                    ps.setLong(6,1001l);
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

                    ps.addBatch();

                    if(i==10)
                    {
                        i=0;
                        batchExecute(ps,cw);
                    }
                }
                i++;
            if(i!=0) {
                ps.executeBatch();
            }
        }catch(Exception e)
        {
            log.error("Exception#batch update#"+ExceptionUtils.getStackTrace(e));
            dto.setStatus(false);
            dto.setData("Error in File Process");
            dto.setCode(HttpStatus.OK.value());
        }

          dto.setStatus(true);
          dto.setData("File Uploaded Successfully");
          dto.setCode(HttpStatus.OK.value());

        return dto;
    }

    private void batchExecute(PreparedStatement ps ,CopyOnWriteArrayList<StoreDto> cw) throws Exception {
        try {
            int[] updateArr = ps.executeBatch();
        }
        catch(BatchUpdateException bue)
        {
            int[] updateCount = bue.getUpdateCounts();
            for (int j : updateCount)
            {
                if  (j == PreparedStatement.EXECUTE_FAILED) {
                    log.debug("Error on Batch " + cw.get(j) +": Execution failed");
                }
            }
            throw new Exception(bue);
        }
    }
}
