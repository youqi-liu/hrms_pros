package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.LogMapper;
import cn.lyp.data.entity.Log;
import cn.lyp.data.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ILogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
