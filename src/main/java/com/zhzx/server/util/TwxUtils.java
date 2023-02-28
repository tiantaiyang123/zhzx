package com.zhzx.server.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

public class TwxUtils {

    private static Map<String, String> columnMap = null;

    public static final String upload_path_common_prefix = "http://222.190.143.210:18080/Thingworx/FileRepositories/";

    public static final String upload_path_tmg_prefix = "TmgRepository";

    public static final String appKey = "03b5c004-5085-426d-9410-e37762287037";

    public static final String[] arr = {"语文", "数学", "英语", "物理", "化学", "政治", "历史", "地理", "生物"};

    public static final String[] arrPrefix = {"chinese", "math", "english",
            "physics", "chemistry", "politics", "history", "geography", "biology"};

    public synchronized static Map<String, String> getColumnMap() {
        if (columnMap == null) {
            columnMap = new LinkedHashMap<String, String>(1 << 6, 0.65f) {
                {
                    put("studentName", "姓名");
                    put("studentNumber", "学号");
                    put("className", "班级");
                    put("chineseScore", "语文");
                    put("chineseRank", "排名");
                    put("mathScore", "数学");
                    put("mathRank", "排名");
                    put("englishScore", "英语");
                    put("englishRank", "排名");
                    put("physicsScore", "物理");
                    put("physicsRank", "排名");
                    put("historyScore", "历史");
                    put("historyRank", "排名");
                    put("chemistryScore", "化学");
                    put("chemistryRank", "排名");
                    put("chemistryWeightedScore", "化赋");
                    put("chemistryWeightedRank", "排名");
                    put("biologyScore", "生物");
                    put("biologyRank", "排名");
                    put("biologyWeightedScore", "生赋");
                    put("biologyWeightedRank", "排名");
                    put("politicsScore", "政治");
                    put("politicsRank", "排名");
                    put("politicsWeightedScore", "政赋");
                    put("politicsWeightedRank", "排名");
                    put("geographyScore", "地理");
                    put("geographyRank", "排名");
                    put("geographyWeightedScore", "地赋");
                    put("geographyWeightedRank", "排名");
                    put("totalScore", "总分");
                    put("totalRank", "班名");
                    put("totalRankNj", "科名");
                    put("totalRankChange", "科名升降");
                    put("totalWeightedScore", "总赋分");
                    put("totalWeightedRank", "班名");
                    put("totalWeightedRankNj", "科名");
                    put("totalWeightRankChange", "科名升降");
                }
            };
        }
        return columnMap;
    }

    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static int getAgeByIDNumber(String idNumber) {
        String dateStr;
        if (idNumber.length() == 15) {
            dateStr = "19" + idNumber.substring(6, 12);
        } else if (idNumber.length() == 18) {
            dateStr = idNumber.substring(6, 14);
        } else {
            return 100;
        }
        LocalDate now = LocalDate.now();
        LocalDate birth = LocalDate.parse(dateStr, pattern);
        Period period = Period.between(birth, now);
        return period.getYears();
    }

    public static <T> void fill(T[] arr, Supplier<T> s) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = s.get();
        }
    }

    public static <T> void arrSort(List<T>[] arr, Comparator<T> c) {
        for (List<T> list : arr) {
            list.sort(c);
        }
    }

    public static List<String> regionDiffer(int highest, int lowest, int tolerance) {
        List<String> res = new LinkedList<>();
        res.add("<=" + lowest);
        res.add(">=" + highest);
        for (; (highest -= tolerance) > lowest; res.add(">=" + highest));
        res.add("<>" + lowest + "-" + (highest + tolerance));
        return res;
    }

    /**
     * 朴素二分查找
     */
    public static int binarySearch(List<BigDecimal> list, BigDecimal curr) {
        int start = 0, end = list.size() - 1;
        while (start <= end) {
            int mid = start + ((end - start) >> 1);
            BigDecimal b = list.get(mid);
            if (curr.compareTo(b) > 0) {
                end = mid - 1;
            } else if (curr.compareTo(b) < 0) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return ++end;
    }

    /**
     * 二分查找 获取curr在list(已降序)中最先出现的位置
     */
    public static int binarySearchEnhance1(List<BigDecimal> list, BigDecimal curr) {
        int start = 0, end = list.size() - 1;
        while (start < end) {
            int mid = start + ((end - start) >> 1);
            BigDecimal b = list.get(mid);
            if (curr.compareTo(b) > 0) {
                end = mid - 1;
            } else if (curr.compareTo(b) < 0) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        return ++end;
    }

    /**
     * 获取curr在left到right范围内的左右端点(分数模式计算赋分用)
     */
    public static BigDecimal[] binarySearchEnhance2(List<BigDecimal> list, BigDecimal left, BigDecimal right) {
        int size = list.size();
        int leftIdx = binarySearch(list, left);
        int rightIdx = binarySearch(list, right);
        if (leftIdx >= size)
            return new BigDecimal[]{left, right};
        if (rightIdx >= size)
            return new BigDecimal[]{list.get(leftIdx), list.get(size - 1)};
        return new BigDecimal[]{list.get(leftIdx), list.get(rightIdx)};
    }
}
