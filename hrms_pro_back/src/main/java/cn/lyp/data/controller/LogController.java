package cn.lyp.data.controller;

import cn.lyp.basics.log.LogType;
import cn.lyp.basics.log.SystemLog;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.data.entity.Log;
import cn.lyp.data.service.ILogService;
import cn.lyp.data.utils.LypNullUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "日志管理接口")
@RequestMapping("/log")
@Transactional
public class LogController{

    @Autowired
    private ILogService iLogService;

    @SystemLog(about = "查询日志", type = LogType.DATA_CENTER,doType = "LOG-01")
    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询日志")
    public Result<Object> getAllByPage(@ModelAttribute Log log, @ModelAttribute PageVo page){
        QueryWrapper<Log> qw = new QueryWrapper<>();
        if(!LypNullUtils.isNull(log.getName())) {
            qw.like("name",log.getName());
        }
        if(log.getLogType() != null) {
            qw.eq("log_type",log.getLogType());
        }
        if(!LypNullUtils.isNull(log.getUsername())) {
            qw.like("username",log.getUsername());
        }
        if(!LypNullUtils.isNull(log.getIp())) {
            qw.like("ip",log.getIp());
        }
        if(!LypNullUtils.isNull(log.getStartDate())) {
            qw.ge("create_time",log.getStartDate());
            qw.le("create_time",log.getEndDate());
        }
        return ResultUtil.data(iLogService.page(PageUtil.initMpPage(page),qw));
    }
}
