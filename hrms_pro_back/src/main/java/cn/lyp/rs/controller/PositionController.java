package cn.lyp.rs.controller;

import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.rs.entity.JobTitle;
import cn.lyp.rs.entity.Position;
import cn.lyp.rs.service.IPositionService;
import cn.lyp.rs.utils.MyNullUtils;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "职位管理管理接口")
@RequestMapping("/position")
@Transactional
public class PositionController {

    @Autowired
    private IPositionService iPositionService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Position>> getAll() {
        List<Position> list = iPositionService.list();
        return new ResultUtil<List<Position>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Position>> getByPage(@ModelAttribute Position position,@ModelAttribute PageVo page) {
        QueryWrapper<Position> qw = new QueryWrapper<>();
        if(position.getTitle() != null && !MyNullUtils.isNull(position.getTitle())) {
            qw.like("title",position.getTitle());
        }
        if(position.getCode() != null && !MyNullUtils.isNull(position.getCode())) {
            qw.like("code",position.getCode());
        }
        IPage<Position> data = iPositionService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Position>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Position> saveOrUpdate(Position position) {
        if (iPositionService.saveOrUpdate(position)) {
            return new ResultUtil<Position>().setData(position);
        }
        return new ResultUtil<Position>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids) {
        for (String id : ids) {
            iPositionService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
