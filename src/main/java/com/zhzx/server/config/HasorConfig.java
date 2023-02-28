/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.config;

import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;
import org.springframework.context.annotation.Configuration;

/**
 * Project: port-takeout-server <br>
 * Description:
 *
 * @author xiongwei
 * 2021/7/26 上午11:48
 */
@Configuration
@EnableHasor
@EnableHasorWeb(path = "/hasor/*")
public class HasorConfig {
}
