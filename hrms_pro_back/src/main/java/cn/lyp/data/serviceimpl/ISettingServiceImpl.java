package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.SettingMapper;
import cn.lyp.data.entity.Setting;
import cn.lyp.data.service.ISettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ISettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements ISettingService {

}
