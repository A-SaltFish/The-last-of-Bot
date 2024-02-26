package com.tlb.backend.service.impl.user.account;

import com.tlb.backend.pojo.User;
import com.tlb.backend.service.impl.utils.UserDetailsImpl;
import com.tlb.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken =(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();
        //System.out.println("hi");
        Map<String,String> map=new HashMap<>();
        map.put("error","success");
        map.put("id",user.getId().toString());
        map.put("username",user.getUsername());
        map.put("photo",user.getPhoto());
        return map;
    }
}
