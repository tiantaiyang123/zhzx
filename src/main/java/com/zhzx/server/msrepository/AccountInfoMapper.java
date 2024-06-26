/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.msrepository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhzx.server.msdomain.AccountInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AccountInfoMapper extends BaseMapper<AccountInfo> {

    Map<String, Object> getInfo(@Param("cPhysicalNo") Long cPhysicalNo,
                                @Param("mobile") String mobile,
                                @Param("idNumber") String idNumber);

    AccountInfo selectOneByPIN(@Param("PIN")String PIN);

}
