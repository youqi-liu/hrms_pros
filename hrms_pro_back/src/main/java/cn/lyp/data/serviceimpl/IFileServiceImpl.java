package cn.lyp.data.serviceimpl;

import cn.lyp.data.dao.mapper.FileMapper;
import cn.lyp.data.entity.File;
import cn.lyp.data.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class IFileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
