package cn.lyp.rs.controller;

import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.rs.entity.JobTitle;
import cn.lyp.rs.service.IJobTitleService;
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
@Api(tags = "职称管理")
@RequestMapping("/jobTitle")
@Transactional
public class JobTitleController {

    @Autowired
    private IJobTitleService iJobTitleService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部职称")
    public Result<List<JobTitle>> getAll() {
        List<JobTitle> list = iJobTitleService.list();
        return new ResultUtil<List<JobTitle>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询职称")
    public Result<IPage<JobTitle>> getByPage(@ModelAttribute JobTitle jobTitle,@ModelAttribute PageVo page) {
        QueryWrapper<JobTitle> qw = new QueryWrapper<>();
        if(jobTitle.getTitle() != null && !MyNullUtils.isNull(jobTitle.getTitle())) {
            qw.like("title",jobTitle.getTitle());
        }
        if(jobTitle.getCode() != null && !MyNullUtils.isNull(jobTitle.getCode())) {
            qw.like("code",jobTitle.getCode());
        }
        IPage<JobTitle> data = iJobTitleService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<JobTitle>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增职称")
    public Result<JobTitle> saveOrUpdate(JobTitle jobTitle) {
        if (iJobTitleService.saveOrUpdate(jobTitle)) {
            return new ResultUtil<JobTitle>().setData(jobTitle);
        }
        return new ResultUtil<JobTitle>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除职称")
    public Result<Object> delByIds(@RequestParam String[] ids) {
        for (String id : ids) {
            iJobTitleService.removeById(id);
        }
        return ResultUtil.success();
    }
}
