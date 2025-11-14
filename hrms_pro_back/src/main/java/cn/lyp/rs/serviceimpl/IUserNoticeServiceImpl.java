package cn.lyp.rs.serviceimpl;

import cn.lyp.rs.mapper.UserNoticeMapper;
import cn.lyp.rs.entity.UserNotice;
import cn.lyp.rs.service.IUserNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class IUserNoticeServiceImpl extends ServiceImpl<UserNoticeMapper, UserNotice> implements IUserNoticeService {

    @Autowired
    private UserNoticeMapper userNoticeMapper;
}