package org.ta.store.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadDto {

    private MultipartFile file;

    private String userId;

}
