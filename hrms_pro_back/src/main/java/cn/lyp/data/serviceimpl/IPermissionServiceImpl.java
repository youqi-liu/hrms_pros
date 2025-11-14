package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.PermissionMapper;
import cn.lyp.data.entity.Permission;
import cn.lyp.data.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IPermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
