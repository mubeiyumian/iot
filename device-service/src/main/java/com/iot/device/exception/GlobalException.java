package com.iot.device.exception;

import com.iot.device.pojo.resp.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 02:42
 * @Version: 1.0
 */

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Response<String> handleBusinessException(Exception e) {
        log.error("业务异常: ", e);
        return Response.error(e.getMessage());
    }
}
