/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：学生家长表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Student;
import com.zhzx.server.enums.RelationEnum;
import com.zhzx.server.repository.StudentMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.StudentParentService;
import com.zhzx.server.repository.StudentParentMapper;
import com.zhzx.server.repository.base.StudentParentBaseMapper;
import com.zhzx.server.domain.StudentParent;
import com.zhzx.server.rest.req.StudentParentParam;

@Service
public class StudentParentServiceImpl extends ServiceImpl<StudentParentMapper, StudentParent> implements StudentParentService {

    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private StudentMapper studentMapper;

    @Override
    public int updateAllFieldsById(StudentParent entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
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
    public boolean saveBatch(Collection<StudentParent> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StudentParentBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public void importStudentParent(String fileUrl) {
        if (fileUrl == null || fileUrl == "")
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 错误行
            StringBuilder sb = new StringBuilder();
            String errText = "";
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String studentName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                String orderNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
                String parentName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                String parentMobile = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String relation = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                if (StringUtils.isNullOrEmpty(orderNumber) ) {
                    errText += "\t编号不能都为空";
                }
                if (StringUtils.isNullOrEmpty(parentName) && StringUtils.isNullOrEmpty(parentMobile) && StringUtils.isNullOrEmpty(relation)) {
                    errText += "\t家长信息不能为空";
                }
                QueryWrapper<Student> studentQueryWrapper1 = new QueryWrapper<Student>().eq("order_number", orderNumber);
                Student student = studentMapper.selectOne(studentQueryWrapper1);
                if(student == null){
                    errText += "\t编号不正确，未查询到学生";
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                if (StringUtils.isNullOrEmpty(errText)) {

                    // 学生
                    StudentParent studentParent = new StudentParent();
                    studentParent.setStudentId(student.getId());
                    studentParent.setName(parentName);
                    studentParent.setPhone(parentMobile);
                    switch (relation.trim()) {
                        case "父亲":
                            studentParent.setRelation(RelationEnum.F);
                            break;
                        case "母亲":
                            studentParent.setRelation(RelationEnum.M);
                            break;
                        case "爷爷":
                            studentParent.setRelation(RelationEnum.FGF);
                            break;
                        case "奶奶":
                            studentParent.setRelation(RelationEnum.FGM);
                            break;
                        case "外公":
                            studentParent.setRelation(RelationEnum.MGF);
                            break;
                        case "外婆":
                            studentParent.setRelation(RelationEnum.MGM);
                            break;
                        case "其他":
                            studentParent.setRelation(RelationEnum.O);
                            break;
                        default:
                            studentParent.setRelation(RelationEnum.O);
                            break;
                    }
                    studentParent.setDefault().validate(true);
                    StudentParent exist = this.baseMapper.selectOne(Wrappers.<StudentParent>lambdaQuery()
                            .eq(StudentParent::getStudentId,studentParent.getStudentId())
                            .eq(StudentParent::getRelation,studentParent.getRelation())
                    );
                    if(exist != null){
                        studentParent.setId(exist.getId());
                        this.baseMapper.updateById(exist);
                    }else{

                        this.baseMapper.insert(studentParent);
                    }
                }
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
            }
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }

}
