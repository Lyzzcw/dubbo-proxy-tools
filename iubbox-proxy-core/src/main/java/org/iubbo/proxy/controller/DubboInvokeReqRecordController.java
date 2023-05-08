package org.iubbo.proxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.iubbo.proxy.common.utils.TokenKeyUtil;
import org.iubbo.proxy.common.wrapper.PageResult;
import org.iubbo.proxy.model.dto.DubboInvokeReqRecordDTO;
import org.iubbo.proxy.model.dto.DubboInvokeRespRecordDTO;
import org.iubbo.proxy.model.dto.ResponseDTO;
import org.iubbo.proxy.model.dto.ResponseUtil;
import org.iubbo.proxy.model.po.LoginStatusPO;
import org.iubbo.proxy.model.po.UserPO;
import org.iubbo.proxy.service.DubboInvokeReqRecordService;
import org.iubbo.proxy.service.LoginStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author idea
 * @version V1.0
 * @date 2020/3/1
 */
@RestController
@Slf4j
@RequestMapping(value = "/dubbo-invoke-req-record")
public class DubboInvokeReqRecordController {

    @Autowired
    private DubboInvokeReqRecordService dubboInvokeReqRecordService;

    @Resource
    private LoginStatusService loginStatusService;


    @PostMapping(value = "/select-in-page")
    public ResponseDTO<PageResult<DubboInvokeRespRecordDTO>> selectInPage(@RequestBody DubboInvokeReqRecordDTO dubboInvokeReqRecordDTO) {
        log.info("【selectInPage】 req args is {}", dubboInvokeReqRecordDTO);
        String token = TokenKeyUtil.buildToken(dubboInvokeReqRecordDTO.getUserToken());
        LoginStatusPO loginStatusPO = loginStatusService.getByUserToken(token);
        if (loginStatusPO == null) {
            return ResponseUtil.unauthorized();
        }
        PageResult<DubboInvokeRespRecordDTO> resultList = dubboInvokeReqRecordService.selectDubboInokeParam(loginStatusPO.getUserId(), dubboInvokeReqRecordDTO.getPage(), dubboInvokeReqRecordDTO.getPageSize());
        return ResponseUtil.successResponse(resultList);
    }

    @PostMapping(value = "/save-one")
    public ResponseDTO<Boolean> saveOne(@RequestBody DubboInvokeReqRecordDTO dubboInvokeReqRecordDTO) {
        log.info("【saveOne】 req args is {}", dubboInvokeReqRecordDTO);
        String token = TokenKeyUtil.buildToken(dubboInvokeReqRecordDTO.getUserToken());
        LoginStatusPO loginStatusPO = loginStatusService.getByUserToken(token);
        if (loginStatusPO == null) {
            return ResponseUtil.unauthorized();
        } else {
            dubboInvokeReqRecordDTO.setUserId(loginStatusPO.getUserId());
        }
        Boolean result = dubboInvokeReqRecordService.saveOne(dubboInvokeReqRecordDTO);
        return ResponseUtil.successResponse(result);
    }

}
