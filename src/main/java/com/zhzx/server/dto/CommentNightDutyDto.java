package com.zhzx.server.dto;

import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.Staff;
import lombok.Data;

import java.util.List;

@Data
public class CommentNightDutyDto extends BaseDto {
    private Long clazzId;
    private Staff advisor;
    private List<Comment> commentList;
}
