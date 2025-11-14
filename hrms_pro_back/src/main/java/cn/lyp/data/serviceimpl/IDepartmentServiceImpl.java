package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.DepartmentMapper;
import cn.lyp.data.entity.Department;
import cn.lyp.data.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IDepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
