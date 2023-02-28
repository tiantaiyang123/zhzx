/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.TeachingResult;
import com.zhzx.server.vo.TeachingResultAuditVo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface TeachingResultService extends IService<TeachingResult> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(TeachingResult entity);

    TeachingResult add(Map<String, Object> params);

    TeachingResult audit(TeachingResultAuditVo teachingResultAuditVo);

    TeachingResult update(Map<String, Object> params);

    TeachingResult updateAttach(String urls, Long id);

    IPage<TeachingResult> queryPage(IPage<TeachingResult> page, QueryWrapper<TeachingResult> wrapper);

    void deleteTogether(Long id);

    Map<String, Object> detail(Long id);

    void addResult(Map<String, Object> params) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;

    void updateResult(Map<String, Object> params, boolean updateAllFields) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;

    void deleteResult(Long id, String schema) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

     void auditResult(TeachingResult entity);

    XSSFWorkbook exportExcel(QueryWrapper<TeachingResult> wrapper) throws IOException, InvalidFormatException;

    TeachingResult detailResult(Long teacherResultId);

    Object init(String fileUrl);
}
