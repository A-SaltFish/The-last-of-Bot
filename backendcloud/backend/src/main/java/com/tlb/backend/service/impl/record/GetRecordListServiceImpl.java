package com.tlb.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tlb.backend.mapper.RecordMapper;
import com.tlb.backend.mapper.UserMapper;
import com.tlb.backend.pojo.Record;
import com.tlb.backend.pojo.User;
import com.tlb.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;

    //多表联查，join
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        //IPage??
        IPage<Record> recordIPage=new Page<>(page,10);
        QueryWrapper<Record> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<Record> recordList=recordMapper.selectPage(recordIPage,queryWrapper).getRecords();
        JSONObject res=new JSONObject();
        List<JSONObject> items=new LinkedList<>();
        for(Record record:recordList){
            User userA=userMapper.selectById(record.getAId());
            User userB=userMapper.selectById(record.getBId());
            JSONObject item=new JSONObject();
            item.put("a_photo",userA.getPhoto());
            item.put("a_username",userA.getUsername());
            item.put("b_photo",userB.getPhoto());
            item.put("b_username",userB.getUsername());
            String record_result="平局";
            if("a".equals(record.getLoser()))
                record_result="一号赢！";
            if("b".equals(record.getLoser()))
                record_result="二号赢！";
            item.put("record_result",record_result);
            item.put("record",record);
            items.add(item);
        }
        res.put("records",items);
        res.put("records_count",recordMapper.selectCount(null));
        return res;
    }
}
