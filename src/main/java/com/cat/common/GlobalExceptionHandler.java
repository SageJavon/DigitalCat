package com.cat.common;

import com.cat.utils.SessionUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private SessionUtils sessionUtil;

    @ExceptionHandler(value = CommonException.class)
    public Result defaultErrorHandler(HttpServletRequest request, Exception e) {

//        SessionData sessionData = sessionUtil.getSessionData();
//        if(sessionData != null) {
//            log.error("user: " + JsonUtil.toJSONString(sessionData));
//        }

        Map<String, String> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //如果字段的值为空，判断若值为空，则删除这个字段>
//                if (null == res.get(en) || "".equals(res.get(en))) {
//                    res.remove(en);
//                }
            }
        }
        log.error("args: " + res.toString());
        log.error("e:" + e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        log.error("exception: " + sw.toString());


        if(e instanceof CommonException) {
            log.error("commonException: " + ((CommonException) e).getCommonErrorCode().getErrorReason() + "\n" + sw.toString());
            return Result.result(((CommonException) e).getCommonErrorCode(),((CommonException) e).getErrorMsg());
        }

        return Result.fail(sw.toString(), null);
    }

}