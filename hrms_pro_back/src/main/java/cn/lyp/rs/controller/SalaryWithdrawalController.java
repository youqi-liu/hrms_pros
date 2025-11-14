package cn.lyp.rs.controller;

import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.basics.utils.SecurityUtil;
import cn.lyp.data.entity.User;
import cn.lyp.data.service.IUserService;
import cn.lyp.rs.entity.SalaryWithdrawal;
import cn.lyp.rs.service.ISalaryWithdrawalService;
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
@Api(description = "工资提现管理接口")
@RequestMapping("/salaryWithdrawal")
@Transactional
public class SalaryWithdrawalController {

    @Autowired
    private ISalaryWithdrawalService iSalaryWithdrawalService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/getMyInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<User> getMyInfo(){
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id",currUser.getId());
        return new ResultUtil<User>().setData(iUserService.getOne(qw));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<SalaryWithdrawal>> getAll(){
        List<SalaryWithdrawal> list = iSalaryWithdrawalService.list();
        return new ResultUtil<List<SalaryWithdrawal>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<SalaryWithdrawal>> getByPage(PageVo page){
        IPage<SalaryWithdrawal> data = iSalaryWithdrawalService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<SalaryWithdrawal>>().setData(data);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ApiOperation(value = "申请")
    public Result<Object> apply(){
        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        String nowMouth = year + "-" + (month < 10 ? "0" + month : month) + "-";
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<SalaryWithdrawal> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        qw.eq("mouth",nowMouth);
        List<SalaryWithdrawal> salaryWithdrawalList = iSalaryWithdrawalService.list(qw);
        if(salaryWithdrawalList.size() > 0) {
            return ResultUtil.error("您已申请,无需重复申请");
        }
        SalaryWithdrawal salaryWithdrawal = new SalaryWithdrawal();
        salaryWithdrawal.setSalaryTime(getZwzNowTime());
        salaryWithdrawal.setMouth(nowMouth);
        salaryWithdrawal.setAuditName("");
        salaryWithdrawal.setUserName(currUser.getNickname());
        salaryWithdrawal.setUserId(currUser.getId());
        salaryWithdrawal.setStatus(0);
        salaryWithdrawal.setAuditTime("-");
        salaryWithdrawal.setMoneyData(currUser.getMoneyData());
        iSalaryWithdrawalService.saveOrUpdate(salaryWithdrawal);
        return ResultUtil.success("OK");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ApiOperation(value = "审核")
    public Result<Object> audit(@RequestParam String id){
        User currUser = securityUtil.getCurrUser();
        SalaryWithdrawal sw = iSalaryWithdrawalService.getById(id);
        if(sw == null) {
            return ResultUtil.error("审核申请不存在");
        }
        if(sw.getStatus() > 0) {
            return ResultUtil.error("审核单已被审核,无需重复审核");
        }
        sw.setStatus(1);
        sw.setAuditTime(getZwzNowTime());
        sw.setAuditName(currUser.getNickname());
        iSalaryWithdrawalService.saveOrUpdate(sw);
        return ResultUtil.success("OK");
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ApiOperation(value = "查询审核状态")
    public Result<Integer> find(){
        Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        String nowMouth = year + "-" + (month < 10 ? "0" + month : month) + "-";
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<SalaryWithdrawal> qw = new QueryWrapper<>();
        qw.eq("user_id",currUser.getId());
        qw.eq("mouth",nowMouth);
        List<SalaryWithdrawal> salaryWithdrawalList = iSalaryWithdrawalService.list(qw);
        if(salaryWithdrawalList.size() > 0) {
            SalaryWithdrawal sw = salaryWithdrawalList.get(0);
            return new ResultUtil<Integer>().setData(sw.getStatus());
        }
        return new ResultUtil<Integer>().setData(2);
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
