package cn.structured.basic.core.filter;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.spring.SpringUtil;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structured.basic.api.interfaces.IManager;
import cn.structured.basic.api.model.BasicEntity;
import cn.structured.basic.api.model.ValueObject;
import cn.structured.basic.api.model.api.ApiDefinition;
import cn.structured.basic.core.manager.BasicManagerImpl;
import cn.structured.basic.core.manager.ExecuteServiceImpl;
import cn.structured.basic.core.manager.IExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由过滤器
 *
 * @author chuck
 * @since JDK1.8
 */
@Slf4j
@Component
@Order(-99)
@RequiredArgsConstructor
public class RouteFilter implements Filter, HandlerInterceptor {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Thread thread = Thread.currentThread();
        long threadId = thread.getId();
        String threadName = thread.getName();
        //当前处理的时间
        long beginTime = System.currentTimeMillis();
        //转换为HttpServlet 的实现
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //上下文类型 multipart/form-data ;application/x-www-form-urlencoded;application/json text/plain;application/javascript;text/html;application/xml;
        //application/x-msdownload
        String contentType = httpServletRequest.getContentType();
        //请求的方法
        String method = httpServletRequest.getMethod();
        //获取uri
        String uri = httpServletRequest.getRequestURI();
        //获取API
        log.info("请求方法 -> {},上下文 -> {} ,url -> {} ", method, contentType, uri);
        //构建入参
        MD5 md5 = SecureUtil.md5();
        String uriDigest = md5.digestHex(uri);
        IManager<BasicEntity> entityManager = SpringUtil.getBean(BasicManagerImpl.class);

        BasicEntity basicEntity = entityManager.findById(uriDigest);
        ApiDefinition apiDefinition = (ApiDefinition) basicEntity;
        //api定义不存在
        if (null == apiDefinition) {
            //不存在不处理
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //请求方法不对不处理
        if (!method.equals(apiDefinition.getMethod())) {
            //不存在不处理
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //请求头校验
        Map<String, String> headers = apiDefinition.getRequestHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String header = httpServletRequest.getHeader(entry.getKey());
            //处理器执行校验
            String value = entry.getValue();
            if (!header.equals(value)) {
                //这个请求头的参数不匹配return;
                chain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }
        StringBuilder requestBody = new StringBuilder();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        Map<String, Object> params = new HashMap<>();
        if ("GET".equalsIgnoreCase(method)) {
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String parameterValue = httpServletRequest.getParameter(parameterName);
                params.put(parameterName, parameterValue);
            }
            requestBody.append(JSON.toJSONString(params));
        } else {
            //请求参数校验
            if (contentType.equals("application/json")) {
                //构建参数
                BufferedReader bufferedReader = request.getReader();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    //参数类型校验，长度校验
                    requestBody.append(str);
                }
                log.info(requestBody.toString());
            }
        }

        String digestData = requestBody.toString() + uri + threadId + threadName + beginTime;
        //生成事务ID
        String transactionId = md5.digestHex(digestData);

        ValueObject inputObject = new ValueObject();
        inputObject.setTransactionId(transactionId);
        inputObject.setEntity(apiDefinition);
        inputObject.setValue(JSON.parseObject(requestBody.toString()));
        IExecuteService executeService = SpringUtil.getBean(ExecuteServiceImpl.class);
        //构建执行器
        Object execute = executeService.execute(inputObject);

        response.setContentType(apiDefinition.getResponseContentType());
        ServletOutputStream outputStream = response.getOutputStream();
        //如果响应结果是JSON
        if (apiDefinition.getResponseContentType().equals("application/json")) {
            outputStream.write(JSON.toJSONString(ResultUtilSimpleImpl.success(execute)).getBytes());
        }
        outputStream.close();
        long endTime = System.currentTimeMillis();
        log.info("处理结束 : 耗时 -> {}", endTime - beginTime);
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
