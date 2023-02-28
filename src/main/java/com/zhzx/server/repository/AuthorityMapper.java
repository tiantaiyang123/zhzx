/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：权限表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.zhzx.server.repository.base.AuthorityBaseMapper;
import com.zhzx.server.vo.AuthorityVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityMapper extends AuthorityBaseMapper {

    /**
     * 根据用户id获取其拥有的权限
     * @param userId
     * @return
     */
    List<AuthorityVo> selectAuthoritiesByUserId(@Param("userId")Long userId);
}
