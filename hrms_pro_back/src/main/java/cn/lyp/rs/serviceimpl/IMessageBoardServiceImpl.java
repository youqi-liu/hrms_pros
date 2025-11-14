package cn.lyp.rs.serviceimpl;

import cn.lyp.rs.mapper.MessageBoardMapper;
import cn.lyp.rs.entity.MessageBoard;
import cn.lyp.rs.service.IMessageBoardService;
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
public class IMessageBoardServiceImpl extends ServiceImpl<MessageBoardMapper, MessageBoard> implements IMessageBoardService {

    @Autowired
    private MessageBoardMapper messageBoardMapper;
}