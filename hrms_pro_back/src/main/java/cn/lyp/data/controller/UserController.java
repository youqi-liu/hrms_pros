package cn.lyp.data.controller;

import cn.lyp.basics.log.SystemLog;
import cn.lyp.basics.redis.RedisTemplateHelper;
import cn.lyp.basics.utils.*;
import cn.lyp.basics.parameter.CommonConstant;
import cn.lyp.basics.parameter.ErrorMessageConstants;
import cn.lyp.basics.log.LogType;
import cn.lyp.basics.baseVo.PageVo;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.data.entity.*;
import cn.lyp.data.service.*;
import cn.lyp.data.utils.LypNullUtils;
import cn.lyp.data.vo.PermissionDTO;
import cn.lyp.data.vo.RoleDTO;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "User Management APIs")
@RequestMapping("/users")
@CacheConfig(cacheNames = "user")
@Transactional
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IDepartmentService iDepartmentService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private IDepartmentHeaderService iDepartmentHeaderService;

    @Autowired
    private IRolePermissionService iRolePermissionService;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private IPermissionService iPermissionService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String USER_ROLE_KEY = "userRole::";

    private static final String USER_ROLE_DEP_KEY = "userRole::depIds:";

    private static final String USER_MENU_KEY = "permission::userMenuList:";

    private static final String USER_CACHE_KEY = "user::";

    @SystemLog(about = "查询我的薪资标准", type = LogType.DATA_CENTER,doType = "USER-02")
    @GetMapping("/my-wage")
    @ApiOperation(value = "Get My Wage Standard")
    public Result<User> getMyWage(){
        User currentUser = securityUtil.getCurrUser();
        return new ResultUtil<User>().setData(iUserService.getById(currentUser.getId()));
    }

    @SystemLog(about = "获取当前登录用户", type = LogType.DATA_CENTER,doType = "USER-02")
    @GetMapping("/profile")
    @ApiOperation(value = "Get Current User Profile")
    public Result<User> getUserInfo(){
        User currentUser = securityUtil.getCurrUser();
        entityManager.clear();
        currentUser.setPassword(null);
        return new ResultUtil<User>().setData(currentUser);
    }

    @PostMapping("/register")
    @ApiOperation(value = "User Registration")
    public Result<Object> regist(@Valid User newUser){
        newUser.setEmail(newUser.getMobile() + CommonConstant.DEFAULT_EMAIL_SUFFIX);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("username", newUser.getUsername()).or().eq("mobile",newUser.getMobile()));
        if(iUserService.count(queryWrapper) > 0L) {
            return ResultUtil.error(ErrorMessageConstants.USER_DUPLICATE);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(encryptedPassword).setType(CommonConstant.USER_TYPE_NORMAL);
        iUserService.saveOrUpdate(newUser);
        QueryWrapper<Role> roleQuery = new QueryWrapper<>();
        roleQuery.eq("default_role",true);
        List<Role> defaultRoles = iRoleService.list(roleQuery);
        if(defaultRoles.size() > 0){
            for(Role role : defaultRoles) {
                iUserRoleService.saveOrUpdate(new UserRole().setUserId(newUser.getId()).setRoleId(role.getId()));
            }
        }
        return ResultUtil.data(newUser);
    }

    @SystemLog(about = "解锁验证密码", type = LogType.DATA_CENTER,doType = "USER-03")
    @PostMapping("/unlock")
    @ApiOperation(value = "Unlock with Password Verification")
    public Result<Object> unLock(@RequestParam String password){
        User currentUser = securityUtil.getCurrUser();
        if(!new BCryptPasswordEncoder().matches(password, currentUser.getPassword())){
            return ResultUtil.error(ErrorMessageConstants.PASSWORD_INCORRECT);
        }
        return ResultUtil.data(null);
    }

    @SystemLog(about = "重置密码", type = LogType.DATA_CENTER,doType = "USER-04")
    @PostMapping("/reset-password")
    @ApiOperation(value = "Reset User Password")
    public Result<Object> resetPass(@RequestParam String[] ids){
        for(String id : ids){
            User user = iUserService.getById(id);
            if(user == null) {
                return ResultUtil.error(ErrorMessageConstants.USER_NOT_EXIST);
            }
            user.setPassword(new BCryptPasswordEncoder().encode(CommonConstant.DEFAULT_PASSWORD));
            iUserService.saveOrUpdate(user);
            redisTemplate.delete(USER_CACHE_KEY + user.getUsername());
        }
        return ResultUtil.success();
    }

    @SystemLog(about = "修改用户资料", type = LogType.DATA_CENTER,doType = "USER-05")
    @PutMapping("/profile")
    @ApiOperation(value = "Update User Profile",notes = "用户名密码不会修改 需要username更新缓存")
    @CacheEvict(key = "#user.username")
    public Result<Object> editOwn(User user){
        User currentUser = securityUtil.getCurrUser();
        user.setUsername(currentUser.getUsername());
        user.setPassword(currentUser.getPassword());
        iUserService.saveOrUpdate(user);
        return ResultUtil.success(ErrorMessageConstants.UPDATE_SUCCESS);
    }

    @SystemLog(about = "修改密码", type = LogType.DATA_CENTER,doType = "USER-06")
    @PutMapping("/password")
    @ApiOperation(value = "Change Password")
    public Result<Object> modifyPass(@RequestParam String password,@RequestParam String newPass,@RequestParam String passStrength){
        User currentUser = securityUtil.getCurrUser();
        if(!new BCryptPasswordEncoder().matches(password, currentUser.getPassword())){
            return ResultUtil.error(ErrorMessageConstants.OLD_PASSWORD_INCORRECT);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(newPass);
        currentUser.setPassword(encryptedPassword);
        currentUser.setPassStrength(passStrength);
        iUserService.saveOrUpdate(currentUser);
        redisTemplate.delete(USER_CACHE_KEY + currentUser.getUsername());
        return ResultUtil.success();
    }

    @SystemLog(about = "查询用户", type = LogType.DATA_CENTER,doType = "USER-07")
    @GetMapping
    @ApiOperation(value = "Get User List with Pagination")
    public Result<IPage<User>> getUserList(@ModelAttribute User user, @ModelAttribute PageVo page) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!LypNullUtils.isNull(user.getNickname())) {
            queryWrapper.like("nickname",user.getNickname());
        }
        if(!LypNullUtils.isNull(user.getDepartmentId())) {
            queryWrapper.eq("department_id",user.getDepartmentId());
        }
        IPage<User> userPage = iUserService.page(PageUtil.initMpPage(page),queryWrapper);
        for(User userRecord: userPage.getRecords()) {
            QueryWrapper<Role> roleQuery = new QueryWrapper<>();
            roleQuery.inSql("id","SELECT role_id FROM hrms_user_role WHERE user_id = '" + userRecord.getId() + "'");
            List<Role> roleList = iRoleService.list(roleQuery);
            List<RoleDTO> roleDTOList = roleList.stream().map(role->{
                return new RoleDTO().setId(role.getId()).setName(role.getName()).setDescription(role.getDescription());
            }).collect(Collectors.toList());
            userRecord.setRoles(roleDTOList);
            entityManager.detach(userRecord);
            userRecord.setPassword(null);
        }
        return new ResultUtil<IPage<User>>().setData(userPage);
    }

    @SystemLog(about = "根据部门查询用户", type = LogType.DATA_CENTER,doType = "USER-08")
    @RequestMapping(value = "/getByDepartmentId", method = RequestMethod.GET)
    @ApiOperation(value = "Get Users by Department ID")
    public Result<List<User>> getByCondition(@RequestParam String departmentId){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        List<User> userList = iUserService.list(queryWrapper);
        entityManager.clear();
        userList.forEach(user -> {
            user.setPassword(null);
        });
        return new ResultUtil<List<User>>().setData(userList);
    }

    @SystemLog(about = "模拟搜索用户", type = LogType.DATA_CENTER,doType = "USER-09")
    @RequestMapping(value = "/searchByName/{username}", method = RequestMethod.GET)
    @ApiOperation(value = "Simulate User Search by Username")
    public Result<List<User>> searchByName(@PathVariable String username) throws UnsupportedEncodingException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", URLDecoder.decode(username, "utf-8"));
        queryWrapper.eq("status", CommonConstant.USER_STATUS_NORMAL);
        List<User> userList = iUserService.list(queryWrapper);
        entityManager.clear();
        userList.forEach(user -> {
            user.setPassword(null);
        });
        return new ResultUtil<List<User>>().setData(userList);
    }

    @SystemLog(about = "查询全部用户", type = LogType.DATA_CENTER,doType = "USER-10")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "Get All Users")
    public Result<List<User>> getAll(){
        List<User> userList = iUserService.list();
        for(User user: userList){
            entityManager.clear();
            user.setPassword(null);
        }
        return new ResultUtil<List<User>>().setData(userList);
    }

    @SystemLog(about = "管理员修改资料", type = LogType.DATA_CENTER,doType = "USER-11")
    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    @ApiOperation(value = "Admin Update User Profile")
    @CacheEvict(key = "#u.username")
    public Result<Object> edit(User u,@RequestParam(required = false) String[] roleIds){
        User customaryUser = iUserService.getById(u.getId());
        u.setUsername(customaryUser.getUsername());
        u.setPassword(customaryUser.getPassword());
        if(!Objects.equals(customaryUser.getMobile(),u.getMobile())) {
            QueryWrapper<User> customaryUserQw = new QueryWrapper<>();
            customaryUserQw.ne("id",customaryUser.getId());
            customaryUserQw.eq("mobile",u.getMobile());
            long customaryUserCount = iUserService.count(customaryUserQw);
            if(customaryUserCount > 0) {
                return ResultUtil.error("手机号重复");
            }
        }
        if(!LypNullUtils.isNull(u.getDepartmentId())) {
            Department department = iDepartmentService.getById(u.getDepartmentId());
            if(department != null) {
                u.setDepartmentTitle(department.getTitle());
            }
        } else {
            u.setDepartmentId("");
            u.setDepartmentTitle("");
        }
        iUserService.saveOrUpdate(u);
        QueryWrapper<UserRole> userRoleQw = new QueryWrapper<>();
        userRoleQw.eq("user_id",u.getId());
        iUserRoleService.remove(userRoleQw);
        if(roleIds != null && roleIds.length > 0) {
            for (String roleId : roleIds) {
                UserRole ur = new UserRole();
                ur.setUserId(u.getId());
                ur.setRoleId(roleId);
                iUserRoleService.saveOrUpdate(ur);
            }
        }
        redisTemplate.delete(USER_ROLE_KEY + u.getId());
        redisTemplate.delete(USER_ROLE_DEP_KEY + u.getId());
        redisTemplate.delete(USER_MENU_KEY + u.getId());
        return ResultUtil.success();
    }

    @SystemLog(about = "添加用户", type = LogType.DATA_CENTER,doType = "USER-12")
    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    @ApiOperation(value = "Admin Add User")
    public Result<Object> add(@Valid User u,@RequestParam(required = false) String[] roleIds) {
        QueryWrapper<User> userQw = new QueryWrapper<>();
        userQw.and(wrapper -> wrapper.eq("username", u.getUsername()).or().eq("mobile",u.getMobile()));
        if(iUserService.count(userQw) > 0L) {
            return ResultUtil.error("登录账号/手机号重复");
        }
        if(!LypNullUtils.isNull(u.getDepartmentId())){
            Department department = iDepartmentService.getById(u.getDepartmentId());
            if(department != null){
                u.setDepartmentTitle(department.getTitle());
            }
        }else{
            u.setDepartmentId("");
            u.setDepartmentTitle("");
        }
        u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
        iUserService.saveOrUpdate(u);
        if(roleIds != null && roleIds.length > 0) {
            for (String roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(u.getId());
                userRole.setRoleId(roleId);
                iUserRoleService.saveOrUpdate(userRole);
            }
        }
        return ResultUtil.success();
    }

    @SystemLog(about = "禁用用户", type = LogType.DATA_CENTER,doType = "USER-13")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ApiOperation(value = "Disable User")
    public Result<Object> disable( @RequestParam String id){
        User user = iUserService.getById(id);
        if(user == null){
            return ResultUtil.error(ErrorMessageConstants.USER_NOT_EXIST);
        }
        user.setStatus(CommonConstant.USER_STATUS_LOCK);
        iUserService.saveOrUpdate(user);
        redisTemplate.delete(USER_CACHE_KEY + user.getUsername());
        return ResultUtil.success();
    }

    @SystemLog(about = "启用用户", type = LogType.DATA_CENTER,doType = "USER-14")
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    @ApiOperation(value = "Enable User")
    public Result<Object> enable(@RequestParam String id){
        User user = iUserService.getById(id);
        if(user==null){
            return ResultUtil.error(ErrorMessageConstants.USER_NOT_EXIST);
        }
        user.setStatus(CommonConstant.USER_STATUS_NORMAL);
        iUserService.saveOrUpdate(user);
        redisTemplate.delete(USER_CACHE_KEY + user.getUsername());
        return ResultUtil.success();
    }

    @SystemLog(about = "删除用户", type = LogType.DATA_CENTER,doType = "USER-15")
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "Delete Users by IDs")
    public Result<Object> delByIds(@RequestParam String[] ids) {
        for(String id:ids){
            User user = iUserService.getById(id);
            redisTemplate.delete(USER_CACHE_KEY + user.getUsername());
            redisTemplate.delete(USER_ROLE_KEY + user.getId());
            redisTemplate.delete(USER_ROLE_DEP_KEY + user.getId());
            redisTemplate.delete(USER_MENU_KEY + user.getId());
            Set<String> keys = redisTemplateHelper.keys("department::*");
            redisTemplate.delete(keys);
            iUserService.removeById(id);
            QueryWrapper<UserRole> userRoleQuery = new QueryWrapper<>();
            userRoleQuery.eq("user_id",id);
            iUserRoleService.remove(userRoleQuery);
            QueryWrapper<DepartmentHeader> deptHeaderQuery = new QueryWrapper<>();
            deptHeaderQuery.eq("user_id",id);
            iDepartmentHeaderService.remove(deptHeaderQuery);
        }
        return ResultUtil.success();
    }

    @SystemLog(about = "导入用户", type = LogType.DATA_CENTER,doType = "USER-16")
    @PostMapping("/import")
    @ApiOperation(value = "Import Users from Excel")
    public Result<Object> importData(@RequestBody List<User> users){
        List<Integer> errors = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        int count = 0;
        for(User u: users){
            count++;
            if(StrUtil.isBlank(u.getUsername())||StrUtil.isBlank(u.getPassword())){
                errors.add(count);
                reasons.add("账号密码为空");
                continue;
            }

            QueryWrapper<User> userQw = new QueryWrapper<>();
            userQw.eq("username",u.getUsername());
            if(iUserService.count(userQw) > 0L) {
                errors.add(count);
                reasons.add("用户名已存在");
                continue;
            }
            u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
            if(StrUtil.isNotBlank(u.getDepartmentId())){
                Department department = iDepartmentService.getById(u.getDepartmentId());
                if(department == null) {
                    errors.add(count);
                    reasons.add("部门不存在");
                    continue;
                }
            }
            if(u.getStatus()==null){
                u.setStatus(CommonConstant.USER_STATUS_NORMAL);
            }
            iUserService.saveOrUpdate(u);
            if(u.getDefaultRole() != null && u.getDefaultRole()==1) {
                QueryWrapper<Role> roleQw = new QueryWrapper<>();
                roleQw.eq("default_role",true);
                List<Role> roleList = iRoleService.list(roleQw);
                if(roleList!=null&&roleList.size()>0){
                    for(Role role : roleList){
                        UserRole ur = new UserRole().setUserId(u.getId()).setRoleId(role.getId());
                        iUserRoleService.saveOrUpdate(ur);
                    }
                }
            }
        }
        int successCount = users.size() - errors.size();
        String successMessage = "成功导入 " + successCount + " 位用户";
        String failMessage = "成功导入 " + successCount + " 位用户，失败 " + errors.size() + " 位用户。<br>" +"第 " + errors.toString() + " 行数据导入出错，错误原因是为 <br>" + reasons.toString();
        String message = null;
        if(errors.size() == 0){
            message = successMessage;
        }else{
            message = failMessage;
        }
        return ResultUtil.success(message);
    }

    @ApiOperation(value = "添加用户的角色和菜单信息")
    public User userToDTO(User user) {
        if(user == null) {
            return null;
        }
        QueryWrapper<UserRole> urQw = new QueryWrapper<>();
        urQw.eq("user_id", user.getId());
        List<UserRole> roleList = iUserRoleService.list(urQw);
        List<RoleDTO> roleDTOList = new ArrayList<>();
        for (UserRole userRole : roleList) {
            Role role = iRoleService.getById(userRole.getRoleId());
            if(role != null) {
                roleDTOList.add(new RoleDTO().setId(role.getId()).setName(role.getName()));
            }
        }
        user.setRoles(roleDTOList);
        List<String> permissionIdList = new ArrayList<>();
        for (RoleDTO dto : roleDTOList) {
            QueryWrapper<RolePermission> rpQw = new QueryWrapper<>();
            rpQw.eq("role_id",dto.getId());
            List<RolePermission> list = iRolePermissionService.list(rpQw);
            for (RolePermission rp : list) {
                boolean flag = true;
                for (String id : permissionIdList) {
                    if(Objects.equals(id,rp.getPermissionId())) {
                        flag = false;
                        break;
                    }
                }
                if(flag) {
                    permissionIdList.add(rp.getPermissionId());
                }
            }
        }
        List<PermissionDTO> permissionDTOList = new ArrayList<>();
        for (String id : permissionIdList) {
            Permission permission = iPermissionService.getById(id);
            if(permission != null) {
                if(Objects.equals(permission.getType(),CommonConstant.PERMISSION_OPERATION)) {
                    continue;
                }
                permissionDTOList.add(new PermissionDTO().setTitle(permission.getTitle()).setPath(permission.getPath()));
            }
        }
        user.setPermissions(permissionDTOList);
        return user;
    }
}
