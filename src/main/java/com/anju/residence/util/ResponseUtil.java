package com.anju.residence.util;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;
import com.anju.residence.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author cygao
 * @date 2021/1/5 22:49
 **/
@Slf4j
public class ResponseUtil {

  public static <T> void response(HttpServletResponse response, ResultVO<T> resultVO) throws IOException {

    response.setStatus(200);
    response.setCharacterEncoding("UTF-8");
    response.setContentType(ContentType.build(ContentType.JSON.toString(), StandardCharsets.UTF_8));

    String jsonBody = JSON.toJSONString(resultVO);

    PrintWriter writer = response.getWriter();
    writer.write(jsonBody);
    writer.flush();
    writer.close();
  }
}
