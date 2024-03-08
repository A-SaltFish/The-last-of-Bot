package com.tlb.backend.controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.tlb.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRankListController {
    @Autowired
    GetRankListService getRankListService;

    @GetMapping("/rank/list/")
    public JSONObject getList(@RequestParam Map<String,String> data){
        Integer page=Integer.parseInt(data.get("page"));
        return getRankListService.getList(page);
    }
}
