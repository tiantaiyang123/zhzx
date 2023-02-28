package com.zhzx.server.dto.exam;

import com.zhzx.server.domain.ExamResult;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.GenderEnum;
import com.zhzx.server.enums.StudentTypeEnum;
import lombok.Data;

@Data
public class ExamPagerDto extends ExamResult {

    private String studentName;

    private String studentNumber;

    private String className;

    private ClazzNatureEnum nature;

    private StudentTypeEnum studentType;

    private GenderEnum gender;

    private int totalRank;

    private int totalWeightedRank;

    private int totalRankNj;

    private int totalWeightedRankNj;

    private int chineseRank;

    private int mathRank;

    private int englishRank;

    private int physicsRank;

    private int chemistryRank;

    private int chemistryWeightedRank;

    private int biologyRank;

    private int biologyWeightedRank;

    private int historyRank;

    private int politicsRank;

    private int politicsWeightedRank;

    private int geographyRank;

    private int geographyWeightedRank;

    private int totalRankPre;

    private int totalWeightRankPre;

}
