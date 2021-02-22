package com.anju.residence.controller;

import com.anju.residence.annotation.AnonymousAccess;
import com.anju.residence.dto.OcrResult;
import com.anju.residence.entity.water.WaterMeter;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.water.WaterMeterService;
import com.anju.residence.service.water.WaterRecordLogService;
import com.anju.residence.util.FileUtil;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cygao
 * @date 2021/2/22 1:12 下午
 **/
@Slf4j
@RestController
@RequestMapping(value = "api/v1/upload")
public class UploadController {

  private final WaterMeterService waterMeterService;
  private final WaterRecordLogService waterRecordLogService;
  private final RestTemplate restTemplate = new RestTemplate();

  @Autowired
  public UploadController(WaterMeterService waterMeterService, WaterRecordLogService waterRecordLogService) {
    this.waterMeterService = waterMeterService;
    this.waterRecordLogService = waterRecordLogService;
  }

  @AnonymousAccess
  @PostMapping(value = "/image/{waterMeterId}")
  public ResultVO<String> uploadImage(@RequestParam(value = "file", required = false) MultipartFile file,
                                      @PathVariable Integer waterMeterId) {
    if (file == null) {
      throw new ApiException(ResultCode.FILE_IS_NULL);
    }

    WaterMeter waterMeter = waterMeterService.getById(waterMeterId).orElseThrow(() -> new ApiException(ResultCode.WATER_METER_ID_NOT_EXISTS));

    String fileName = file.getOriginalFilename();
    if (fileName == null) {
      throw new ApiException(ResultCode.FILE_NAME_IS_NULL);
    }
    fileName = System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));

    String filePath = FileUtil.WATER_IMAGE_LOCATION + '/' + waterMeterId + "/" + fileName;
    log.info(filePath);
    log.info(fileName);
    File newFile = new File(filePath);
    if (!newFile.getParentFile().exists()) {
      boolean success = newFile.getParentFile().mkdirs();
      log.info(String.valueOf(success));
    }
    try {
      file.transferTo(newFile);
    } catch (IOException e) {
      throw new ApiException(ResultCode.TRANSFER_FAILED);
    }

    // 请求参数
    Map<String, Object> variables = new HashMap<>(3);
    variables.put("userID", waterMeter.getUser().getId());
    variables.put("waterMeterId", waterMeterId);
    variables.put("path", filePath);

    ResponseEntity<OcrResult> result = restTemplate.postForEntity(FileUtil.OCR_URL, null, OcrResult.class, variables);
    if (result.getStatusCode() != HttpStatus.OK || result.getBody() == null) {
      throw new ApiException(ResultCode.OCR_ERROR.getCode(), String.valueOf(result.getStatusCodeValue()));
    }

    waterRecordLogService.addOcrResult(result.getBody(), waterMeterId);

    return new ResultVO<>("success");
  }

}
