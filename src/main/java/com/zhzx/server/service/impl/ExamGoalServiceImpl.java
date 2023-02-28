/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试目标表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.base.CharMatcher;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.MapResultHandlerDto;
import com.zhzx.server.dto.StaffLessonLeaderDto;
import com.zhzx.server.dto.exam.*;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.ExamGoalBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalClazzService;
import com.zhzx.server.service.ExamGoalService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.util.TwxUtils;
import com.zhzx.server.vo.ExamGoalMutateVo;
import com.zhzx.server.vo.ExamGoalVo;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExamGoalServiceImpl extends ServiceImpl<ExamGoalMapper, ExamGoal> implements ExamGoalService {

    @Resource
    private ExamResultMapper examResultMapper;

    @Resource
    private StaffLessonLeaderMapper staffLessonLeaderMapper;

    @Resource
    private StaffLessonTeacherMapper staffLessonTeacherMapper;

    @Resource
    private StaffClazzAdviserMapper staffClazzAdviserMapper;

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private ExamGoalClazzMapper examGoalClazzMapper;

    @Resource
    private SettingsMapper settingsMapper;

    @Resource
    private ExamMapper examMapper;

    @Resource
    private ExamGoalWarningMapper examGoalWarningMapper;

    @Override
    public int updateAllFieldsById(ExamGoal entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeAll(Long id) {
        ExamGoal examGoal = this.getOne(Wrappers.<ExamGoal>lambdaQuery().eq(ExamGoal::getId, id));
        if (examGoal != null) {
            this.getBaseMapper().deleteById(id);
            this.examGoalClazzMapper.delete(Wrappers.<ExamGoalClazz>lambdaQuery().eq(ExamGoalClazz::getExamGoalId, id));
            this.settingsMapper.delete(Wrappers.<Settings>lambdaQuery().likeRight(Settings::getCode, "GOAL\\_" + examGoal.getExamId() + "\\_" + id + "\\_"));
            this.examGoalWarningMapper.delete(Wrappers.<ExamGoalWarning>lambdaQuery()
                    .eq(ExamGoalWarning::getExamId, examGoal.getExamId())
                    .eq(ExamGoalWarning::getGoalId, id));
            return 1;
        }
        return 0;
    }

    private int getCount(List<ExamGoalClazzSubjectDto> list, String subjectName) {
        int count = 0;
        if ("语文".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getChineseMeetingCnt).sum();
        } else if ("数学".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getMathMeetingCnt).sum();
        } else if ("英语".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getEnglishMeetingCnt).sum();
        } else if ("物理".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getPhysicsMeetingCnt).sum();
        } else if ("历史".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getHistoryMeetingCnt).sum();
        } else if ("化学".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getChemistryMeetingCnt).sum();
        } else if ("生物".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getBiologyMeetingCnt).sum();
        } else if ("政治".equals(subjectName)) {
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getPhysicsMeetingCnt).sum();
        } else if ("地理".equals(subjectName)){
            count = list.stream().mapToInt(ExamGoalClazzSubjectDto::getGeographyMeetingCnt).sum();
        }
        return count;
    }

    @Override
    public Map<String, Object> goalStationSchool(Long examId, Long academicYearSemesterId, Long gradeId) {
        Map<String, Object> res = new HashMap<>();
        if (academicYearSemesterId == null)
            academicYearSemesterId = ((User) SecurityUtils.getSubject().getPrincipal()).getAcademicYearSemester().getId();

        // TODO: 2022/9/5 学年考试不对应会报错 这里暂时校验一下
        Exam exam = this.examMapper.selectById(examId);
        if (exam == null || !exam.getAcademicYearSemesterId().equals(academicYearSemesterId))
            return res;
        List<ExamGoalDto> examGoalDtoList = this.getDefault(examId, null);
        if (CollectionUtils.isEmpty(examGoalDtoList)) {
            examGoalDtoList = new ArrayList<>();
        }
        examGoalDtoList = examGoalDtoList.stream().filter(item -> item.getTransGoalScore() != null).collect(Collectors.toList());
        // 班级组
        List<Clazz> clazzList = clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                .eq(Clazz::getGradeId, gradeId)
                .eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId));
        List<Map<String, Object>> clazzGroupInfo = new ArrayList<>();
        res.put("clazzGroupInfo", clazzGroupInfo);
        if (CollectionUtils.isNotEmpty(clazzList)) {
            List<Long> idList = clazzList.stream().map(Clazz::getId).collect(Collectors.toList());
            List<StaffLessonTeacher> staffLessonTeacherList = this.staffLessonTeacherMapper.selectList(Wrappers.<StaffLessonTeacher>lambdaQuery()
                    .select(StaffLessonTeacher::getClazzId, StaffLessonTeacher::getStaffId)
                    .in(StaffLessonTeacher::getClazzId, idList)
                    .eq(StaffLessonTeacher::getIsCurrent, YesNoEnum.YES));
            List<StaffClazzAdviser> staffClazzAdviserList = this.staffClazzAdviserMapper.selectList(Wrappers.<StaffClazzAdviser>lambdaQuery()
                    .select(StaffClazzAdviser::getClazzId, StaffClazzAdviser::getStaffId)
                    .in(StaffClazzAdviser::getClazzId, idList)
                    .eq(StaffClazzAdviser::getIsCurrent, YesNoEnum.YES));
            if (CollectionUtils.isEmpty(staffLessonTeacherList))
                staffLessonTeacherList = new ArrayList<>();
            if (CollectionUtils.isEmpty(staffClazzAdviserList))
                staffClazzAdviserList = new ArrayList<>();
            Map<Long, List<StaffLessonTeacher>> map2 = staffLessonTeacherList.stream().collect(Collectors.groupingBy(StaffLessonTeacher::getClazzId));
            Map<Long, String> map3 = staffClazzAdviserList
                    .stream()
                    .collect(Collectors.groupingBy(StaffClazzAdviser::getClazzId, Collectors.mapping(o -> o.getStaffId().toString(), Collectors.joining(","))));
            clazzList.sort(Comparator.comparing(o -> Integer.parseInt(o.getName().substring(0, o.getName().length() - 1))));
            Map<Long, List<ExamGoalClazzTotalDto>> map = null;
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                List<ExamGoalClazzTotalDto> clazzGoalTotal = this.getBaseMapper().getClazzGoalTotal(null, examGoalDtoList);
                map = clazzGoalTotal.stream().collect(Collectors.groupingBy(ExamGoalClazzTotalDto::getClazzId));
            }
            for (Clazz item : clazzList) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("id", item.getId());
                map1.put("name", item.getName());
                map1.put("gradeId", item.getGradeId());
                map1.put("headTeacher", item.getHeadTeacher());
                map1.put("staffIds", map2.containsKey(item.getId()) ? map2.get(item.getId()).stream().map(o -> o.getStaffId().toString()).distinct().collect(Collectors.joining(",")) : "");
                map1.put("staffId", Long.parseLong(map3.getOrDefault(item.getId(), "0").split(",")[0]));
                map1.put("clazzNature", item.getClazzNature());
                if (CollectionUtils.isNotEmpty(map)) {
                    map.get(item.getId()).forEach(item1 -> map1.put("C" + item1.getId(), item1.getClazzMeetingCnt()));
                }
                clazzGroupInfo.add(map1);
            }
        }
        // 备课组
        List<Map<String, Object>> bkGroupInfo = new ArrayList<>();
        res.put("bkGroupInfo", bkGroupInfo);
        List<StaffLessonLeaderDto> staffLessonLeaderList = this.staffLessonLeaderMapper.selectLessonLeader(academicYearSemesterId, gradeId);
        if (CollectionUtils.isNotEmpty(staffLessonLeaderList)) {
            Map<Long, List<ExamGoalClazzSubjectDto>> map = null;
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                List<ExamGoalClazzSubjectDto> clazzGoalSubject = this.getBaseMapper().getClazzGoalSubject(null, examGoalDtoList);
                map = clazzGoalSubject.stream().collect(Collectors.groupingBy(ExamGoalClazzSubjectDto::getId));
            }
            for (StaffLessonLeaderDto item : staffLessonLeaderList) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("id", item.getId());
                map2.put("gradeId", item.getGradeId());
                map2.put("subjectId", item.getSubjectId());
                String staffIds = item.getStaffIds();
                map2.put("subjectCount", CharMatcher.is(',').countIn(staffIds) + 1);
                map2.put("subjectName", item.getSubjectName());
                map2.put("leaderName", item.getLeaderName());
                map2.put("staffId", item.getStaffId());
                map2.put("staffIds", staffIds);
                if (CollectionUtils.isNotEmpty(map)) {
                    map.forEach((k, v) -> map2.put("C" + k, this.getCount(v, item.getSubjectName())));
                }
                bkGroupInfo.add(map2);
            }
        }
        // 附加表头
        List<ExamGoalDto> columnInfo = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
            columnInfo = examGoalDtoList.stream().collect(Collectors
                    .collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(ExamGoalDto::getId))), ArrayList::new));
        }
        res.put("columnInfo", columnInfo);
        return res;
    }

    private BigDecimal getScore(List<BigDecimal> list, int count) {
        if (count <= 0)
            return BigDecimal.valueOf(150);

        int lastNotZero = list.size() - 1;
        for (; lastNotZero >= 0 && list.get(lastNotZero).compareTo(BigDecimal.ZERO) == 0; lastNotZero--);

        if (lastNotZero < 0)
            return BigDecimal.valueOf(150);
        if (lastNotZero + 1 <= count)
            return BigDecimal.valueOf(list.get(lastNotZero).setScale(0, RoundingMode.DOWN).intValue());

        int b = list.get(count - 1).setScale(0, RoundingMode.DOWN).intValue();
        if (list.get(count - 1).compareTo(list.get(count)) == 0)
            b += 1;
        return BigDecimal.valueOf(b);
    }

    @Override
    public List<ExamGoalDto> getDefault(Long examId, Long goalId) {
        List<Settings> settingsList = this.settingsMapper.selectList(Wrappers.<Settings>lambdaQuery()
                .likeRight(Settings::getCode, "GOAL\\_" + examId + "\\_"));
        if (CollectionUtils.isNotEmpty(settingsList)) {
            if (goalId != null) {
                settingsList = settingsList.stream().filter(item -> item.getCode().startsWith("GOAL_" + examId + "_" + goalId + "_")).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(settingsList)) {
                    return settingsList.stream().map(item -> JSONObject.parseObject(item.getParams(), ExamGoalDto.class)).collect(Collectors.toList());
                }
            }
            return settingsList.stream().map(item -> JSONObject.parseObject(item.getParams(), ExamGoalDto.class)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private int getGoalCount(BigDecimal base, boolean needFilter, String name, Map<Long, List<ExamResultSimpleDto>> data) {
        int all = 0;
        for (Map.Entry<Long, List<ExamResultSimpleDto>> entry : data.entrySet()) {
            List<ExamResultSimpleDto> values = entry.getValue();
            String otherDivision = values.get(0).getOtherDivision();
            if (needFilter && !otherDivision.contains(name)) continue;
            all += values.stream().filter(item -> item.getTotalWeightedScore().compareTo(base) >= 0).count();
        }
        return all;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ExamGoalDto> getAllGoal(Long examId, Long goalId) {
        List<ExamGoalDto> examGoalDtoList = this.getBaseMapper().getAllGoal(examId, goalId);
        if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
            examGoalDtoList = examGoalDtoList.stream().filter(o -> o.getTotalCount() > 0).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                Map<String, List<BigDecimal>[]> map = new HashMap<>();
                List<ExamResultSimpleDto> examResultSimpleDtoList = this.examResultMapper.getSimpleResult(examId, null);
                if (CollectionUtils.isNotEmpty(examResultSimpleDtoList)) {
                    examResultSimpleDtoList.forEach(item -> {
                        List<BigDecimal>[] arr = map.computeIfAbsent(item.getClazzNature().toString(), o -> new List[16]);
                        if (arr[0] == null) {
                            TwxUtils.fill(arr, ArrayList::new);
                        }
                        arr[0].add(item.getChineseScore());
                        arr[1].add(item.getMathScore());
                        arr[2].add(item.getEnglishScore());
                        arr[3].add(item.getPhysicsScore());
                        arr[4].add(item.getHistoryScore());
                        arr[5].add(item.getChemistryScore());
                        arr[6].add(item.getChemistryWeightedScore());
                        arr[7].add(item.getBiologyScore());
                        arr[8].add(item.getBiologyWeightedScore());
                        arr[9].add(item.getPoliticsScore());
                        arr[10].add(item.getPoliticsWeightedScore());
                        arr[11].add(item.getGeographyScore());
                        arr[12].add(item.getGeographyWeightedScore());
                        arr[13].add(item.getTotalWeightedScore());
                        arr[14].add(item.getThreeTotalScore());
                        arr[15].add(item.getFourTotalScore());
                    });
                    map.forEach((k, v) -> TwxUtils.arrSort(v, Comparator.reverseOrder()));

                    Map<String, Map<Long, List<ExamResultSimpleDto>>> clazzMap = examResultSimpleDtoList.stream().collect(Collectors.groupingBy(item -> item.getClazzNature().toString(), Collectors.groupingBy(ExamResultSimpleDto::getClazzId)));

                    examGoalDtoList.forEach(item -> {
                        item.setTransGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[13], item.getTotalCount()));
                        item.setThreeTotalGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[14], item.getTotalCount()));
                        item.setFourTotalGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[15], item.getTotalCount()));

                        // 学科目标数已达成总分目标的人数为准， 同时如果该班级不上此科目，则不累加
                        BigDecimal base = item.getTransGoalScore();
                        Map<Long, List<ExamResultSimpleDto>> data = clazzMap.get(item.getSubjectType());

                        int noWeightedSubjectCnt = this.getGoalCount(base, false, null, data);

                        item.setChineseGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[0], noWeightedSubjectCnt));
                        item.setMathGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[1], noWeightedSubjectCnt));
                        item.setEnglishGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[2], noWeightedSubjectCnt));
                        item.setPhysicsGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[3], noWeightedSubjectCnt));
                        item.setHistoryGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[4], noWeightedSubjectCnt));

                        List<BigDecimal> list = map.get(item.getSubjectType())[5];
                        int cnt = Math.min(this.getGoalCount(base, true, "化学", data), (int)list.stream().filter(i -> i.compareTo(BigDecimal.ZERO) > 0).count());
                        item.setChemistryGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[5], cnt));
                        item.setChemistryWeightedGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[6], cnt));

                        list = map.get(item.getSubjectType())[7];
                        cnt = Math.min(this.getGoalCount(base, true, "生物", data), (int)list.stream().filter(i -> i.compareTo(BigDecimal.ZERO) > 0).count());
                        item.setBiologyGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[7], cnt));
                        item.setBiologyWeightedGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[8], cnt));

                        list = map.get(item.getSubjectType())[9];
                        cnt = Math.min(this.getGoalCount(base, true, "政治", data), (int)list.stream().filter(i -> i.compareTo(BigDecimal.ZERO) > 0).count());
                        item.setPoliticsGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[9], cnt));
                        item.setPoliticsWeightedGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[10], cnt));

                        list = map.get(item.getSubjectType())[11];
                        cnt = Math.min(this.getGoalCount(base, true, "地理", data), (int)list.stream().filter(i -> i.compareTo(BigDecimal.ZERO) > 0).count());
                        item.setGeographyGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[11], cnt));
                        item.setGeographyWeightedGoalScore(CollectionUtils.isEmpty(map) ? BigDecimal.valueOf(150) : this.getScore(map.get(item.getSubjectType())[12], cnt));
                    });
                }
            }
        }
        return examGoalDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamGoal> addUpdateTotal(Long examId, List<ExamGoal> examGoalList) {
        // 数据初始化
        String str = new JSONObject(new HashMap<String, Object>() {
            {
                put("goalReference", new JSONArray());
            }
        }).toString();

        QueryWrapper<ExamGoal> wrapper = Wrappers.<ExamGoal>query().eq("exam_id", examId);

        if (examGoalList.size() == 1 && examGoalList.get(0).getId() == null) {
            // 未分文理新增
            ExamGoal examGoalCurr = examGoalList.get(0);
            examGoalCurr.setCreateTime(new Date());
            examGoalCurr.setUpdateTime(new Date());
            examGoalCurr.setGoalReference(str);
            examGoalCurr.validate(true);
            this.getBaseMapper().insert(examGoalCurr);
        } else if (examGoalList.size() == 2 && examGoalList.get(0).getId() == null && examGoalList.get(1).getId() == null){
            for (ExamGoal examGoal : examGoalList) {
                examGoal.setCreateTime(new Date());
                examGoal.setUpdateTime(new Date());
                examGoal.setGoalReference(str);
                examGoal.validate(true);
            }
            this.getBaseMapper().batchInsert(examGoalList);
        } else {
            // 获取此次目标 对应的各班目标
            MapResultHandlerDto<Long, String> mapResultHandlerDto = new MapResultHandlerDto<>();
            this.getBaseMapper().getGoalForEachClazz(examId, mapResultHandlerDto);
            Map<Long, String> map = mapResultHandlerDto.getMapRes();
            for (ExamGoal examGoal : examGoalList) {
                examGoal.setGoalReference(map.getOrDefault(examGoal.getId() == null ? 0L : examGoal.getId(), str));
                examGoal.setCreateTime(new Date());
                examGoal.setUpdateTime(new Date());
                examGoal.validate(true);
            }
            // 删除所有目标
            this.getBaseMapper().delete(wrapper);
            // 重新添加
            this.getBaseMapper().batchInsert(examGoalList);
        }
        return this.getBaseMapper().selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamGoal addUpdateEach(ExamGoalVo examGoalVo) {
        if (examGoalVo.getId() == null) {
            throw new ApiCode.ApiException(500, "ID与各班参考不能为空");
        }

        JSONObject jo = examGoalVo.getJo();
        if (jo == null || !jo.containsKey("goalReference") || !(jo.get("goalReference") instanceof JSONArray || jo.get("goalReference") instanceof List)) {
            throw new ApiCode.ApiException(500, "数据格式不匹配");
        }

        ExamGoal currExamGoal = this.getBaseMapper().selectById(examGoalVo.getId());
        if (currExamGoal == null) {
            throw new ApiCode.ApiException(500, "目标不存在");
        }
        currExamGoal.setUpdateTime(new Date());
        currExamGoal.setGoalReference(jo.toString());
        this.getBaseMapper().updateById(currExamGoal);
        return this.getBaseMapper().selectById(examGoalVo.getId());
    }

    @Override
    public List<Map<String, Object>> goalStation(Long examId, Long goalId, Long clazzId) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<ExamGoalDto> examGoalDtoList = this.getDefault(examId, goalId);
        if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
            List<ExamGoalWorkBenchDto> examGoalWorkBenchDtoList = this.getBaseMapper().getClazzGoalWorkBench(null, examGoalDtoList);
            examGoalWorkBenchDtoList.sort((o1, o2) -> {
                Integer s1 = Integer.parseInt(o1.getClazzName().substring(0, o1.getClazzName().length() - 1));
                Integer s2 = Integer.parseInt(o2.getClazzName().substring(0, o2.getClazzName().length() - 1));
                if (s1.equals(s2)) {
                    return o1.getId().compareTo(o2.getId());
                }
                return s1.compareTo(s2);
            });
            examGoalWorkBenchDtoList.forEach(item -> {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("clazzId", item.getClazzId());
                map1.put("clazzName", item.getClazzName());
                map1.put("targetName", item.getTargetName());
                map1.put("targetId", item.getId());
                map1.put("totalMeetingCnt", item.getClazzMeetingCnt());
                map1.put("chineseMeetingCnt", item.getChineseMeetingCnt());
                map1.put("mathMeetingCnt", item.getMathMeetingCnt());
                map1.put("englishMeetingCnt", item.getEnglishMeetingCnt());
                map1.put("physicsMeetingCnt", item.getPhysicsMeetingCnt());
                map1.put("chemistryMeetingCnt", item.getChemistryMeetingCnt());
                map1.put("chemistryWeightedMeetingCnt", item.getChemistryWeightedMeetingCnt());
                map1.put("biologyMeetingCnt", item.getBiologyMeetingCnt());
                map1.put("biologyWeightedMeetingCnt", item.getBiologyWeightedMeetingCnt());
                map1.put("historyMeetingCnt", item.getHistoryMeetingCnt());
                map1.put("politicsMeetingCnt", item.getPoliticsMeetingCnt());
                map1.put("politicsWeightedMeetingCnt", item.getPoliticsWeightedMeetingCnt());
                map1.put("geographyMeetingCnt", item.getGeographyMeetingCnt());
                map1.put("geographyWeightedMeetingCnt", item.getGeographyWeightedMeetingCnt());
                map1.put("shouldMeetingCnt", item.getShouldMeetingCnt());
                res.add(map1);
            });
        }
        return res;
    }

    @Override
    public Map<String, Object> getGoalTotal(Long clazzId, Long examId, List<ExamGoalDto> examGoalDtoList) {
        Map<String, Object> params = new HashMap<>();
        Map<String, List<ExamGoalDto>> goalTotalColumns = examGoalDtoList.stream().collect(Collectors.groupingBy(ExamGoalDto::getSubjectType));
        goalTotalColumns.forEach((k, v) -> v.sort(Comparator.comparing(ExamGoalDto::getId)));
        params.put("goalTotalColumns", goalTotalColumns);
        List<ExamGoalClazzTotalDto> clazzGoalTotal = this.getBaseMapper().getClazzGoalTotal(clazzId, examGoalDtoList);
        // 计算比例及排名
        Map<String, List<BigDecimal>> goalEnumListMap = new HashMap<>();
        for (ExamGoalClazzTotalDto item : clazzGoalTotal) {
            if (item.getShouldMeetingCnt() == 0) {
                item.setRatio(BigDecimal.ZERO);
            } else {
                item.setRatio(BigDecimal.valueOf(item.getClazzMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(item.getShouldMeetingCnt()), 1, RoundingMode.HALF_UP));
            }
            goalEnumListMap.computeIfAbsent(item.getSubjectType().toString().concat(item.getTargetName()), o -> new ArrayList<>()).add(item.getRatio());
        }
        goalEnumListMap.forEach((k, v) -> v.sort(Comparator.reverseOrder()));
        for (ExamGoalClazzTotalDto item : clazzGoalTotal) {
            item.setPm(TwxUtils.binarySearchEnhance1(goalEnumListMap.get(item.getSubjectType().toString().concat(item.getTargetName())), item.getRatio()));
        }
        // 各班目标情况
        Map<ClazzNatureEnum, List<ExamGoalTotalCombineDto>> goalTotalList = new HashMap<>();
        Map<Long, List<ExamGoalClazzTotalDto>> clazzTotalMap = clazzGoalTotal.stream().collect(Collectors.groupingBy(ExamGoalClazzTotalDto::getClazzId));
        Map<ClazzNatureEnum, ExamGoalTotalCombineDto> sumMap = new HashMap<>();
        clazzTotalMap.forEach((k, v) -> {
            ExamGoalTotalCombineDto examGoalTotalCombineDto = new ExamGoalTotalCombineDto();
            ExamGoalClazzTotalDto curr = v.get(0);
            examGoalTotalCombineDto.setGoals(new HashMap<>());
            examGoalTotalCombineDto.setClazzId(k);
            examGoalTotalCombineDto.setClazzName(curr.getClazzName());
            examGoalTotalCombineDto.setStudentCnt(curr.getStudentCnt());
            examGoalTotalCombineDto.setJoinCnt(curr.getJoinCnt());
            examGoalTotalCombineDto.setClazzNature(curr.getClazzNature());
            goalTotalList.computeIfAbsent(curr.getClazzNature(), o -> new ArrayList<>()).add(examGoalTotalCombineDto);
            ExamGoalTotalCombineDto sumDto = sumMap.computeIfAbsent(curr.getClazzNature(), o -> new ExamGoalTotalCombineDto());
            sumDto.setJoinCnt(sumDto.getJoinCnt() + curr.getJoinCnt());
            sumDto.setStudentCnt(sumDto.getStudentCnt() + curr.getStudentCnt());
            v.sort(Comparator.comparing(ExamGoalClazzTotalDto::getClazzId));
            v.forEach(item -> {
                ExamGoalForClazzDto curr1 = new ExamGoalForClazzDto();
                examGoalTotalCombineDto.getGoals().put(item.getTargetName(), curr1);
                curr1.setPm(item.getPm());
                curr1.setRatio(item.getRatio());
                curr1.setClazzMeetingCnt(item.getClazzMeetingCnt());
                curr1.setShouldMeetingCnt(item.getShouldMeetingCnt());
                if (sumDto.getGoals() == null) {
                    sumDto.setGoals(new HashMap<>());
                }
                ExamGoalForClazzDto curr2 = sumDto.getGoals().computeIfAbsent(item.getTargetName(), o -> new ExamGoalForClazzDto());
                curr2.setClazzMeetingCnt(curr2.getClazzMeetingCnt() + item.getClazzMeetingCnt());
                curr2.setShouldMeetingCnt(curr2.getShouldMeetingCnt() + item.getShouldMeetingCnt());
            });
        });
        params.put("goalTotalList", goalTotalList);
        // 表头类型展示顺序
        params.put("columnsTypeList", goalTotalList.keySet());
        // 计算总和
        sumMap.forEach((k, v) -> {
            v.setClazzNature(k);
            if (k.equals(ClazzNatureEnum.OTHER)) {
                v.setClazzName(ClazzNatureEnum.OTHER.getName());
                v.setClazzId(-1L);
            } else if (k.equals(ClazzNatureEnum.SCIENCE)) {
                v.setClazzName(ClazzNatureEnum.SCIENCE.getName());
                v.setClazzId(-2L);
            } else {
                v.setClazzName(ClazzNatureEnum.LIBERAL.getName());
                v.setClazzId(-3L);
            }
            v.getGoals().forEach((k1, item) -> item.setRatio(BigDecimal.valueOf(item.getClazzMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(item.getShouldMeetingCnt()), 1, RoundingMode.HALF_UP)));
            goalTotalList.get(k).add(v);
        });
        return params;
    }

    @Override
    public Map<String, Object> getGoalSubject(Long clazzId, Long examId, List<ExamGoalDto> examGoalDtoList) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Map<String, List<ExamGoalDto>>> goalSubjectColumns = examGoalDtoList.stream().collect(Collectors.groupingBy(ExamGoalDto::getName, Collectors.groupingBy(ExamGoalDto::getSubjectType)));
        params.put("goalSubjectColumns", goalSubjectColumns);
        List<ExamGoalClazzSubjectDto> clazzGoalSubject = this.getBaseMapper().getClazzGoalSubject(clazzId, examGoalDtoList);
        Map<String, Map<ClazzNatureEnum, List<ExamGoalClazzSubjectDto>>> clazzTotalMap = clazzGoalSubject.stream().collect(Collectors.groupingBy(ExamGoalClazzSubjectDto::getTargetName, Collectors.groupingBy(ExamGoalClazzSubjectDto::getClazzNature)));
        // 计算总和
        clazzTotalMap.forEach((k, v) -> v.forEach((k1, v1) -> {
            ExamGoalClazzSubjectDto sum = new ExamGoalClazzSubjectDto();
            if (k1.equals(ClazzNatureEnum.OTHER)) {
                sum.setClazzName(ClazzNatureEnum.OTHER.getName());
                sum.setClazzId(-1L);
            } else if (k1.equals(ClazzNatureEnum.SCIENCE)) {
                sum.setClazzName(ClazzNatureEnum.SCIENCE.getName());
                sum.setClazzId(-2L);
            } else {
                sum.setClazzName(ClazzNatureEnum.LIBERAL.getName());
                sum.setClazzId(-3L);
            }
            sum.setShouldMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getShouldMeetingCnt).sum());
            sum.setChineseMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getChineseMeetingCnt).sum());
            sum.setMathMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getMathMeetingCnt).sum());
            sum.setEnglishMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getEnglishMeetingCnt).sum());
            sum.setHistoryMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getHistoryMeetingCnt).sum());
            sum.setPhysicsMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getPhysicsMeetingCnt).sum());
            sum.setChemistryMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getChemistryMeetingCnt).sum());
            sum.setChemistryWeightedMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getChemistryWeightedMeetingCnt).sum());
            sum.setBiologyMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getBiologyMeetingCnt).sum());
            sum.setBiologyWeightedMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getBiologyWeightedMeetingCnt).sum());
            sum.setPoliticsMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getPoliticsMeetingCnt).sum());
            sum.setPoliticsWeightedMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getPoliticsWeightedMeetingCnt).sum());
            sum.setGeographyMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getGeographyMeetingCnt).sum());
            sum.setGeographyWeightedMeetingCnt(v1.stream().mapToInt(ExamGoalClazzSubjectDto::getGeographyWeightedMeetingCnt).sum());
            v1.sort(Comparator.comparing(ExamGoalClazzSubjectDto::getClazzId));
            v1.add(sum);
        }));
        params.put("goalSubjectMap", clazzTotalMap);
        return params;
    }

    @Override
    public int updateTotalGoalScoreCache(ExamGoalMutateVo examGoalMutateVo) {
        Long examId = examGoalMutateVo.getExamId();
        String subjectType = examGoalMutateVo.getSubjectType();
        List<ExamGoalMutateTotalDto> examGoalMutateTotalDtoList = examGoalMutateVo.getExamGoalMutateTotalDtoList();
        if (CollectionUtils.isEmpty(examGoalMutateTotalDtoList))
            return -1;
        examGoalMutateTotalDtoList.forEach(examGoalMutateTotalDto -> {
            Long goalId = examGoalMutateTotalDto.getGoalId();
            String goalName = examGoalMutateTotalDto.getGoalName();
            ExamGoal examGoal = this.baseMapper.selectOne(Wrappers.<ExamGoal>lambdaQuery()
                    .eq(ExamGoal::getExamId, examId)
                    .and(wrapper -> wrapper.eq(ExamGoal::getId, goalId).or().eq(ExamGoal::getName, goalName)));
            if (examGoal == null)
                throw new ApiCode.ApiException(-5, "无法获取对应目标");

            Settings settings = this.settingsMapper.selectOne(Wrappers.<Settings>lambdaQuery()
                    .likeRight(Settings::getCode, "GOAL\\_" + examId + "\\_" + goalId + "\\_" + subjectType));
            if (settings == null)
                throw new ApiCode.ApiException(-5, "无法获取对应缓存");

            ExamGoalDto examGoalDto = JSONObject.parseObject(settings.getParams(), ExamGoalDto.class);

            if (examGoalMutateTotalDto.getScore() != null)
                examGoalDto.setTransGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getScore()));
            if (examGoalMutateTotalDto.getChineseGoalScore() != null)
                examGoalDto.setChineseGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getChineseGoalScore()));
            if (examGoalMutateTotalDto.getMathGoalScore() != null)
                examGoalDto.setMathGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getMathGoalScore()));
            if (examGoalMutateTotalDto.getEnglishGoalScore() != null)
                examGoalDto.setEnglishGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getEnglishGoalScore()));
            if (examGoalMutateTotalDto.getPhysicsGoalScore() != null)
                examGoalDto.setPhysicsGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getPhysicsGoalScore()));
            if (examGoalMutateTotalDto.getHistoryGoalScore() != null)
                examGoalDto.setHistoryGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getHistoryGoalScore()));
            if (examGoalMutateTotalDto.getChemistryGoalScore() != null)
                examGoalDto.setChemistryGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getChemistryGoalScore()));
            if (examGoalMutateTotalDto.getChemistryWeightedGoalScore() != null)
                examGoalDto.setChemistryWeightedGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getChemistryWeightedGoalScore()));
            if (examGoalMutateTotalDto.getBiologyGoalScore() != null)
                examGoalDto.setBiologyGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getBiologyGoalScore()));
            if (examGoalMutateTotalDto.getBiologyWeightedGoalScore() != null)
                examGoalDto.setBiologyWeightedGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getBiologyWeightedGoalScore()));
            if (examGoalMutateTotalDto.getGeographyGoalScore() != null)
                examGoalDto.setGeographyGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getGeographyGoalScore()));
            if (examGoalMutateTotalDto.getGeographyWeightedGoalScore() != null)
                examGoalDto.setGeographyWeightedGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getGeographyWeightedGoalScore()));
            if (examGoalMutateTotalDto.getPoliticsGoalScore() != null)
                examGoalDto.setPoliticsGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getPoliticsGoalScore()));
            if (examGoalMutateTotalDto.getPoliticsWeightedGoalScore() != null)
                examGoalDto.setPoliticsWeightedGoalScore(BigDecimal.valueOf(examGoalMutateTotalDto.getPoliticsWeightedGoalScore()));

            String updateStr = JSONObject.toJSONString(examGoalDto);
            this.settingsMapper.update(null, Wrappers.<Settings>lambdaUpdate()
                    .set(Settings::getParams, updateStr)
                    .eq(Settings::getId, settings.getId()));
        });
        return 1;
    }

    @Value("${web.upload-path}")
    private String uploadPath;

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(Long examId, String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");

        Exam exam = this.examMapper.selectById(examId);
        if (exam == null)
            throw new ApiCode.ApiException(-1, "考试不存在！");
        List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                .eq(Clazz::getAcademicYearSemesterId, exam.getAcademicYearSemesterId())
                .eq(Clazz::getGradeId, exam.getGradeId()));
        Map<String, Clazz> clazzMap = clazzList.stream().collect(Collectors.toMap(Clazz::getName, Function.identity()));

        XSSFWorkbook book = new XSSFWorkbook(file);
        XSSFSheet sheet = book.getSheetAt(0);
        file.delete();
        //获得总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        if (rowNum - 1 != clazzList.size())
            throw new ApiCode.ApiException(-1, "班级数量有误！");

        XSSFRow row = sheet.getRow(0);
        XSSFCell cell;
        int start = 1;
        List<String> goalNameList = new ArrayList<>();
        while ((cell = row.getCell(start++)) != null) {
            String goalName = CellUtils.getCellValue(cell);
            if (StringUtils.isNullOrEmpty(goalName)) break;
            if (goalNameList.contains(goalName))
                throw new ApiCode.ApiException(-1, "目标名称重复！");
            goalNameList.add(goalName);
        }

        ExamGoalClazzService examGoalClazzService = SpringUtils.getBean(ExamGoalClazzService.class);
        for (start = 0; start < goalNameList.size(); start++) {
            String goalName = goalNameList.get(start);
            ExamGoal examGoal = this.baseMapper.selectOne(Wrappers.<ExamGoal>lambdaQuery()
                    .eq(ExamGoal::getExamId, examId)
                    .eq(ExamGoal::getName, goalName));
            if (examGoal == null) {
                examGoal = new ExamGoal();
                examGoal.setExamId(examId);
                examGoal.setName(goalName);
                examGoal.setDefault().validate(true);
                this.baseMapper.insert(examGoal);
            }
            Long goalId = examGoal.getId();
            List<ExamGoalClazz> examGoalClazzList = new ArrayList<>();
            for (int rn = 1; rn < rowNum; rn++) {
                row = sheet.getRow(rn);
                String clazzName = CellUtils.getCellValue(row.getCell(0));
                if (!clazzMap.containsKey(clazzName))
                    throw new ApiCode.ApiException(-1, "第" + rn + 1 + "行班级名称有误！");
                ExamGoalClazz examGoalClazz = new ExamGoalClazz();
                examGoalClazz.setExamGoalId(goalId);
                examGoalClazz.setClazzId(clazzMap.get(clazzName).getId());
                String val = CellUtils.getCellValue(row.getCell(start + 1));
                if (StringUtils.isNullOrEmpty(val)) val = "0";
                examGoalClazz.setGoalValue(Integer.parseInt(val));
                examGoalClazzList.add(examGoalClazz);
            }
            examGoalClazzService.saveBatchExamGoalClazz(examGoalClazzList);
        }
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
    public boolean saveBatch(Collection<ExamGoal> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamGoalBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
