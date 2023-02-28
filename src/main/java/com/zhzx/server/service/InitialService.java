package com.zhzx.server.service;


public interface InitialService {

    void importExcel(Long schoolyardId, String gradeName, Long academicYearSemesterId, String fileUrl);

    void importExcelScore(Long examId, String fileUrl);

    void importExcelStudent(Long academicYearSemesterId, String fileUrl);
}
