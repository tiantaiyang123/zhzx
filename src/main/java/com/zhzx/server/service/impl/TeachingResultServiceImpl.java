/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.*;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.TeachingResultBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.TeachingResultService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.util.TwxUtils;
import com.zhzx.server.vo.TeachingResultAuditVo;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeachingResultServiceImpl extends ServiceImpl<TeachingResultMapper, TeachingResult> implements TeachingResultService {

    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private TeachingResultAttachmentMapper teachingResultAttachmentMapper;

    @Resource
    private TeachingResultBjryMapper teachingResultBjryMapper;

    @Resource
    private TeachingResultCbzcMapper teachingResultCbzcMapper;

    @Resource
    private TeachingResultClassifyMapper teachingResultClassifyMapper;

    @Resource
    private TeachingResultGrjshjMapper teachingResultGrjshjMapper;

    @Resource
    private TeachingResultGrryMapper teachingResultGrryMapper;

    @Resource
    private TeachingResultJtryMapper teachingResultJtryMapper;

    @Resource
    private TeachingResultJxpxMapper teachingResultJxpxMapper;

    @Resource
    private TeachingResultJzksMapper teachingResultJzksMapper;

    @Resource
    private TeachingResultKtyjMapper teachingResultKtyjMapper;

    @Resource
    private TeachingResultLwfbMapper teachingResultLwfbMapper;

    @Resource
    private TeachingResultLwpbhjMapper teachingResultLwpbhjMapper;

    @Resource
    private TeachingResultLzcbMapper teachingResultLzcbMapper;

    @Resource
    private TeachingResultSfkksMapper teachingResultSfkksMapper;

    @Resource
    private TeachingResultYkhjMapper teachingResultYkhjMapper;

    @Resource
    private TeachingResultHistoryMapper teachingResultHistoryMapper;

    @Resource
    private TeachingResultZdqnjsMapper teachingResultZdqnjsMapper;

    @Resource
    private TeachingResultZdxshjMapper teachingResultZdxshjMapper;

    @Resource
    private TeachingResultWkhjMapper teachingResultWkhjMapper;

    @Resource
    private MessageMapper messageMapper;

    @Override
    public int updateAllFieldsById(TeachingResult entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeachingResult add(Map<String, Object> params) {
        TeachingResult entity = new TeachingResult();
        // 自定义枚举类型转换
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
            @Override
            public Object convert(String value, Class clazz) {
                if (clazz.isEnum()) {
                    return Enum.valueOf(clazz, value);
                }
                return super.convert(value, clazz);
            }
        });

        try {
            beanUtilsBean.populate(entity, params);
        } catch (Exception e) {
            throw new ApiCode.ApiException(-1, "BEAN转换异常");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getStaffId() == 0) {
            // 管理员添加则直接通过
            entity.setState(TeachingResultStateEnum.PASSED);
            entity.setReviewerId(user.getStaffId());
        } else {
            entity.setTeacherId(user.getStaffId());
            entity.setState(TeachingResultStateEnum.PENDING_REVIEW);
        }
//        entity.validate(true);
        TeachingResultClassify currTeachingResultClassify = this.teachingResultClassifyMapper.selectById(entity.getResultClassifyId());
        if (currTeachingResultClassify == null || currTeachingResultClassify.getParentId() == 0) {
            throw new ApiCode.ApiException(-1, "分类ID错误");
        }
        // 本表添加一条记录
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        this.getBaseMapper().insert(entity);

        // 添加关联表
        String suffix = params.getOrDefault("suffix", "").toString();
        if ("".equals(suffix)) {
            throw new ApiCode.ApiException(-1, "细分后缀不能为空");
        }
        String totalName = "TeachingResult" + suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
        try {
            Class<?> c = Class.forName("com.zhzx.server.domain." + totalName);
            Object o = c.newInstance();
            beanUtilsBean.populate(o, params);
            Method method = c.getMethod("setTeachingResultId", Long.class);
            method.invoke(o, entity.getId());

            Class<?> c1 = Class.forName("com.zhzx.server.repository." + totalName + "Mapper");
            Object o1 = SpringUtils.getBean("t" + totalName.substring(1) + "Mapper");
            Method insert = c1.getMethod("insert", Object.class);
            // 第一个参数一定要写jdk动态代理实例
            insert.invoke(o1, o);
        } catch (Exception e1) {
            log.error("教学成果反射异常", e1);
            throw new ApiCode.ApiException(-1, "反射异常");
        }

        // 附件表添加记录
        String urls = params.getOrDefault("urls", "").toString();
        if (!"".equals(urls)) {
            List<TeachingResultAttachment> addList = new LinkedList<TeachingResultAttachment>();
            StringTokenizer st = new StringTokenizer(urls, "|", false);
            while (st.hasMoreElements()) {
                TeachingResultAttachment teachingResultAttachment = new TeachingResultAttachment();
                teachingResultAttachment.setTeachingResultId(entity.getId());
                teachingResultAttachment.setUrl(TwxUtils.upload_path_common_prefix + TwxUtils.upload_path_tmg_prefix + st.nextElement().toString());
                addList.add(teachingResultAttachment);
            }
            this.teachingResultAttachmentMapper.batchInsert(addList);
        }

        return this.getBaseMapper().selectById(entity.getId());
    }

    @Override
    public TeachingResult audit(TeachingResultAuditVo teachingResultAuditVo) {
        TeachingResult teachingResult = this.getBaseMapper().selectById(teachingResultAuditVo.getId());
        String reason = teachingResultAuditVo.getReason();
        String type = teachingResultAuditVo.getType();
        if (teachingResult == null) {
            throw new ApiCode.ApiException(-1, "数据不存在");
        }
        if (!TeachingResultStateEnum.PENDING_REVIEW.equals(teachingResult.getState())) {
            throw new ApiCode.ApiException(-1, "数据状态非待审核");
        }
        if (StringUtils.isNullOrEmpty(reason) && "reject".equals(type)) {
            throw new ApiCode.ApiException(-1, "拒绝原因不能为空");
        }
        if (!StringUtils.isNullOrEmpty(reason) && reason.length() > 255) {
            throw new ApiCode.ApiException(-1, "原因过长");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        teachingResult.setReason(reason);
        teachingResult.setUpdateTime(new Date());
        teachingResult.setReviewerId(user.getStaffId());
        teachingResult.setState("reject".equals(type) ? TeachingResultStateEnum.REJECTED : TeachingResultStateEnum.PASSED);
        this.getBaseMapper().updateAllFieldsById(teachingResult);
        return this.getBaseMapper().selectById(teachingResultAuditVo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeachingResult update(Map<String, Object> params) {
        if (!params.containsKey("id")) {
            throw new ApiCode.ApiException(-1, "ID不能为空");
        }
        TeachingResult entity = this.getBaseMapper().selectById(Long.valueOf(params.get("id").toString()));
        if (entity == null) {
            throw new ApiCode.ApiException(-1, "无效ID");
        }
        // 自定义枚举类型转换
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
            @Override
            public Object convert(String value, Class clazz) {
                if (clazz.isEnum()) {
                    return Enum.valueOf(clazz, value);
                }
                return super.convert(value, clazz);
            }
        });

        try {
            beanUtilsBean.populate(entity, params);
        } catch (Exception e) {
            throw new ApiCode.ApiException(-1, "BEAN转换异常");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getStaffId() != 0) {
            entity.setState(TeachingResultStateEnum.PENDING_REVIEW);
            entity.setTeacherId(user.getStaffId());
        }
//        entity.validate(true);
        TeachingResultClassify currTeachingResultClassify = this.teachingResultClassifyMapper.selectById(entity.getResultClassifyId());
        if (currTeachingResultClassify == null || currTeachingResultClassify.getParentId() == 0) {
            throw new ApiCode.ApiException(-1, "分类ID错误");
        }
        // 本表添加一条记录
        Date now = new Date();
        entity.setUpdateTime(now);
        this.getBaseMapper().updateById(entity);

        // 更新关联表
        Map<String, Object> queryMap = new HashMap<String, Object>() {
            {
                put("teaching_result_id", entity.getId());
            }
        };
        String suffix = params.getOrDefault("suffix", "").toString();
        if ("".equals(suffix)) {
            throw new ApiCode.ApiException(-1, "细分后缀不能为空");
        }
        String totalName = "TeachingResult" + suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
        try {
            // mapper
            Class<?> c1 = Class.forName("com.zhzx.server.repository." + totalName + "Mapper");
            // bean
            Object o1 = SpringUtils.getBean("t" + totalName.substring(1) + "Mapper");
            // pojo
            Class<?> c = Class.forName("com.zhzx.server.domain." + totalName);
            Method selectByMap = c1.getMethod("selectByMap", Map.class);
            Object res = selectByMap.invoke(o1, queryMap);
            if (res instanceof List) {
                Object first = ((List<?>) res).get(0);
                params.remove("id");
                beanUtilsBean.populate(first, params);
                Method updateById = c1.getMethod("updateById", Object.class);
                // 第一个参数一定要写jdk动态代理实例
                updateById.invoke(o1, first);
            }
        } catch (Exception e1) {
            log.error("教学成果反射异常", e1);
            throw new ApiCode.ApiException(-1, "反射异常");
        }

        String urls = params.getOrDefault("urls", "").toString();
        if (!"".equals(urls)) {
            // 附件表删除重新添加
            this.teachingResultAttachmentMapper.deleteByMap(queryMap);
            List<TeachingResultAttachment> addList = new LinkedList<TeachingResultAttachment>();
            StringTokenizer st = new StringTokenizer(urls, "|", false);
            while (st.hasMoreElements()) {
                TeachingResultAttachment teachingResultAttachment = new TeachingResultAttachment();
                teachingResultAttachment.setTeachingResultId(entity.getId());
                teachingResultAttachment.setUrl(TwxUtils.upload_path_common_prefix + TwxUtils.upload_path_tmg_prefix + st.nextElement().toString());
                addList.add(teachingResultAttachment);
            }
            this.teachingResultAttachmentMapper.batchInsert(addList);
        }

        return this.getBaseMapper().selectById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeachingResult updateAttach(String urls, Long id) {
        TeachingResult teachingResult = this.getBaseMapper().selectById(id);
        if (teachingResult == null) {
            throw new ApiCode.ApiException(-1, "教学成果不存在");
        }
        teachingResult.setUpdateTime(new Date());
        teachingResult.setState(TeachingResultStateEnum.PENDING_REVIEW);
        this.getBaseMapper().updateById(teachingResult);
        Map<String, Object> queryMap = new HashMap<String, Object>() {
            {
                put("teaching_result_id", id);
            }
        };
        this.teachingResultAttachmentMapper.deleteByMap(queryMap);
        if (!"".equals(urls.trim())) {
            // 附件表删除重新添加
            this.teachingResultAttachmentMapper.deleteByMap(queryMap);
            List<TeachingResultAttachment> addList = new LinkedList<TeachingResultAttachment>();
            StringTokenizer st = new StringTokenizer(urls, "|", false);
            while (st.hasMoreElements()) {
                TeachingResultAttachment teachingResultAttachment = new TeachingResultAttachment();
                teachingResultAttachment.setTeachingResultId(id);
                teachingResultAttachment.setUrl(TwxUtils.upload_path_common_prefix + TwxUtils.upload_path_tmg_prefix + st.nextElement().toString());
                addList.add(teachingResultAttachment);
            }
            this.teachingResultAttachmentMapper.batchInsert(addList);
        }
        return this.getBaseMapper().selectById(id);
    }

    @Override
    public IPage<TeachingResult> queryPage(IPage<TeachingResult> page, QueryWrapper<TeachingResult> wrapper) {
        return this.getBaseMapper().queryPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTogether(Long id) {
        TeachingResult teachingResult = this.getBaseMapper().selectById(id);
        if (teachingResult != null) {
            // 删除本表
            this.getBaseMapper().deleteById(id);
            // 关联表都删一遍
            Map<String, Object> deleteMap = new HashMap<String, Object>() {
                {
                    put("teaching_result_id", id);
                }
            };
            if (this.teachingResultHistoryMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultAttachmentMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultBjryMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultCbzcMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultGrjshjMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            if (this.teachingResultGrjshjMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultJzksMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultGrryMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultJtryMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultJxpxMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultKtyjMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultLwfbMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultLwpbhjMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultLzcbMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultSfkksMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultYkhjMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            if (this.teachingResultZdqnjsMapper.deleteByMap(deleteMap) != 0) {
                return;
            }
            ;
            this.teachingResultZdxshjMapper.deleteByMap(deleteMap);
        }
    }

    @Override
    public Map<String, Object> detail(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("bjry", this.teachingResultBjryMapper.selectOne(new QueryWrapper<TeachingResultBjry>().lambda().eq(TeachingResultBjry::getTeachingResultId, id)));
        params.put("cbzc", this.teachingResultCbzcMapper.selectOne(new QueryWrapper<TeachingResultCbzc>().lambda().eq(TeachingResultCbzc::getTeachingResultId, id)));
        params.put("grjshj", this.teachingResultGrjshjMapper.selectOne(new QueryWrapper<TeachingResultGrjshj>().lambda().eq(TeachingResultGrjshj::getTeachingResultId, id)));
        params.put("grry", this.teachingResultGrryMapper.selectOne(new QueryWrapper<TeachingResultGrry>().lambda().eq(TeachingResultGrry::getTeachingResultId, id)));
        params.put("jtry", this.teachingResultJtryMapper.selectOne(new QueryWrapper<TeachingResultJtry>().lambda().eq(TeachingResultJtry::getTeachingResultId, id)));
        params.put("jxpx", this.teachingResultJxpxMapper.selectOne(new QueryWrapper<TeachingResultJxpx>().lambda().eq(TeachingResultJxpx::getTeachingResultId, id)));
        params.put("jzks", this.teachingResultJzksMapper.selectOne(new QueryWrapper<TeachingResultJzks>().lambda().eq(TeachingResultJzks::getTeachingResultId, id)));
        params.put("ktyj", this.teachingResultKtyjMapper.selectOne(new QueryWrapper<TeachingResultKtyj>().lambda().eq(TeachingResultKtyj::getTeachingResultId, id)));
        params.put("lwfb", this.teachingResultLwfbMapper.selectOne(new QueryWrapper<TeachingResultLwfb>().lambda().eq(TeachingResultLwfb::getTeachingResultId, id)));
        params.put("lwpbhj", this.teachingResultLwpbhjMapper.selectOne(new QueryWrapper<TeachingResultLwpbhj>().lambda().eq(TeachingResultLwpbhj::getTeachingResultId, id)));
        params.put("lzcb", this.teachingResultLzcbMapper.selectOne(new QueryWrapper<TeachingResultLzcb>().lambda().eq(TeachingResultLzcb::getTeachingResultId, id)));
        params.put("sfkks", this.teachingResultSfkksMapper.selectOne(new QueryWrapper<TeachingResultSfkks>().lambda().eq(TeachingResultSfkks::getTeachingResultId, id)));
        params.put("ykhj", this.teachingResultYkhjMapper.selectOne(new QueryWrapper<TeachingResultYkhj>().lambda().eq(TeachingResultYkhj::getTeachingResultId, id)));
        params.put("zdqnjs", this.teachingResultZdqnjsMapper.selectOne(new QueryWrapper<TeachingResultZdqnjs>().lambda().eq(TeachingResultZdqnjs::getTeachingResultId, id)));
        params.put("zdxshj", this.teachingResultZdxshjMapper.selectOne(new QueryWrapper<TeachingResultZdxshj>().lambda().eq(TeachingResultZdxshj::getTeachingResultId, id)));
        return params;
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
    public boolean saveBatch(Collection<TeachingResult> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeachingResultBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    private Map<String, Object> tidyName(Map<String, Object> params) {
        if (params.containsKey("rych"))
            params.put("name", params.get("rych"));
        if (params.containsKey("zzmc"))
            params.put("name", params.get("zzmc"));
        if (params.containsKey("bsmc"))
            params.put("name", params.get("bsmc"));
        if (params.containsKey("bsmc"))
            params.put("name", params.get("bsmc"));
        if (params.containsKey("pxmc"))
            params.put("name", params.get("pxmc"));
        if (params.containsKey("jzmc"))
            params.put("name", params.get("jzmc"));
        if (params.containsKey("ktmc"))
            params.put("name", params.get("ktmc"));
        if (params.containsKey("lwbt"))
            params.put("name", params.get("lwbt"));
        if (params.containsKey("hjqk"))
            params.put("name", params.get("hjqk"));
        if (params.containsKey("wkmc"))
            params.put("name", params.get("wkmc"));
        return params;
    }

    // 自定义枚举类型转换
    private BeanUtilsBean bean = new BeanUtilsBean(new ConvertUtilsBean() {
        @Override
        public Object convert(String value, Class clazz) {
            if (clazz.isEnum()) {
                return Enum.valueOf(clazz, value);
            }
            return super.convert(value, clazz);
        }
    });

    /**
     * 添加成果
     *
     * @param params
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addResult(Map<String, Object> params) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        // 更新主表 name 值
        params = this.tidyName(params);

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getStaffId() == null || user.getStaffId() <= 0) {
            throw new ApiCode.ApiException(-1, "只有老师才能添加成果");
        }
        // 插入主表数据
        TeachingResult entity = new TeachingResult();
        bean.populate(entity, params);
        if (entity.getTeacherId() == null)
            entity.setTeacherId(user.getStaffId());
        entity.setState(TeachingResultStateEnum.PENDING_REVIEW);
        entity.setEditorId(user.getStaffId());
        entity.setEditorName(user.getRealName());
        AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectById(entity.getAcademicYearSemesterId());
        entity.setYear(academicYearSemester.getYear());
        entity.setSemester(academicYearSemester.getSemester());
        entity.setDefault();
        entity.validate(true);
        this.getBaseMapper().insert(entity);
        // 插入从表数据
        params.put("teachingResultId", entity.getId());
        String schema = params.get("schema").toString();
        Class<?> schemaClazz = Class.forName("com.zhzx.server.domain." + schema);
        BaseDomain subEntity = (BaseDomain) schemaClazz.newInstance();
        bean.populate(subEntity, params);
        Class<?> mapperClazz = Class.forName("com.zhzx.server.repository." + schema + "Mapper");
        Object mapperBean = SpringUtils.getBean(schema.substring(0, 1).toLowerCase() + schema.substring(1) + "Mapper");
        Method insert = mapperClazz.getMethod("insert", Object.class);
        insert.invoke(mapperBean, subEntity);
        // 附件表添加记录
        String urls = params.getOrDefault("attachmentUrls", "").toString();
        if (!StringUtils.isNullOrEmpty(urls)) {
            List<TeachingResultAttachment> attachmentList = new ArrayList<>();
            for (String url : urls.split(",")) {
                TeachingResultAttachment teachingResultAttachment = new TeachingResultAttachment();
                teachingResultAttachment.setTeachingResultId(entity.getId());
                teachingResultAttachment.setUrl(url);
                attachmentList.add(teachingResultAttachment);
            }
            this.teachingResultAttachmentMapper.batchInsert(attachmentList);
        }
    }

    /**
     * 修改成果
     *
     * @param params
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResult(Map<String, Object> params, boolean updateAllFields) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        // 更新主表 name 值
        params = this.tidyName(params);
        // 更新主表数据
        TeachingResult entity = this.getBaseMapper().selectById(Long.parseLong(params.get("id").toString()));
        if (entity == null) {
            throw new ApiCode.ApiException(-1, "该成果不存在");
        }
        if (entity.getState() == TeachingResultStateEnum.PASSED) {
            throw new ApiCode.ApiException(-1, "该成果已审核通过,不可修改!");
        }
        bean.populate(entity, params);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        entity.setEditorId(user.getStaffId());
        entity.setEditorName(user.getRealName());
        entity.setState(TeachingResultStateEnum.PENDING_REVIEW);
        entity.setReason(" ");
        entity.setUpdateTime(new Date());
        AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectById(entity.getAcademicYearSemesterId());
        entity.setYear(academicYearSemester.getYear());
        entity.setSemester(academicYearSemester.getSemester());
        if (updateAllFields) {
            this.getBaseMapper().updateAllFieldsById(entity);
        } else {
            this.getBaseMapper().updateById(entity);
        }
        // 更新从表数据
        params.put("teachingResultId", entity.getId());
        params.put("id", params.get("subId"));
        String schema = params.get("schema").toString();
        Class<?> schemaClazz = Class.forName("com.zhzx.server.domain." + schema);
        Object subEntity = schemaClazz.newInstance();
        bean.populate(subEntity, params);
        Class<?> mapperClazz = Class.forName("com.zhzx.server.repository." + schema + "Mapper");
        Object mapperBean = SpringUtils.getBean(schema.substring(0, 1).toLowerCase() + schema.substring(1) + "Mapper");
        if (updateAllFields) {
            Method update = mapperClazz.getMethod("updateAllFieldsById", Object.class);
            update.invoke(mapperBean, subEntity);
        } else {
            Method update = mapperClazz.getMethod("updateById", Object.class);
            update.invoke(mapperBean, subEntity);
        }
        // 更新附件表记录
        QueryWrapper<TeachingResultAttachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teaching_result_id", entity.getId());
        this.teachingResultAttachmentMapper.delete(queryWrapper);
        String urls = params.getOrDefault("attachmentUrls", "").toString();
        if (!StringUtils.isNullOrEmpty(urls)) {
            List<TeachingResultAttachment> attachmentList = new ArrayList<>();
            for (String url : urls.split(",")) {
                TeachingResultAttachment teachingResultAttachment = new TeachingResultAttachment();
                teachingResultAttachment.setTeachingResultId(entity.getId());
                teachingResultAttachment.setUrl(url);
                attachmentList.add(teachingResultAttachment);
            }
            this.teachingResultAttachmentMapper.batchInsert(attachmentList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResult(Long id, String schema) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        TeachingResult entity = this.getBaseMapper().selectById(id);
        if (entity == null) {
            throw new ApiCode.ApiException(-1, "该成果不存在");
        }
        if (entity.getState() == TeachingResultStateEnum.PASSED) {
            throw new ApiCode.ApiException(-1, "该成果已审核通过,不可删除!");
        }
        // 删除主表
        this.getBaseMapper().deleteById(id);
        // 删除从表
        Map<String, Object> deleteMap = new HashMap<String, Object>() {
            {
                put("teaching_result_id", id);
            }
        };
        BaseMapper<Class<?>> baseMapper = SpringUtils.getBean(schema.substring(0, 1).toLowerCase() + schema.substring(1) + "Mapper");
        baseMapper.deleteByMap(deleteMap);
        // 删除附件表记录
        QueryWrapper<TeachingResultAttachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teaching_result_id", entity.getId());
        this.teachingResultAttachmentMapper.delete(queryWrapper);

        //删除消息
        this.messageMapper.delete(Wrappers.<Message>lambdaQuery()
                .eq(Message::getName, "teachingResult_" + entity.getId())
                .eq(Message::getMessageTaskId, -2)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditResult(TeachingResult entity) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        entity.setReviewerId(user.getStaffId());
        TeachingResult teachingResult = this.getBaseMapper().selectById(entity.getId());
        if (teachingResult.getState() != TeachingResultStateEnum.PENDING_REVIEW) {
            throw new ApiCode.ApiException(-1, "该成果已审核过,不可再次审核!");
        }
        this.updateById(entity);
        messageMapper.update(new Message(), Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsRead, YesNoEnum.YES)
                .set(Message::getIsWrite, YesNoEnum.YES)
                .eq(Message::getName, "teachingResult_" + entity.getId())
        );
    }

    @Override
    public XSSFWorkbook exportExcel(QueryWrapper<TeachingResult> wrapper) throws IOException, InvalidFormatException {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        List<TeachingResult> teachingResults = this.list(wrapper);
        if (CollectionUtils.isNotEmpty(teachingResults)) {
            XSSFCell cell;
            XSSFRow row;
            for (TeachingResult teachingResult : teachingResults) {
                String name = teachingResult.getResultClassify().getName();
                XSSFSheet sheet = book.getSheet(name);
                if (sheet == null) {
                    sheet = book.createSheet(name);
                }
                if (name.equals("个人荣誉")) {
                    TeachingResultGrry obj = this.teachingResultGrryMapper.selectOne(Wrappers.<TeachingResultGrry>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("荣誉称号");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("发证单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getFzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("集体荣誉")) {
                    TeachingResultJtry obj = this.teachingResultJtryMapper.selectOne(Wrappers.<TeachingResultJtry>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("荣誉称号");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("发证单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getFzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("论文发表")) {
                    TeachingResultLwfb obj = this.teachingResultLwfbMapper.selectOne(Wrappers.<TeachingResultLwfb>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("论文标题");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("ISSN刊号");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("ISSN书号");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("CN刊号");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("期刊名称");

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(10, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getIssnkh());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getIssnsh());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getCnkh());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getQkmc());

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("论文评比获奖")) {
                    TeachingResultLwpbhj obj = this.teachingResultLwpbhjMapper.selectOne(Wrappers.<TeachingResultLwpbhj>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("论文标题");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("获奖情况");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getHjqk());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("论著出版")) {
                    TeachingResultLzcb obj = this.teachingResultLzcbMapper.selectOne(Wrappers.<TeachingResultLzcb>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("著作名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("ISBN书号");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("出版社");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("完成字数及比例");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getIsbnsh());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getCbs());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getWczsjbl());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("参编教材")) {
                    TeachingResultCbzc obj = this.teachingResultCbzcMapper.selectOne(Wrappers.<TeachingResultCbzc>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("著作名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("ISBN书号");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("出版社");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("完成字数及比例");

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(10, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getIsbnsh());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getCbs());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getWczsjbl());

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("课题研究")) {
                    TeachingResultKtyj obj = this.teachingResultKtyjMapper.selectOne(Wrappers.<TeachingResultKtyj>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("课题名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("课题编号");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("参与方式");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("研究状态");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("开题时间");

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("结题时间");

                        cell = row.createCell(10, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(11, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getKtbh());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getCyfs().getName());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getYjzt().getName());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getKtsj());

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getJtsj());

                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(11, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("示范课、研究课开设")) {
                    TeachingResultSfkks obj = this.teachingResultSfkksMapper.selectOne(Wrappers.<TeachingResultSfkks>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("课题名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("优课获奖")) {
                    TeachingResultYkhj obj = this.teachingResultYkhjMapper.selectOne(Wrappers.<TeachingResultYkhj>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("课题名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("比赛名称");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getBsmc());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("讲座开设")) {
                    TeachingResultJzks obj = this.teachingResultJzksMapper.selectOne(Wrappers.<TeachingResultJzks>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("讲座名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("个人竞赛获奖")) {
                    TeachingResultGrjshj obj = this.teachingResultGrjshjMapper.selectOne(Wrappers.<TeachingResultGrjshj>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("比赛名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("获奖情况");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getHjqk());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("指导学生获奖")) {
                    TeachingResultZdxshj obj = this.teachingResultZdxshjMapper.selectOne(Wrappers.<TeachingResultZdxshj>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("比赛名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("获奖情况");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学生姓名");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学生班级");

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(10, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getHjqk());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getXsxm());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getXsbj());

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("指导青年教师")) {
                    TeachingResultZdqnjs obj = this.teachingResultZdqnjsMapper.selectOne(Wrappers.<TeachingResultZdqnjs>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("获奖情况");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师姓名");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("开始时间");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("结束时间");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getJsmc());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getKssj());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getJssj());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("进修培训")) {
                    TeachingResultJxpx obj = this.teachingResultJxpxMapper.selectOne(Wrappers.<TeachingResultJxpx>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("培训名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("班级荣誉")) {
                    TeachingResultBjry obj = this.teachingResultBjryMapper.selectOne(Wrappers.<TeachingResultBjry>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("荣誉称号");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("获奖名称");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("班级");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getHjmc());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getClazz() == null ? "" : obj.getClazz().getName());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                } else if (name.equals("微课获奖")) {
                    TeachingResultWkhj obj = this.teachingResultWkhjMapper.selectOne(Wrappers.<TeachingResultWkhj>query().eq("teaching_result_id", teachingResult.getId()));
                    if (sheet.getPhysicalNumberOfRows() == 0) {
                        row = sheet.createRow(0);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("教师名称");

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("学年学期");

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("成果时间");

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("级别");

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("微课名称");

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("活动名称");

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("获奖名次");

                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("组织单位");

                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("上传时间");

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue("状态");
                    }
                    row = sheet.createRow(sheet.getLastRowNum() + 1);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getTeacher().getName());

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getAcademicYearSemester().getYear().concat(teachingResult.getAcademicYearSemester().getSemester().getName()));

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getResultDate());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getLevel().getName());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getName());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getHdmc());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getHjmc());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(obj.getZzdw());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(DateUtils.format(teachingResult.getCreateTime(), "yyyy-MM-dd"));

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(teachingResult.getState().getName());
                }
            }
        }
        return book;
    }

    @Override
    public TeachingResult detailResult(Long teacherResultId) {
        messageMapper.update(new Message(), Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsRead, YesNoEnum.YES)
                .eq(Message::getName, "teachingResult_" + teacherResultId)
        );
        return this.baseMapper.selectById(teacherResultId);
    }

    private String handleString(String s) {
        s = s.replace("（", "(").replace("）", ")").trim();
        int idx = s.indexOf("(");
        if (idx > 0) {
            s = s.substring(0, idx);
        }
        return s;
    }

    private static String handleDate(String s) {
        Long millionMiles = Long.parseLong(s);
        return Instant.ofEpochSecond(millionMiles).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    @Value("${web.upload-path}")
    private String uploadPath;

    @Override
    @SneakyThrows
    public Object init(String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (file.exists()) {
            Workbook book = null;
            try {
                book = WorkbookFactory.create(file);
                Sheet sheet = book.getSheetAt(0);
                int rowNum = sheet.getPhysicalNumberOfRows();
                Map<String, Long> staffMap = this.staffMapper.selectList(Wrappers.emptyWrapper()).stream().collect(Collectors.toMap(Staff::getName, Staff::getId));
                Map<String, Long> classifyMap = this.teachingResultClassifyMapper.selectList(Wrappers.<TeachingResultClassify>lambdaQuery().gt(TeachingResultClassify::getParentId, 0))
                        .stream().collect(Collectors.toMap(item-> item.getName().concat(item.getParent().getName()), TeachingResultClassify::getId));
                // 读取单元格数据
                for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                    String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                    String deleted = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                    String c = this.handleString(CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(35)));
                    String f = this.handleString(CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(34))).trim();

                    String key = c + f;

                    if (!staffMap.containsKey(name) || "1".equals(deleted) || !classifyMap.containsKey(key)) {
                        continue;
                    }
                    String pass = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(28));
                    String files = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(37));
                    String resultDate = this.handleDate(CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3)));
                    String term = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(29));
                    if (StringUtils.isNullOrEmpty(term)) {
                        term = "20212";
                    }
                    String semester = "Q".concat(term.substring(term.length() - 1));
                    String year = term.substring(0, term.length() - 1);
                    AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>lambdaQuery()
                            .eq(AcademicYearSemester::getSemester, SemesterEnum.valueOf(semester))
                            .likeRight(AcademicYearSemester::getYear, year.concat("\\-")));
                    String comment = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(33));
                    String resId = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(38));
                    String[] arr = new String[3];
                    String level = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                    if (level.startsWith("市")) {
                        level = "市级";
                    }
                    if (StringUtils.isNullOrEmpty(level)) {
                        level = "校级";
                    }
                    arr[0] = level;
                    Map<String, Object> param = new HashMap<>();
                    param.put("teacherId", staffMap.get(name));
                    param.put("resultClassifyId", classifyMap.get(key));
                    param.put("resultDate", resultDate);
                    param.put("academicYearSemesterId", academicYearSemester.getId());
                    param.put("level", Arrays.stream(TeachingResultLevelEnum.values()).filter(item -> item.getName().equals(arr[0])).collect(Collectors.toList()).get(0));
                    param.put("reviewerId", 1L);
                    param.put("state", pass.equals("0") ? TeachingResultStateEnum.PASSED : TeachingResultStateEnum.REJECTED);
                    param.put("reason", comment.concat("_").concat(resId));
                    param.put("editorId", 1L);
                    param.put("editorName", "admin");
                    param.put("attachmentUrls", files);

                    String hjmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                    String rych = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                    String fzdw = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
                    String lwbt = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
                    String hjqk = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
                    String issn = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(10));
                    String zzmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(11));
                    String cbs = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(12));
                    String wczsjbl = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(13));
                    String zzdw = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(14));
                    String ktsj = this.handleDate(CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(15)));
                    String ktmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(16));
                    String ktbh = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(17));
                    String cyfs = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(18));
                    if (StringUtils.isNullOrEmpty(cyfs)) {
                        cyfs = "参与";
                    }
                    arr[1] = cyfs;
                    String yjzt = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(19));
                    if (StringUtils.isNullOrEmpty(yjzt)) {
                        yjzt = "研究中";
                    }
                    arr[2] = yjzt;
                    String jtsj = this.handleDate(CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(20)));
                    String jzmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(21));
                    String bsmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(22));
                    String xsxm = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(23));
                    String xsbj = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(24));
                    String jsmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(25));
                    String pxmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(26));
                    String qkmc = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(30));
                    String isbnsh = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(31));
                    String cnkh = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(32));

                    if ("班级荣誉".equals(c)) {
                        param.put("schema", "TeachingResultBjry");
                        param.put("rych", rych);
                        param.put("hjmc", hjmc);
                        param.put("clazzId", 0L);
                    } else if ("参编教材".equals(c)) {
                        param.put("schema", "TeachingResultCbzc");
                        param.put("zzmc", zzmc);
                        param.put("zzdw", zzdw);
                        param.put("isbnsh", isbnsh);
                        param.put("cbs", cbs);
                        param.put("wczsjbl", wczsjbl);
                    } else if ("个人竞赛获奖".equals(c)) {
                        param.put("schema", "TeachingResultGrjshj");
                        param.put("hjqk", hjqk);
                        param.put("zzdw", zzdw);
                        param.put("bsmc", bsmc);
                    } else if ("个人荣誉".equals(c)) {
                        param.put("schema", "TeachingResultGrry");
                        param.put("rych", rych);
                        param.put("fzdw", fzdw);
                    }else if ("集体荣誉".equals(c)) {
                        param.put("schema", "TeachingResultJtry");
                        param.put("rych", rych);
                        param.put("fzdw", fzdw);
                        param.put("hjmc", hjmc);
                    }else if ("进修培训".equals(c)) {
                        param.put("schema", "TeachingResultJxpx");
                        param.put("pxmc", pxmc);
                        param.put("zzdw", zzdw);
                    }else if ("讲座开设".equals(c)) {
                        param.put("schema", "TeachingResultJzks");
                        param.put("jzmc", jzmc);
                        param.put("zzdw", zzdw);
                    }else if ("课题研究".equals(c)) {
                        param.put("schema", "TeachingResultKtyj");
                        param.put("ktmc", ktmc);
                        param.put("ktbh", ktbh);
                        param.put("cyfs", Arrays.stream(KtyjCyfsEnum.values()).filter(item -> item.getName().equals(arr[1])).collect(Collectors.toList()).get(0));
                        param.put("yjzt", Arrays.stream(KtyjYjztEnum.values()).filter(item -> item.getName().equals(arr[2])).collect(Collectors.toList()).get(0));
                        param.put("zzdw", zzdw);
                        param.put("ktsj", ktsj);
                        param.put("jtsj", jtsj);
                    }else if ("论文发表".equals(c)) {
                        param.put("schema", "TeachingResultLwfb");
                        param.put("lwbt", lwbt);
                        param.put("cnkh", cnkh);
                        param.put("qkmc", qkmc);
                        param.put("issnsh", issn);
                        param.put("issnkh", issn);
                    }else if ("论文评比获奖".equals(c)) {
                        param.put("schema", "TeachingResultLwpbhj");
                        param.put("lwbt", lwbt);
                        param.put("hjqk", hjqk);
                        param.put("zzdw", zzdw);
                    }else if ("论著出版".equals(c)) {
                        param.put("schema", "TeachingResultLzcb");
                        param.put("zzmc", zzmc);
                        param.put("isbnsh", isbnsh);
                        param.put("cbs", cbs);
                        param.put("wczsjbl", wczsjbl);
                    }else if ("示范课、研究课开设".equals(c)) {
                        param.put("schema", "TeachingResultSfkks");
                        param.put("ktmc", ktmc);
                        param.put("zzdw", zzdw);
                    }else if ("微课获奖".equals(c)) {
                    }else if ("优课获奖".equals(c)) {
                        param.put("schema", "TeachingResultYkhj");
                        param.put("ktmc", ktmc);
                        param.put("zzdw", zzdw);
                        param.put("bsmc", bsmc);
                    }else if ("指导青年教师".equals(c)) {
                        param.put("schema", "TeachingResultZdqnjs");
                        param.put("hjqk", hjqk);
                        param.put("kssj", ktsj);
                        param.put("jssj", jtsj);
                        param.put("jsmc", jsmc);
                    }else if ("指导学生获奖".equals(c)) {
                        param.put("schema", "TeachingResultZdxshj");
                        param.put("hjqk", hjqk);
                        param.put("zzdw", zzdw);
                        param.put("bsmc", bsmc);
                        param.put("xsxm", xsxm);
                        param.put("xsbj", xsbj);
                    }
                    this.addResult(param);
                }
            } finally {
                if (book != null)
                    book.close();
                file.delete();
            }
        }
        return null;
    }
}
