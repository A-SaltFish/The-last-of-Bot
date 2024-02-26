package com.tlb.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId private Integer id;
    private String username;
    private String password;
    private Integer rating;

    private String photo;
}
