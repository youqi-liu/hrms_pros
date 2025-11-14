package cn.lyp.rs.serviceimpl;

import cn.lyp.rs.mapper.JobTitleMapper;
import cn.lyp.rs.entity.JobTitle;
import cn.lyp.rs.service.IJobTitleService;
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
public class IJobTitleServiceImpl extends ServiceImpl<JobTitleMapper, JobTitle> implements IJobTitleService {

    @Autowired
    private JobTitleMapper jobTitleMapper;
}