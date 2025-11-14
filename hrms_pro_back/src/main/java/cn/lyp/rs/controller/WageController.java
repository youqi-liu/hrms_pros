package cn.lyp.rs.controller;

import cn.lyp.basics.baseVo.Result;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.data.entity.User;
import cn.lyp.data.service.IUserService;
import cn.lyp.rs.entity.Position;
import cn.lyp.rs.service.IPositionService;
import cn.lyp.rs.utils.MyNullUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "职位管理管理接口")
@RequestMapping("/wage")
@Transactional
public class WageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<List<User>> getByPage(@ModelAttribute User user) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        if(user.getNickname() != null && !MyNullUtils.isNull(user.getNickname())) {
            qw.like("nickname",user.getNickname());
        }
        return new ResultUtil<List<User>>().setData(iUserService.list(qw));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> edit(@RequestParam String id,@RequestParam float moneyData) {
        User user = iUserService.getById(id);
        if(user == null) {
            return ResultUtil.error("用户不存在");
        }
        user.setMoneyData(BigDecimal.valueOf(moneyData).setScale(2, RoundingMode.DOWN));
        iUserService.saveOrUpdate(user);
        return ResultUtil.success("OK");
    }
}
