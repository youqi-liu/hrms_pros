package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.UserRoleMapper;
import cn.lyp.data.entity.UserRole;
import cn.lyp.data.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IUserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
