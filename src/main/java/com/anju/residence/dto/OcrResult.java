package com.anju.residence.dto;

import com.anju.residence.entity.Ocr;
import com.anju.residence.entity.water.WaterMeter;
import lombok.Data;

import java.util.Arrays;

/**
 * @author cygao
 * @date 2021/2/22 2:04 下午
 **/
@Data
public class OcrResult {

  /**
   * 数字图像处理是否正常寻找到指针指向，寻不到False，此时数字识别读数后三位有的为0，表示不准确返回。示例：True or False
   */
  private String findCircle;

  /**
   * 处理后图片保存路径
   */
  private String saveFilePath;

  /**
   * 数据列表，水表最终识别读数
   */
  private String[] resultNum;

  /**
   * 带字母读数
   */
  private String[] resultAlpha;

  /**
   * 检测模型是否检测到数字区域目标，示例：True or False
   */
  private String useDetect;

  /**
   * 模型预测置信度
   */
  private String[] scores;

  /**
   * 原始图片存储参数
   */
  private String originalPath;

  public Ocr buildOcr(Integer waterMeterId) {
    return Ocr.builder()
            .findCircle(findCircle)
            .saveFilePath(saveFilePath)
            .resultAlpha(Arrays.toString(resultAlpha))
            .resultNum(Arrays.toString(resultNum))
            .useDetect(useDetect)
            .scores(Arrays.toString(scores))
            .originalPath(originalPath)
            .waterMeter(WaterMeter.builder().id(waterMeterId).build())
            .build();
  }

}
