package com.zhzx.server.dto;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.ClassifyEnum;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class DayRoutineDto extends LeaderDuty{
    private Date time;

    //年级总值班及总值班
    private String gradeOneTeacher;
    private String gradeTwoTeacher;
    private String gradeThreeTeacher;
    private String totalDutyTeacher;

    //南大门准备情况
    private SouthGate southGate;

    //早餐就餐情况
    private Breakfast breakfast;

    //早读情况
    private MorningReading morningReading;

    //大课间活动
    private BreakActivity breakActivity;

    //午餐情况
    private Lunch lunch;

    //教学区秩序
    private TeachingArea teachingArea;

    //运动区秩序
    private NoonSportArea noonSportArea;

    //走读生出门情况
    private GoOut goOut;

    //晚餐就餐情况
    private Dinner dinner;

    //运动区秩序
    private NightSportArea nightSportArea;

    //偶发时间情况
    private List<Incident> incidentList;

    //意见与建议表
    private List<CommentDto> comment;

    private Map<String,List<FunctionDepartment>> departmentMap;

    public static Map<String,Object> parse(DayRoutineDto dayRoutineDto){
        List<Incident> incidentList = new ArrayList<>();
        List<CommentDto> commentDtoList = new ArrayList<>();

        if(dayRoutineDto.getSouthGate() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getSouthGate().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getSouthGate().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getSouthGate().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getSouthGate().getCommentList());
            }
        }
        if(dayRoutineDto.getBreakfast() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getBreakfast().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getBreakfast().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getBreakfast().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getBreakfast().getCommentList());
            }
        }
        if(dayRoutineDto.getMorningReading() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getMorningReading().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getMorningReading().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getMorningReading().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getMorningReading().getCommentList());
            }
        }
        if(dayRoutineDto.getBreakActivity() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getBreakActivity().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getBreakActivity().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getBreakActivity().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getBreakActivity().getCommentList());
            }
        }
        if(dayRoutineDto.getLunch() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getLunch().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getLunch().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getLunch().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getLunch().getCommentList());
            }
        }
        if(dayRoutineDto.getTeachingArea() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getTeachingArea().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getTeachingArea().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getTeachingArea().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getTeachingArea().getCommentList());
            }
        }
        if(dayRoutineDto.getNoonSportArea() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getNoonSportArea().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getNoonSportArea().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getNoonSportArea().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getNoonSportArea().getCommentList());
            }
        }
        if(dayRoutineDto.getGoOut() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getGoOut().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getGoOut().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getGoOut().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getGoOut().getCommentList());
            }
        }
        if(dayRoutineDto.getDinner() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getDinner().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getDinner().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getDinner().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getDinner().getCommentList());
            }
        }
        if(dayRoutineDto.getNightSportArea() != null){
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getNightSportArea().getIncidentList())){
                incidentList.addAll(dayRoutineDto.getNightSportArea().getIncidentList());
            }
            if(CollectionUtils.isNotEmpty(dayRoutineDto.getNightSportArea().getCommentList())){
                commentDtoList.addAll(dayRoutineDto.getNightSportArea().getCommentList());
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("incident",incidentList);
        map.put("comment",commentDtoList);
        return map;
    }
}
