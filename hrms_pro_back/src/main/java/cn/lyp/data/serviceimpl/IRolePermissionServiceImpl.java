package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.RolePermissionMapper;
import cn.lyp.data.entity.RolePermission;
import cn.lyp.data.service.IRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IRolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

}
