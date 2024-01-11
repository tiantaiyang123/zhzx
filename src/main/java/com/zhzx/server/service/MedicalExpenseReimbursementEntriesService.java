/**
 * 项目：中华中学管理平台
 * 模型分组：医疗管理
 * 模型名称：医药费报销条目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.MedicalExpenseReimbursementEntries;

public interface MedicalExpenseReimbursementEntriesService extends IService<MedicalExpenseReimbursementEntries> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(MedicalExpenseReimbursementEntries entity);


    void importExcel(String year, String fileUrl);

    Integer removeYearData(String year);
}
