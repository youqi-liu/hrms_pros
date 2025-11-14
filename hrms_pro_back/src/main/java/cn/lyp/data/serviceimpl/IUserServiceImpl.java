package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.UserMapper;
import cn.lyp.data.entity.User;
import cn.lyp.data.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
