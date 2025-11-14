package cn.lyp.data.controller;

import cn.lyp.basics.log.LogType;
import cn.lyp.basics.log.SystemLog;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.data.entity.User;
import cn.lyp.data.service.IUserService;
import cn.lyp.data.utils.LypNullUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "Extended User APIs")
@RequestMapping("/users/extended")
@Transactional
public class MyUserController {

    @Autowired
    private IUserService iUserService;

    @SystemLog(about = "查询用户", type = LogType.DATA_CENTER,doType = "USER-01")
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户")
    public Result<IPage<User>> getByPage(@ModelAttribute User user,@ModelAttribute PageVo page){
        QueryWrapper<User> qw = new QueryWrapper<>();
        if(user.getDepartmentId() != null && !LypNullUtils.isNull(user.getDepartmentId())) {
            qw.like("department_id",user.getDepartmentId());
        }
        if(user.getNickname() != null && !LypNullUtils.isNull(user.getNickname())) {
            qw.like("nickname",user.getNickname());
        }
        IPage<User> data = iUserService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<User>>().setData(data);
    }
}
