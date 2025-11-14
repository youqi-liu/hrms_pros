package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.DepartmentHeaderMapper;
import cn.lyp.data.entity.DepartmentHeader;
import cn.lyp.data.service.IDepartmentHeaderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IDepartmentHeaderServiceImpl extends ServiceImpl<DepartmentHeaderMapper, DepartmentHeader> implements IDepartmentHeaderService {

}
