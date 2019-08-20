package com.hzqing.cloud.log.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzqing.cloud.log.api.dto.AddLoginLogRequest;
import com.hzqing.cloud.log.api.dto.LoginLogDto;
import com.hzqing.cloud.log.api.service.ILoginLogService;
import com.hzqing.cloud.log.provider.converter.LoginLogConverter;
import com.hzqing.cloud.log.provider.dal.entity.LoginLog;
import com.hzqing.cloud.log.provider.dal.mapper.LoginLogMapper;
import com.hzqing.common.core.constants.GlobalConstants;
import com.hzqing.common.core.service.exception.ExceptionProcessUtils;
import com.hzqing.common.core.service.request.PageRequest;
import com.hzqing.common.core.service.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 登陆服务接口实现
 * @author hzqing
 * @date 2019-08-19 14:47
 */
@Slf4j
@Service(version = GlobalConstants.VERSION_V1)
public class LoginLogServiceImpl implements ILoginLogService {


    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private LoginLogConverter loginLogConverter;

    @Override
    public CommonResponse save(AddLoginLogRequest request) {
        log.info("LoginLogServiceImpl.save request: " + request);
        CommonResponse response = new CommonResponse();
        try {
            request.checkParams();
            LoginLog loginLog = loginLogConverter.req2LoginLog(request);
            int row = loginLogMapper.insert(loginLog);
            log.info("LoginLogServiceImpl.save effect row: " + row);
        }catch (Exception e){
            log.error("LoginLogServiceImpl.save occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse<Page<LoginLogDto>> page(PageRequest request) {
        log.info("LoginLogServiceImpl.page request: " + request);
        CommonResponse<Page<LoginLogDto>> response = new CommonResponse<>();
        try {
            IPage<LoginLog> logIPage = loginLogMapper.selectPage(new Page<LoginLog>(request.getPageNum(), request.getPageSize()), null);
            Page<LoginLogDto> logDtoPage = loginLogConverter.page2pageDto(logIPage);
            response.setData(logDtoPage);
        } catch (Exception e) {
            log.error("LoginLogServiceImpl.page occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }
}
