package com.zhzx.server.vo;

import com.zhzx.server.domain.Authority;
import lombok.Data;

import java.util.List;

/**
 * @Author: 王志斌
 * @Date: 2021/12/29 上午10:27
 */
@Data
public class AuthorityVo extends Authority {
    private List<Authority> children;
}
