package cn.lyp.data.controller;

import cn.lyp.basics.log.LogType;
import cn.lyp.basics.log.SystemLog;
import cn.lyp.basics.redis.RedisTemplateHelper;
import cn.lyp.basics.utils.PageUtil;
import cn.lyp.basics.utils.ResultUtil;
import cn.lyp.basics.parameter.ErrorMessageConstants;
import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.data.entity.*;
import cn.lyp.data.service.IRolePermissionService;
import cn.lyp.data.service.IRoleService;
import cn.lyp.data.service.IUserRoleService;
import cn.lyp.data.utils.LypNullUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@Api(tags = "角色管理接口")
@RequestMapping("/role")
@Transactional
public class RoleController {

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private IRolePermissionService iRolePermissionService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @SystemLog(about = "查询全部角色", type = LogType.DATA_CENTER,doType = "ROLE-01")
    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部角色")
    public Result<Object> getAllList(){
        return ResultUtil.data(iRoleService.list());
    }

    @SystemLog(about = "查询角色", type = LogType.DATA_CENTER,doType = "ROLE-02")
    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询角色")
    public Result<IPage<Role>> getRoleByPage(@ModelAttribute Role role,@ModelAttribute PageVo page) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if(!LypNullUtils.isNull(role.getName())) {
            queryWrapper.like("name",role.getName());
        }
        if(!LypNullUtils.isNull(role.getDescription())) {
            queryWrapper.like("description",role.getDescription());
        }
        IPage<Role> rolePage = iRoleService.page(PageUtil.initMpPage(page),queryWrapper);
        for(Role roleRecord : rolePage.getRecords()){
            QueryWrapper<RolePermission> permissionQuery = new QueryWrapper<>();
            permissionQuery.eq("role_id",roleRecord.getId());
            roleRecord.setPermissions(iRolePermissionService.list(permissionQuery));
        }
        return new ResultUtil<IPage<Role>>().setData(rolePage);
    }

    @SystemLog(about = "配置默认角色", type = LogType.DATA_CENTER,doType = "ROLE-03")
    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    @ApiOperation(value = "配置默认角色")
    public Result<Object> setDefault(@RequestParam String id,@RequestParam Boolean isDefault){
        Role role = iRoleService.getById(id);
        if(role != null) {
            if(!Objects.equals(role.getDefaultRole(),isDefault)) {
                role.setDefaultRole(isDefault);
                iRoleService.saveOrUpdate(role);
            }
            return ResultUtil.success();
        }
        return ResultUtil.error(ErrorMessageConstants.ROLE_NOT_EXIST);
    }

    @SystemLog(about = "修改菜单权限", type = LogType.DATA_CENTER,doType = "ROLE-04")
    @RequestMapping(value = "/editRolePerm", method = RequestMethod.POST)
    @ApiOperation(value = "修改菜单权限")
    public Result<Object> editRolePerm(@RequestParam String roleId,@RequestParam(required = false) String[] permIds){
        Role role = iRoleService.getById(roleId);
        if(role == null) {
            return ResultUtil.error(ErrorMessageConstants.ROLE_NOT_EXIST);
        }
        if(permIds == null) {
            permIds = new String[0];
        }
        QueryWrapper<RolePermission> oldPermissionQuery = new QueryWrapper<>();
        oldPermissionQuery.eq("role_id",role.getId());
        List<RolePermission> oldPermissionList = iRolePermissionService.list(oldPermissionQuery);
        for (String permId : permIds) {
            boolean flag = true;
            for (RolePermission rp : oldPermissionList) {
                if(Objects.equals(permId,rp.getPermissionId())) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                RolePermission rp = new RolePermission();
                rp.setRoleId(role.getId());
                rp.setPermissionId(permId);
                iRolePermissionService.saveOrUpdate(rp);
            }
        }
        for (RolePermission rp : oldPermissionList) {
            boolean flag = true;
            for (String permId : permIds) {
                if(Objects.equals(permId,rp.getPermissionId())) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                iRolePermissionService.removeById(rp.getId());
            }
        }
        Set<String> keysUser = redisTemplateHelper.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplateHelper.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);
        Set<String> keysUserMenu = redisTemplateHelper.keys("permission::userMenuList:*");
        redisTemplate.delete(keysUserMenu);
        return ResultUtil.data();
    }

    @SystemLog(about = "新增角色", type = LogType.DATA_CENTER,doType = "ROLE-05")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "新增角色")
    public Result<Role> save(Role role){
        iRoleService.saveOrUpdate(role);
        return new ResultUtil<Role>().setData(role);
    }

    @SystemLog(about = "编辑角色", type = LogType.DATA_CENTER,doType = "ROLE-06")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑角色")
    public Result<Role> edit(Role role){
        iRoleService.saveOrUpdate(role);
        Set<String> keysUser = redisTemplateHelper.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplateHelper.keys("userRole:" + "*");
        redisTemplate.delete(keysUserRole);
        return new ResultUtil<Role>().setData(role);
    }

    @SystemLog(about = "删除角色", type = LogType.DATA_CENTER,doType = "ROLE-07")
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除角色")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids) {
            QueryWrapper<UserRole> userRoleQuery = new QueryWrapper<>();
            userRoleQuery.eq("role_id", id);
            long userRoleCount = iUserRoleService.count(userRoleQuery);
            if(userRoleCount > 0L){
                return ResultUtil.error("不能删除正在使用的角色");
            }
        }
        for(String id:ids){
            iRoleService.removeById(id);
            QueryWrapper<RolePermission> rolePermissionQuery = new QueryWrapper<>();
            rolePermissionQuery.eq("role_id",id);
            iRolePermissionService.remove(rolePermissionQuery);
        }
        return ResultUtil.success();
    }
}
