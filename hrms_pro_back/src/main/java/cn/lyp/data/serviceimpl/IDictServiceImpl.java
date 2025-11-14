package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.DictMapper;
import cn.lyp.data.entity.Dict;
import cn.lyp.data.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IDictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

}
