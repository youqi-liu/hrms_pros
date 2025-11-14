package cn.lyp.rs.serviceimpl;

import cn.lyp.rs.mapper.SalaryWithdrawalMapper;
import cn.lyp.rs.entity.SalaryWithdrawal;
import cn.lyp.rs.service.ISalaryWithdrawalService;
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
public class ISalaryWithdrawalServiceImpl extends ServiceImpl<SalaryWithdrawalMapper, SalaryWithdrawal> implements ISalaryWithdrawalService {

    @Autowired
    private SalaryWithdrawalMapper salaryWithdrawalMapper;
}