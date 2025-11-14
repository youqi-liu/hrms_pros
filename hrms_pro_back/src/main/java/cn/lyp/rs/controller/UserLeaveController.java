package cn.lyp.rs.controller;

import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.basics.utils.SecurityUtil;
import cn.lyp.data.entity.User;
import cn.lyp.data.utils.LypNullUtils;
import cn.lyp.rs.entity.UserLeave;
import cn.lyp.rs.service.IUserLeaveService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(description = "员工请假管理接口")
@RequestMapping("/userLeave")
@Transactional
public class UserLeaveController {

    @Autowired
    private IUserLeaveService iUserLeaveService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<UserLeave> get(@PathVariable String id){

        UserLeave userLeave = iUserLeaveService.getById(id);
        return new ResultUtil<UserLeave>().setData(userLeave);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<UserLeave>> getAll(){

        List<UserLeave> list = iUserLeaveService.list();
        return new ResultUtil<List<UserLeave>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<UserLeave>> getByPage(@ModelAttribute UserLeave leave,@ModelAttribute PageVo page){
        QueryWrapper<UserLeave> qw = new QueryWrapper<>();
        if(!LypNullUtils.isNull(leave.getLeaveDate())) {
            qw.eq("leave_date",leave.getLeaveDate());
        }
        if(!LypNullUtils.isNull(leave.getLeaveReason())) {
            qw.eq("leave_reason",leave.getLeaveReason());
        }
        IPage<UserLeave> data = iUserLeaveService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<UserLeave>>().setData(data);
    }

    @RequestMapping(value = "/getMyByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<UserLeave>> getMyByPage(@ModelAttribute UserLeave leave,@ModelAttribute PageVo page){
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<UserLeave> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        if(!LypNullUtils.isNull(leave.getLeaveDate())) {
            qw.eq("leave_date",leave.getLeaveDate());
        }
        if(!LypNullUtils.isNull(leave.getLeaveReason())) {
            qw.eq("leave_reason",leave.getLeaveReason());
        }
        IPage<UserLeave> data = iUserLeaveService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<UserLeave>>().setData(data);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserLeave> insert(@RequestParam String date,@RequestParam String reason){
        User currUser = securityUtil.getCurrUser();
        UserLeave userLeave = new UserLeave();
        userLeave.setUserId(currUser.getId());
        userLeave.setUserName(currUser.getNickname());
        userLeave.setDateTime(getZwzNowTime());
        userLeave.setStatus("未审核");
        userLeave.setLeaveDate(date);
        userLeave.setLeaveReason(reason);
        userLeave.setAuditUser("");
        if(iUserLeaveService.saveOrUpdate(userLeave)){
            return new ResultUtil<UserLeave>().setData(userLeave);
        }
        return new ResultUtil<UserLeave>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserLeave> update(UserLeave userLeave){

        if(iUserLeaveService.saveOrUpdate(userLeave)){
            return new ResultUtil<UserLeave>().setData(userLeave);
        }
        return new ResultUtil<UserLeave>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){
        for(String id : ids){
            UserLeave userLeave = iUserLeaveService.getById(id);
            if(userLeave == null || userLeave.getStatus() == null || !userLeave.getStatus().equals("未审核")) {
                iUserLeaveService.removeById(id);
            }
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/auditByIds", method = RequestMethod.POST)
    @ApiOperation(value = "审核")
    public Result<Object> auditByIds(@RequestParam String[] ids){
        for(String id : ids){
            UserLeave userLeave = iUserLeaveService.getById(id);
            if(userLeave != null) {
                userLeave.setStatus("已审核");
                iUserLeaveService.saveOrUpdate(userLeave);
            }
        }
        return ResultUtil.success("OK");
    }

    @RequestMapping(value = "/notAuditByIds", method = RequestMethod.POST)
    @ApiOperation(value = "审核退回")
    public Result<Object> nowAuditByIds(@RequestParam String[] ids){
        for(String id : ids){
            UserLeave userLeave = iUserLeaveService.getById(id);
            if(userLeave != null) {
                userLeave.setStatus("未通过");
                iUserLeaveService.saveOrUpdate(userLeave);
            }
        }
        return ResultUtil.success("OK");
    }

    /**
     * 获取当前完整日期时间
     * @return
     */
    public static String getZwzNowTime() {
        Date date = new Date();
        int hour = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int date1 = date.getDate();
        return "" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (date1 < 10 ? "0" + date1 : date1)
                + " " + hour + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
}
