package com.zhzx.server.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.TeachingResultAttachment;
import com.zhzx.server.service.TeachingResultAttachmentService;
import com.zhzx.server.util.SFTPUtils;
import com.zhzx.server.util.TwxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

//@Component
@Slf4j
public class FileTransferSchedule {

    @Resource
    private SFTPUtils sftpUtils;

    @Resource
    private TeachingResultAttachmentService teachingResultAttachmentService;

    @Scheduled(cron = "0 0 23 * * ? ")
    public void local2Ftp() {
        String folder = LocalDate.now(ZoneId.systemDefault()).toString().replaceAll("-", "");
        if (folder.length() < 10000) {
            return;
        }
        String path = TwxUtils.upload_path_common_prefix + TwxUtils.upload_path_common_prefix;
        // 获取数据库所有文件
        LambdaQueryWrapper<TeachingResultAttachment> wrapper =  Wrappers.<TeachingResultAttachment>lambdaQuery().select(TeachingResultAttachment::getId, TeachingResultAttachment::getUrl).likeLeft(TeachingResultAttachment::getUrl, path);
        List<TeachingResultAttachment> teachingResultAttachmentList = this.teachingResultAttachmentService.list(wrapper);
        if (CollectionUtils.isNotEmpty(teachingResultAttachmentList)) {
            List<TeachingResultAttachment> res;
            try {
                res = sftpUtils.transferFtp(teachingResultAttachmentList);
            } catch (Exception tfExp) {
                log.error("执行文件转移任务 转移文件失败！", tfExp);
                return;
            }
            if (res != null && res.size() > 0) {
                // 先更新数据库
                try {
                    this.teachingResultAttachmentService.updateBatch(res);
                } catch (Exception dbExp) {
                    log.error("执行文件转移任务 更新DB失败！", dbExp);
                    return;
                }
                Set<String> filesSet = new HashSet<>();
                Map<String, List<String>> maps = new HashMap<>();
                res.forEach(item -> {
                    filesSet.add(item.getUrl().substring(item.getUrl().lastIndexOf("/") + 1));
                    String partUrl = item.getUrl().substring(path.length() + 1, item.getUrl().lastIndexOf("/"));
                    String[] arr = partUrl.split("/");
                    maps.computeIfAbsent(arr[0], o -> new LinkedList<>()).add(arr[1]);
                });
                // 删除twx的文件存储
                try {
                    Path localPath = Paths.get("D:\\thingworx\\ThingworxStorage\\repository\\TmgRepository");
                    Files.walkFileTree(localPath, new SimpleFileVisitor<Path>() {
                        int level = 0;

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (filesSet.contains(file.getFileName().toString())) {
                                Files.delete(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                            if (level == 0 || (level == 1 && maps.containsKey(dir.getFileName().toString()))
                                    || (level == 2 && maps.get(dir.getParent().getFileName().toString()).contains(dir.getFileName().toString()))) {
                                level++;
                                return FileVisitResult.CONTINUE;
                            }
                            return FileVisitResult.SKIP_SUBTREE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                            level--;
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (Exception fileExp) {
                    log.error("执行文件转移任务 删除本地文件失败！", fileExp);
                }
            }
        }
    }

}
