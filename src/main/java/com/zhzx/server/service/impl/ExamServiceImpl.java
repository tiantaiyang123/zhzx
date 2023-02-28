/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.exam.ExamGoalDto;
import com.zhzx.server.dto.exam.ExamResultOtherDto;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.SemesterEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.ExamBaseMapper;
import com.zhzx.server.rest.req.ExamParam;
import com.zhzx.server.service.ExamGoalService;
import com.zhzx.server.service.ExamService;
import com.zhzx.server.util.TwxUtils;
import com.zhzx.server.vo.ExamVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Resource
    private ExamResultMapper examResultMapper;

    @Resource
    private SettingsMapper settingsMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Resource
    private ExamGoalService examGoalService;

    @Override
    public int updateAllFieldsById(Exam entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public List<ExamVo> getExamBySemmsterAndYard(Map<String, Object> map) {
        return this.getBaseMapper().getExamBySemmsterAndYard(map);
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
    public boolean saveBatch(Collection<Exam> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public List<Exam> getList(Long schoolyardId, String academicYear, Long gradeId){
        return this.getBaseMapper().getList(schoolyardId, academicYear, gradeId);
    }

    @Override
    public IPage<Map<String, Object>> selectByPageDetail(IPage<Exam> page, String orderByClause, ExamParam param) {
        return this.getBaseMapper().selectByPageDetail(page, orderByClause, param);
    }

    @Override
    public void publish(Long examId) {
        if (examId != null) {
            List<ExamResult> allList = this.examResultMapper.selectList(Wrappers.<ExamResult>lambdaQuery()
                    .eq(ExamResult::getExamId, examId));
            if (CollectionUtils.isNotEmpty(allList)) {
                Map<Long, List<ExamResult>> bjMap = allList.stream().collect(Collectors.groupingBy(ExamResult::getClazzId));
                Map<ClazzNatureEnum, List<ExamResult>> njMap = allList.stream().collect(Collectors.groupingBy(o -> o.getClazz().getClazzNature()));
                Map<Long, Map<Integer, List<BigDecimal>>> scoreBjMap = new HashMap<>();
                Map<ClazzNatureEnum, Map<Integer, List<BigDecimal>>> scoreNjMap = new HashMap<>();
                bjMap.forEach((k, v) -> {
                    Map<Integer, List<BigDecimal>> map = scoreBjMap.computeIfAbsent(k, o -> new HashMap<>(32, 0.75f));
                    v.forEach(item -> {
                        map.computeIfAbsent(1, o -> new ArrayList<>()).add(item.getChineseScore());
                        map.computeIfAbsent(2, o -> new ArrayList<>()).add(item.getMathScore());
                        map.computeIfAbsent(3, o -> new ArrayList<>()).add(item.getEnglishScore());
                        map.computeIfAbsent(4, o -> new ArrayList<>()).add(item.getPhysicsScore());
                        map.computeIfAbsent(5, o -> new ArrayList<>()).add(item.getHistoryScore());
                        map.computeIfAbsent(6, o -> new ArrayList<>()).add(item.getChemistryScore());
                        map.computeIfAbsent(7, o -> new ArrayList<>()).add(item.getChemistryWeightedScore());
                        map.computeIfAbsent(8, o -> new ArrayList<>()).add(item.getBiologyScore());
                        map.computeIfAbsent(9, o -> new ArrayList<>()).add(item.getBiologyWeightedScore());
                        map.computeIfAbsent(10, o -> new ArrayList<>()).add(item.getPoliticsScore());
                        map.computeIfAbsent(11, o -> new ArrayList<>()).add(item.getPoliticsWeightedScore());
                        map.computeIfAbsent(12, o -> new ArrayList<>()).add(item.getGeographyScore());
                        map.computeIfAbsent(13, o -> new ArrayList<>()).add(item.getGeographyWeightedScore());
                        map.computeIfAbsent(14, o -> new ArrayList<>()).add(item.getTotalScore());
                        map.computeIfAbsent(15, o -> new ArrayList<>()).add(item.getTotalWeightedScore());
                        BigDecimal three = item.getChineseScore().add(item.getMathScore()).add(item.getEnglishScore());
                        map.computeIfAbsent(16, o -> new ArrayList<>()).add(three);
                        if (item.getClazz().getClazzNature().equals(ClazzNatureEnum.OTHER)) {
                            map.computeIfAbsent(17, o -> new ArrayList<>());
                        } else if (item.getClazz().getClazzNature().equals(ClazzNatureEnum.LIBERAL)){
                            map.computeIfAbsent(17, o -> new ArrayList<>()).add(item.getHistoryScore().add(three));
                        } else {
                            map.computeIfAbsent(17, o -> new ArrayList<>()).add(item.getPhysicsScore().add(three));
                        }
                    });
                });
                Map<Long, List<Integer>> bjLastPmMap = new HashMap<>();
                scoreBjMap.forEach((k, v) -> {
                    List<Integer> list = bjLastPmMap.computeIfAbsent(k, o -> new ArrayList<>());
                    v.forEach((k1, v1) -> {
                        v1.sort(Comparator.reverseOrder());
                        int size = v1.size();
                        if (size > 0) {
                            BigDecimal last = v1.get(v1.size() - 1);
                            list.add(k1 - 1, size - (int) v1.stream().filter(o -> o.compareTo(last) == 0).count());
                        } else {
                            list.add(k1 - 1, 0);
                        }
                    });
                });

                njMap.forEach((k, v) -> {
                    Map<Integer, List<BigDecimal>> map = scoreNjMap.computeIfAbsent(k, o -> new HashMap<>(8, 0.75f));
                    v.forEach(item -> {
                        map.computeIfAbsent(1, o -> new ArrayList<>()).add(item.getTotalScore());
                        map.computeIfAbsent(2, o -> new ArrayList<>()).add(item.getTotalWeightedScore());
                        BigDecimal three = item.getChineseScore().add(item.getMathScore()).add(item.getEnglishScore());
                        map.computeIfAbsent(3, o -> new ArrayList<>()).add(three);
                        if (k.equals(ClazzNatureEnum.OTHER)) {
                            map.computeIfAbsent(4, o -> new ArrayList<>());
                        } else if (k.equals(ClazzNatureEnum.LIBERAL)){
                            map.computeIfAbsent(4, o -> new ArrayList<>()).add(item.getHistoryScore().add(three));
                        } else {
                            map.computeIfAbsent(4, o -> new ArrayList<>()).add(item.getPhysicsScore().add(three));
                        }
                        map.computeIfAbsent(5, o -> new ArrayList<>()).add(item.getChineseScore());
                        map.computeIfAbsent(6, o -> new ArrayList<>()).add(item.getMathScore());
                        map.computeIfAbsent(7, o -> new ArrayList<>()).add(item.getEnglishScore());
                        map.computeIfAbsent(8, o -> new ArrayList<>()).add(item.getPhysicsScore());
                        map.computeIfAbsent(9, o -> new ArrayList<>()).add(item.getHistoryScore());
                        map.computeIfAbsent(10, o -> new ArrayList<>()).add(item.getChemistryScore());
                        map.computeIfAbsent(11, o -> new ArrayList<>()).add(item.getChemistryWeightedScore());
                        map.computeIfAbsent(12, o -> new ArrayList<>()).add(item.getBiologyScore());
                        map.computeIfAbsent(13, o -> new ArrayList<>()).add(item.getBiologyWeightedScore());
                        map.computeIfAbsent(14, o -> new ArrayList<>()).add(item.getPoliticsScore());
                        map.computeIfAbsent(15, o -> new ArrayList<>()).add(item.getPoliticsWeightedScore());
                        map.computeIfAbsent(16, o -> new ArrayList<>()).add(item.getGeographyScore());
                        map.computeIfAbsent(17, o -> new ArrayList<>()).add(item.getGeographyWeightedScore());
                    });
                });
                Map<ClazzNatureEnum, List<Integer>> njLastPmMap = new HashMap<>();
                scoreNjMap.forEach((k, v) -> {
                    List<Integer> list = njLastPmMap.computeIfAbsent(k, o -> new ArrayList<>());
                    v.forEach((k1, v1) -> {
                        v1.sort(Comparator.reverseOrder());
                        if (k1 < 5) {
                            int size = v1.size();
                            if (size > 0) {
                                BigDecimal last = v1.get(v1.size() - 1);
                                list.add(k1 - 1, 1 + size - (int) v1.stream().filter(o -> o.compareTo(last) == 0).count());
                            } else {
                                list.add(k1 - 1, 0);
                            }
                        }
                    });
                });

                ExamResultOtherDto examResultOtherDto = new ExamResultOtherDto();
                allList.forEach(item -> {
                    examResultOtherDto.setChineseRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(1), item.getChineseScore()));
                    examResultOtherDto.setYwNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(5), item.getChineseScore()));
                    examResultOtherDto.setYwAll(bjLastPmMap.get(item.getClazzId()).get(0));
                    examResultOtherDto.setMathRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(2), item.getMathScore()));
                    examResultOtherDto.setSxNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(6), item.getMathScore()));
                    examResultOtherDto.setSxAll(bjLastPmMap.get(item.getClazzId()).get(1));
                    examResultOtherDto.setEnglishRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(3), item.getEnglishScore()));
                    examResultOtherDto.setYyNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(7), item.getEnglishScore()));
                    examResultOtherDto.setYyAll(bjLastPmMap.get(item.getClazzId()).get(2));
                    examResultOtherDto.setPhysicsRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(4), item.getPhysicsScore()));
                    examResultOtherDto.setWlNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(8), item.getPhysicsScore()));
                    examResultOtherDto.setWlAll(bjLastPmMap.get(item.getClazzId()).get(3));
                    examResultOtherDto.setHistoryRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(5), item.getHistoryScore()));
                    examResultOtherDto.setLsNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(9), item.getHistoryScore()));
                    examResultOtherDto.setLsAll(bjLastPmMap.get(item.getClazzId()).get(4));
                    examResultOtherDto.setChemistryRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(6), item.getChemistryScore()));
                    examResultOtherDto.setHxNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(10), item.getChemistryScore()));
                    examResultOtherDto.setHxAll(bjLastPmMap.get(item.getClazzId()).get(5));
                    examResultOtherDto.setChemistryWeightedRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(7), item.getChemistryWeightedScore()));
                    examResultOtherDto.setHxfNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(11), item.getChemistryWeightedScore()));
                    examResultOtherDto.setHxfAll(bjLastPmMap.get(item.getClazzId()).get(6));
                    examResultOtherDto.setPoliticsRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(10), item.getPoliticsScore()));
                    examResultOtherDto.setZzNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(14), item.getPoliticsScore()));
                    examResultOtherDto.setZzAll(bjLastPmMap.get(item.getClazzId()).get(9));
                    examResultOtherDto.setPoliticsWeightedRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(11), item.getPoliticsWeightedScore()));
                    examResultOtherDto.setZzfNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(15), item.getPoliticsWeightedScore()));
                    examResultOtherDto.setZzfAll(bjLastPmMap.get(item.getClazzId()).get(10));
                    examResultOtherDto.setBiologyRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(8), item.getBiologyScore()));
                    examResultOtherDto.setSwNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(12), item.getBiologyScore()));
                    examResultOtherDto.setSwAll(bjLastPmMap.get(item.getClazzId()).get(7));
                    examResultOtherDto.setBiologyWeightedRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(9), item.getBiologyWeightedScore()));
                    examResultOtherDto.setSwfNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(13), item.getBiologyWeightedScore()));
                    examResultOtherDto.setSwfAll(bjLastPmMap.get(item.getClazzId()).get(8));
                    examResultOtherDto.setGeographyRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(12), item.getGeographyScore()));
                    examResultOtherDto.setDlNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(16), item.getGeographyScore()));
                    examResultOtherDto.setDlAll(bjLastPmMap.get(item.getClazzId()).get(11));
                    examResultOtherDto.setGeographyWeightedRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(13), item.getGeographyWeightedScore()));
                    examResultOtherDto.setDlfNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(17), item.getGeographyWeightedScore()));
                    examResultOtherDto.setDlfAll(bjLastPmMap.get(item.getClazzId()).get(12));
                    examResultOtherDto.setTotalRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(14), item.getTotalScore()));
                    examResultOtherDto.setZfAll(bjLastPmMap.get(item.getClazzId()).get(13));
                    examResultOtherDto.setTotalWeightedRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(15), item.getTotalWeightedScore()));
                    examResultOtherDto.setZffAll(bjLastPmMap.get(item.getClazzId()).get(14));
                    examResultOtherDto.setTotalRankNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(1), item.getTotalScore()));
                    examResultOtherDto.setZfAllNj(njLastPmMap.get(item.getClazz().getClazzNature()).get(0));
                    examResultOtherDto.setTotalWeightedRankNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(2), item.getTotalWeightedScore()));
                    examResultOtherDto.setZffAllNj(njLastPmMap.get(item.getClazz().getClazzNature()).get(1));
                    examResultOtherDto.setThreeTotal(item.getChineseScore().add(item.getMathScore()).add(item.getEnglishScore()));
                    examResultOtherDto.setThreeTotalRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(16), examResultOtherDto.getThreeTotal()));
                    examResultOtherDto.setSzAll(bjLastPmMap.get(item.getClazzId()).get(15));
                    examResultOtherDto.setThreeTotalRankNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(3), examResultOtherDto.getThreeTotal()));
                    examResultOtherDto.setSzAllNj(njLastPmMap.get(item.getClazz().getClazzNature()).get(2));
                    if (item.getClazz().getClazzNature().equals(ClazzNatureEnum.OTHER)) {
                        examResultOtherDto.setFourTotal(BigDecimal.ZERO);
                        examResultOtherDto.setSizAll(0);
                        examResultOtherDto.setSizAllNj(0);
                    } else {
                        if (item.getClazz().getClazzNature().equals(ClazzNatureEnum.LIBERAL)){
                            examResultOtherDto.setFourTotal(examResultOtherDto.getThreeTotal().add(item.getHistoryScore()));
                        } else {
                            examResultOtherDto.setFourTotal(examResultOtherDto.getThreeTotal().add(item.getPhysicsScore()));
                        }
                        examResultOtherDto.setFourTotalRankBj(TwxUtils.binarySearchEnhance1(scoreBjMap.get(item.getClazzId()).get(17), examResultOtherDto.getFourTotal()));
                        examResultOtherDto.setSizAll(bjLastPmMap.get(item.getClazzId()).get(16));
                        examResultOtherDto.setFourTotalRankNj(TwxUtils.binarySearchEnhance1(scoreNjMap.get(item.getClazz().getClazzNature()).get(4), examResultOtherDto.getFourTotal()));
                        examResultOtherDto.setSizAllNj(njLastPmMap.get(item.getClazz().getClazzNature()).get(3));
                    }
                    item.setClazzRank(examResultOtherDto.getTotalRankBj());
                    item.setGradeRank(examResultOtherDto.getTotalRankNj());
                    item.setOther(JSONObject.toJSONString(examResultOtherDto));
                    examResultOtherDto.setFourTotal(BigDecimal.ZERO);
                    examResultOtherDto.setFourTotalRankBj(0);
                    examResultOtherDto.setFourTotalRankNj(0);
                    examResultOtherDto.setSizAll(0);
                    examResultOtherDto.setSizAllNj(0);
                });

                int size = allList.size();
                for (int i = 0, j = Math.min(100, size); i <= size;) {
                    this.examResultMapper.updateRank(allList.subList(i, j));
                    i = (j == size ? j + 1 : j);
                    j = Math.min(j + 100, size);
                }
            }
            this.getBaseMapper().update(null, Wrappers.<Exam>lambdaUpdate().set(Exam::getIsPublish, YesNoEnum.YES).set(Exam::getUpdateTime, new Date()).eq(Exam::getId, examId));

            // todo 这里按照模板算目标
            List<ExamGoalDto> examGoalDtoList = this.examGoalService.getAllGoal(examId, null);
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                List<Settings> settingsList = examGoalDtoList.stream().map(item -> {
                    Settings settings = new Settings();
                    settings.setCode("GOAL_" + examId + "_" + item.getId() + "_" + item.getSubjectType());
                    settings.setRemark(settings.getCode());
                    settings.setParams(JSONObject.toJSONString(item));
                    return settings;
                }).collect(Collectors.toList());
                this.settingsMapper.delete(Wrappers.<Settings>lambdaQuery().likeRight(Settings::getCode, "GOAL\\_" + examId + "\\_"));
                this.settingsMapper.batchInsert(settingsList);
            }
        }
    }

    @Override
    public List<Exam> getListByGrade(Long schoolyardId, Long gradeId) {
        Grade grade = this.gradeMapper.selectById(gradeId);
        if (grade != null) {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            List<AcademicYearSemester> academicYearSemesters = this.academicYearSemesterMapper.selectList(Wrappers.
                    <AcademicYearSemester>lambdaQuery().le(AcademicYearSemester::getEndTime, user.getAcademicYearSemester().getEndTime()).orderByDesc(AcademicYearSemester::getEndTime));
            Map<String, Long> grades = this.gradeMapper.selectList(Wrappers.<Grade>lambdaQuery().eq(Grade::getSchoolyardId, schoolyardId)).stream().collect(Collectors.toMap(Grade::getName, Grade::getId));
            List<String> list = new ArrayList<>();
            String name = grade.getName();
            int idx = 0;
            if ("高三年级".equals(name)) {
                list.add(grade.getId().toString().concat("_").concat(academicYearSemesters.get(idx++).getId().toString()));
                if (academicYearSemesters.get(0).getSemester().equals(SemesterEnum.Q2) && academicYearSemesters.size() > 1) {
                    list.add(grade.getId().toString().concat("_").concat(academicYearSemesters.get(idx++).getId().toString()));
                }
                name = "高二年级";
            }
            if ("高二年级".equals(name)) {
                Long gId = grades.get(name);
                if (academicYearSemesters.size() > idx) {
                    list.add(gId.toString().concat("_").concat(academicYearSemesters.get(idx++).getId().toString()));
                    if (academicYearSemesters.get(idx - 1).getSemester().equals(SemesterEnum.Q2) && academicYearSemesters.size() > idx) {
                        list.add(gId.toString().concat("_").concat(academicYearSemesters.get(idx++).getId().toString()));
                    }
                    name = "高一年级";
                }
            }
            if ("高一年级".equals(name)) {
                Long gId = grades.get(name);
                if (academicYearSemesters.size() > idx) {
                    list.add(gId.toString().concat("_").concat(academicYearSemesters.get(idx++).getId().toString()));
                    if (academicYearSemesters.get(idx - 1).getSemester().equals(SemesterEnum.Q2) && academicYearSemesters.size() > idx) {
                        list.add(gId.toString().concat("_").concat(academicYearSemesters.get(idx).getId().toString()));
                    }
                }
            }
            List<Exam> exams = this.baseMapper.selectList(Wrappers.<Exam>lambdaQuery().eq(Exam::getSchoolyardId, schoolyardId));
            if (CollectionUtils.isNotEmpty(exams)) {
                exams.removeIf(next -> !list.contains(next.getGradeId().toString().concat("_").concat(next.getAcademicYearSemesterId().toString())));
            }
            return exams;
        }
        return new ArrayList<>();
    }
}
