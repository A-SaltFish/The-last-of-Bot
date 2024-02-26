package com.tlb.backend.service.impl.user.account;

import com.tlb.backend.pojo.User;
import com.tlb.backend.service.impl.utils.UserDetailsImpl;
import com.tlb.backend.service.user.account.LoginService;
import com.tlb.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        //进行加密，防止存铭文
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果登陆失败，会自动处理
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticate.getPrincipal();
        User user=loginUser.getUser();
        //利用jwt进行加密
        String jwt= JwtUtil.createJWT(user.getId().toString());
        Map<String,String> map=new HashMap<>();
        map.put("error","success");
        map.put("token",jwt);
        return map;
    }
}
