package cn.lyp.rs.controller;

import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.basics.utils.SecurityUtil;
import cn.lyp.data.entity.User;
import cn.lyp.data.utils.LypNullUtils;
import cn.lyp.rs.entity.UserNotice;
import cn.lyp.rs.service.IUserNoticeService;
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
@Api(description = "公告管理接口")
@RequestMapping("/userNotice")
@Transactional
public class UserNoticeController {

    @Autowired
    private IUserNoticeService iUserNoticeService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<UserNotice> get(@PathVariable String id){
        UserNotice userNotice = iUserNoticeService.getById(id);
        return new ResultUtil<UserNotice>().setData(userNotice);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<UserNotice>> getAll(){
        List<UserNotice> list = iUserNoticeService.list();
        return new ResultUtil<List<UserNotice>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<UserNotice>> getByPage(@ModelAttribute UserNotice notice,@ModelAttribute PageVo page){
        QueryWrapper<UserNotice> qw = new QueryWrapper<>();
        if(!LypNullUtils.isNull(notice.getTitle())) {
            qw.like("title",notice.getTitle());
        }
        if(!LypNullUtils.isNull(notice.getSendTime())) {
            qw.like("send_time",notice.getSendTime());
        }
        IPage<UserNotice> data = iUserNoticeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<UserNotice>>().setData(data);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserNotice> insert(@RequestParam String title,@RequestParam String content){
        if(LypNullUtils.isNull(title)) {
            return new ResultUtil<UserNotice>().setErrorMsg("公告标题不能为空");
        }
        if(LypNullUtils.isNull(content)) {
            return new ResultUtil<UserNotice>().setErrorMsg("公告内容不能为空");
        }
        User currUser = securityUtil.getCurrUser();
        UserNotice userNotice = new UserNotice();
        userNotice.setPushId(currUser.getId());
        userNotice.setPushName(currUser.getNickname());
        userNotice.setSendTime(getZwzNowTime());
        userNotice.setTitle(title);
        userNotice.setContent(content);
        if(iUserNoticeService.saveOrUpdate(userNotice)){
            return new ResultUtil<UserNotice>().setData(userNotice);
        }
        return new ResultUtil<UserNotice>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<UserNotice> update(UserNotice userNotice){
        if(iUserNoticeService.saveOrUpdate(userNotice)){
            return new ResultUtil<UserNotice>().setData(userNotice);
        }
        return new ResultUtil<UserNotice>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids){
        for(String id : ids){
            iUserNoticeService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
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
