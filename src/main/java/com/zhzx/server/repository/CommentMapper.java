/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.dto.CommentDto;
import com.zhzx.server.dto.CommentNightDutyDto;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.Comment;
import com.zhzx.server.repository.base.CommentBaseMapper;

@Repository
public interface CommentMapper extends CommentBaseMapper {

    List<CommentNightDutyDto> getNightDutyComments();

    List<CommentDto> deanPageDetail(@Param("page") IPage page,@Param(Constants.WRAPPER) QueryWrapper<Comment> wrapper);

    List<CommentDto> selectListDto(@Param(Constants.WRAPPER) LambdaQueryWrapper<Comment> wrapper);

    Integer removeById(Long id);

    Integer removeProcessByCommentId(Long id);

    Integer removeByIds(@Param("ids") List<Long> ids);
}
