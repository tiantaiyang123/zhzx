/**
 * 项目：中华中学管理平台
 * 模型分组：医疗管理
 * 模型名称：医药费报销条目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.MedicalExpenseReimbursementEntries;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.MedicalEntryEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.MedicalExpenseReimbursementEntriesMapper;
import com.zhzx.server.repository.base.MedicalExpenseReimbursementEntriesBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.MedicalExpenseReimbursementEntriesService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
public class MedicalExpenseReimbursementEntriesServiceImpl extends ServiceImpl<MedicalExpenseReimbursementEntriesMapper, MedicalExpenseReimbursementEntries> implements MedicalExpenseReimbursementEntriesService {

    @Override
    public int updateAllFieldsById(MedicalExpenseReimbursementEntries entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Value("${web.upload-path}")
    private String uploadPath;

    @Override
    @Transactional( rollbackFor = Exception.class )
    public void importExcel(String year, String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + File.separator + items[items.length - 2] + File.separator + items[items.length - 1]);
        if (!file.exists()) {
            throw new ApiCode.ApiException(-1, "文件不存在！");
        }

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        MedicalExpenseReimbursementEntries medicalExpenseReimbursementEntries;
        List<MedicalExpenseReimbursementEntries> medicalExpenseReimbursementEntriesList = new LinkedList<>();
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();

            StringBuilder sb = new StringBuilder();
            // 清洗数据
            for (int rowIndex = 2; rowIndex < rowNum; rowIndex++) {
                XSSFRow rowValue = sheet.getRow(rowIndex);

                String idNumber = CellUtils.getCellValue(rowValue.getCell(0));
                if (StringUtils.isNullOrEmpty(idNumber)) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("身份证号为空").append("\r\n");
                    continue;
                }
                String staffName = CellUtils.getCellValue(rowValue.getCell(1));
                if (StringUtils.isNullOrEmpty(staffName)) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("教职工名称为空").append("\r\n");
                    continue;
                }
                String isDelete = CellUtils.getCellValue(rowValue.getCell(2));
                if (StringUtils.isNullOrEmpty(isDelete)) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("人员类别为空").append("\r\n");
                    continue;
                }
                String visitEndDate = CellUtils.getCellValue(rowValue.getCell(3), "yyyy-MM-dd");
                if (StringUtils.isNullOrEmpty(visitEndDate)) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("就诊结束日期为空").append("\r\n");
                    continue;
                }
                String type = CellUtils.getCellValue(rowValue.getCell(4));
                if (StringUtils.isNullOrEmpty(type)) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("门诊类别为空").append("\r\n");
                    continue;
                }
                String costTotal = CellUtils.getCellValue(rowValue.getCell(5));
                if (StringUtils.isNullOrEmpty(costTotal)) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("门诊类别为空").append("\r\n");
                    continue;
                }
                String costInsurance = CellUtils.getCellValue(rowValue.getCell(6));
                String costPollingFund = CellUtils.getCellValue(rowValue.getCell(7));
                String costSeriousIllnessSupply = CellUtils.getCellValue(rowValue.getCell(8));
                String costSeriousIllness = CellUtils.getCellValue(rowValue.getCell(9));
                String costOtherSocialInsurance = CellUtils.getCellValue(rowValue.getCell(10));
                String costNonSocialInsurance = CellUtils.getCellValue(rowValue.getCell(11));
                String affiliatedMedicalInstitution = CellUtils.getCellValue(rowValue.getCell(12));
                if (StringUtils.isNullOrEmpty(affiliatedMedicalInstitution)) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("定点医疗机构为空").append("\r\n");
                    continue;
                }

                medicalExpenseReimbursementEntries = new MedicalExpenseReimbursementEntries();
                medicalExpenseReimbursementEntries.setIdNumber(idNumber);
                medicalExpenseReimbursementEntries.setStaffName(staffName);
                medicalExpenseReimbursementEntries.setIsDelete("在职".equals(isDelete) ? YesNoEnum.YES : YesNoEnum.NO);
                medicalExpenseReimbursementEntries.setVisitEndDate(visitEndDate);
                medicalExpenseReimbursementEntries.setType(type);
                medicalExpenseReimbursementEntries.setCostTotal(new BigDecimal(costTotal));
                medicalExpenseReimbursementEntries.setCostInsurance(StringUtils.isNullOrEmpty(costInsurance) ? BigDecimal.ZERO : new BigDecimal(costInsurance));
                medicalExpenseReimbursementEntries.setCostPollingFund(StringUtils.isNullOrEmpty(costPollingFund) ? BigDecimal.ZERO : new BigDecimal(costPollingFund));
                medicalExpenseReimbursementEntries.setCostSeriousIllnessSupply(StringUtils.isNullOrEmpty(costSeriousIllnessSupply) ? BigDecimal.ZERO : new BigDecimal(costSeriousIllnessSupply));
                medicalExpenseReimbursementEntries.setCostSeriousIllness(StringUtils.isNullOrEmpty(costSeriousIllness) ? BigDecimal.ZERO : new BigDecimal(costSeriousIllness));
                medicalExpenseReimbursementEntries.setCostOtherSocialInsurance(StringUtils.isNullOrEmpty(costOtherSocialInsurance) ? BigDecimal.ZERO : new BigDecimal(costOtherSocialInsurance));
                medicalExpenseReimbursementEntries.setCostNonSocialInsurance(StringUtils.isNullOrEmpty(costNonSocialInsurance) ? BigDecimal.ZERO : new BigDecimal(costNonSocialInsurance));
                medicalExpenseReimbursementEntries.setAffiliatedMedicalInstitution(affiliatedMedicalInstitution);
                medicalExpenseReimbursementEntries.setEntryStatus(MedicalEntryEnum.NOT_FOUND);
                medicalExpenseReimbursementEntries.setEditorId(user.getId());
                medicalExpenseReimbursementEntries.setEditorName(user.getRealName());
                medicalExpenseReimbursementEntries.setDefault().validate(true);
                medicalExpenseReimbursementEntriesList.add(medicalExpenseReimbursementEntries);
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, sb.toString());
            }

            if (CollectionUtils.isNotEmpty(medicalExpenseReimbursementEntriesList)) {
                this.baseMapper.delete(
                        Wrappers.<MedicalExpenseReimbursementEntries>lambdaQuery()
                                .likeRight(MedicalExpenseReimbursementEntries::getVisitEndDate, year + "\\-")
                );
                this.baseMapper.batchInsert(medicalExpenseReimbursementEntriesList);
            }
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        } finally {
            file.delete();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer removeYearData(String year) {
        return this.baseMapper.delete(
                Wrappers.<MedicalExpenseReimbursementEntries>lambdaQuery()
                        .likeRight(MedicalExpenseReimbursementEntries::getVisitEndDate, year + "\\-")
        );
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<MedicalExpenseReimbursementEntries> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(MedicalExpenseReimbursementEntriesBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
