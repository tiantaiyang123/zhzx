package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.card.ConsumeRecordDto;
import com.zhzx.server.dto.exam.ExamClazzAnalyseStudentDto;
import com.zhzx.server.msdomain.AccountInfo;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.CardService;
import com.zhzx.server.service.ExamResultService;
import com.zhzx.server.util.JWTUtils;
import com.zhzx.server.vo.ConsumeRecordVo;
import com.zhzx.server.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "CardController", description = "一卡通")
@RequestMapping("/v1/card")
public class CardController {

    @Resource
    private CardService cardService;

    @Resource
    private ExamResultService examResultService;

    @GetMapping("/exam-record")
    @ApiOperation("查询学期考试")
    public ApiResponse<List<ExamClazzAnalyseStudentDto>> selectExamPage(
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "studentId", required = false) Long studentId) {
        return ApiResponse.ok(this.examResultService.getExamCard(studentId, examId, academicYearSemesterId));
    }

    @GetMapping("/exam-print")
    @ApiOperation("打印考试")
    public ApiResponse<Map<String, Object>> selectExamPrint(
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "studentId", required = false) Long studentId) {
        return ApiResponse.ok(this.examResultService.printExam(studentId, examId, academicYearSemesterId));
    }

    @GetMapping("/consume-record")
    @ApiOperation("分页查询消费记录")
    public ApiResponse<IPage<ConsumeRecordDto>> selectConsumePage(
            ConsumeRecordVo consumeRecordVo,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ApiResponse.ok(this.cardService.selectConsumePage(consumeRecordVo, pageNum, pageSize));
    }

    @GetMapping("/account-info")
    @ApiOperation("个人信息")
    public ApiResponse<AccountInfo> info(@RequestParam(value = "cPhysicalNo", required = false) Long cPhysicalNo,
                                         @RequestParam(value = "mobile", required = false) String mobile,
                                         @RequestParam(value = "idNumber", required = false) String idNumber) {
        return ApiResponse.ok(this.cardService.getInfo(cPhysicalNo, mobile, idNumber));
    }

    @ApiOperation("读卡器登录")
    @PostMapping("/read-login")
    public ApiResponse<Map<String, Object>> login(@RequestBody User user) throws UnsupportedEncodingException {
        Map<String, Object> result = this.cardService.cardLogin(user.getUsername(), user.getPassword());
        UserVo loginUser = (UserVo) result.get("userVo");
        result.put("token", JWTUtils.sign(loginUser.getUserInfo().getUsername(), loginUser.getUserInfo().getPassword()));
        return ApiResponse.ok(result);
    }

}
