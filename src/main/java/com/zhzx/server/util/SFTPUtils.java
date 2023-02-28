package com.zhzx.server.util;

import com.zhzx.server.domain.TeachingResultAttachment;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "upload")
public class SFTPUtils {

    public String SYSTEM_TYPE;

    // FTP服务器所在的地址
    public String BASE_URL;

    // 连接FTP服务器的端口号
    public String FTP_PORT;

    // 文件服务器域名
    public String DOMAIN_URL;

    // 文件服务器域名
    public String DOMAIN_URL_FTP;

    // 文件存放的基本路径
    public String COMMON_PATH;

    // 登录FTP服务器的用户名
    public String USER_NAME;

    // 登录FTP服务器的密码
    public String PASS_WORD;

    // 用户根目录路径
    public String HOME_DIR;

    // FTP用户根目录路径
    public String HOME_DIR_FTP;

    public List<TeachingResultAttachment> transferFtp(List<TeachingResultAttachment> list) throws Exception {
        // 前缀全部替换为本机twx文件存储位置
        Map<String, TeachingResultAttachment> map = new HashMap<>();
        List<TeachingResultAttachment> res = new LinkedList<>();
        List<String> tmp = new LinkedList<>();
        int length = (TwxUtils.upload_path_common_prefix + TwxUtils.upload_path_common_prefix).length();
        if (list != null && list.size() > 0) {
            list.forEach(item -> {
                String suffix = item.getUrl().substring(length + 1);
                map.put(suffix, item);
                tmp.add(suffix);
            });
            List<String> successList = this.uploadFtp1(tmp.toArray(new String[0]));
            if (successList != null && successList.size() > 0) {
                successList.stream().filter(map::containsKey).forEach(i -> res.add(map.get(i)));
            }
        }
        return res;
    }

    public List<String> uploadFtp1(String... strings) throws Exception {
        List<String> successPath = new LinkedList<>();
        String tempPath = HOME_DIR_FTP;
        String separatorStr = "LINUX".equals(SYSTEM_TYPE) ? "/" : "\\\\";
        if (strings != null && strings.length > 0) {
            // ftp服务器外网地址
            String netPath = DOMAIN_URL_FTP + "/" + COMMON_PATH + "/";
            FTPClient ftp = new FTPClient();
            ftp.setControlEncoding("utf-8");
            ftp.connect(netPath, Integer.parseInt(FTP_PORT));// 连接FTP服务器
            ftp.login(USER_NAME, PASS_WORD);// 登录
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return successPath;
            }
            InputStream is;
            Path path;
            for (String string : strings) {
                path = Paths.get(string);
                if (Files.isDirectory(path)) continue;
                try {
                    String filePath = path.getParent().toString() + separatorStr + path.getParent() + separatorStr + path.getFileName();
                    String localPath = HOME_DIR_FTP + separatorStr + filePath;
                    // 切换到上传目录
                    if (!ftp.changeWorkingDirectory(localPath)) {
                        // 如果目录不存在创建目录
                        String[] dirs = filePath.split(separatorStr);
                        if (!ftp.changeWorkingDirectory(HOME_DIR_FTP)) {
                            if (!ftp.makeDirectory(HOME_DIR_FTP)) {
                                return null;
                            } else {
                                ftp.changeWorkingDirectory(HOME_DIR_FTP);
                            }
                        }
                        for (String dir : dirs) {
                            if (null == dir || "".equals(dir))
                                continue;
                            tempPath += separatorStr + dir;
                            if (!ftp.changeWorkingDirectory(tempPath)) {
                                if (!ftp.makeDirectory(tempPath)) {
                                    return null;
                                } else {
                                    ftp.changeWorkingDirectory(tempPath);
                                }
                            }
                        }
                    }
                    // 准备传输数据，做出如下设置
                    // 设置上传文件的类型为二进制类型
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    // 设置被动模式
                    ftp.enterLocalPassiveMode();
                    is = Files.newInputStream(path, StandardOpenOption.READ);
                    // 上传文件
                    if (!ftp.storeFile(localPath, is)) {
                        return successPath;
                    }
                    successPath.add(filePath);
                    is.close();
                } catch (Exception e) {
                    log.error("上传Ftp失败！" + e);
                    throw e;
                }
            }
            ftp.logout();
            ftp.disconnect();
        }
        return successPath;
    }

    public String uploadFtp(MultipartFile file) throws Exception {
        FTPClient ftp = null;
        String netPath;
        InputStream is;
        try {
            String newFileName = file.getOriginalFilename();
            Date currDate = new Date();
            netPath = DOMAIN_URL_FTP + "/" + COMMON_PATH + "/" + new SimpleDateFormat("yyyyMMdd").format(currDate) + "/" + newFileName;

            // 这里判断FTP服务器所在系统类型进行区分
            String separatorStr;
            if (SYSTEM_TYPE.equals("LINUX")) {
                separatorStr = "/";
            } else {
                separatorStr = "\\\\";
            }
            String filePath = new SimpleDateFormat("yyyy" + separatorStr + "MM" + separatorStr + "dd").format(currDate);
            ftp = new FTPClient();
            ftp.setControlEncoding("utf-8");

            int reply;
            ftp.connect(BASE_URL, Integer.parseInt(FTP_PORT));// 连接FTP服务器
            ftp.login(USER_NAME, PASS_WORD);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            String basePath = HOME_DIR_FTP + separatorStr + COMMON_PATH;
            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(basePath + separatorStr + filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split(separatorStr);
                String tempPath = basePath;
                if (!ftp.changeWorkingDirectory(tempPath)) {
                    if (!ftp.makeDirectory(tempPath)) {
                        return null;
                    } else {
                        ftp.changeWorkingDirectory(tempPath);
                    }
                }
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir))
                        continue;
                    tempPath += separatorStr + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return null;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 准备传输数据，做出如下设置
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置被动模式
            ftp.enterLocalPassiveMode();
            is = file.getInputStream();
            // 上传文件
            if (!ftp.storeFile(newFileName, is)) {
                return null;
            }
            is.close();
            ftp.logout();
        } catch (Exception e) {
            log.error("上传Ftp失败！" + e);
            throw e;
        } finally {
            // 如果FTP连接已经建立并且没有断开，则断开连接
            if (ftp != null && ftp.isConnected()) {
                ftp.disconnect();
            }
        }
        return netPath;
    }

    public String uploadCloud(MultipartFile file) throws Exception {
        InputStream is = null;
        try {
            String path = HOME_DIR + "/" + COMMON_PATH;
            String fileName = String.format("/%s/%s/%s",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    UUID.randomUUID(),
                    file.getOriginalFilename());
            Path p = Paths.get(path + fileName);
            Files.createDirectories(p.getParent());
            is = file.getInputStream();
            Files.copy(is, p);
            return DOMAIN_URL + "/" + COMMON_PATH + fileName;
        } catch (Exception e){
            log.error("上传失败！" + e);
            throw e;
        } finally {
            if (is != null) is.close();
        }
    }

}
