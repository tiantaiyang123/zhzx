/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ServerApplicationTests {

    @Test
    void contextLoads() {
        /**
        QueryWrapper<BarCode> queryWrapper = Wrappers.<BarCode>query().select("id", "code_type").eq("id", 1);
        System.out.println("queryWrapper.sqlSegment=" + queryWrapper.getCustomSqlSegment());

        IPage<BarCode> page = new Page(0, 10);
        barCodeService.page(page);
        System.out.println("page.total=" + page.getTotal() + " page.pages=" + page.getPages());
        page.getRecords().forEach(i -> {
            BarCode barCode = barCodeService.getById(i.getId());
            System.out.println("barCode.planOrder=" + barCode.getPlanOrder());
        });
         */
    }

}
