package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.DictDataMapper;
import cn.lyp.data.entity.DictData;
import cn.lyp.data.service.IDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IDictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

}
