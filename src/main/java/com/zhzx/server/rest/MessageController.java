/**
 * 项目：中华中学管理平台
 *
 * @Author: xiongwei
 * @Date: 2020-07-23 17:08:00
 */

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Message;
import com.zhzx.server.domain.StaffMessageRefuse;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.MessageDto;
import com.zhzx.server.enums.MessageSystemEnum;
import com.zhzx.server.enums.MessageTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.MessageParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.MessageService;
import com.zhzx.server.service.StaffMessageRefuseService;
import com.zhzx.server.util.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "MessageController", description = "消息表管理")
@RequestMapping("/v1/message/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private StaffMessageRefuseService staffMessageRefuseService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Message> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.messageService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Message> add(@RequestBody Message entity) {
        entity.setDefault().validate(true);
        this.messageService.save(entity);
        return ApiResponse.ok(this.messageService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Message> update(@RequestBody Message entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.messageService.updateAllFieldsById(entity);
        } else {
            this.messageService.updateById(entity);
        }
        return ApiResponse.ok(this.messageService.getById(entity.getId()));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.messageService.removeById(id));
    }


    /**
     * 查询当前用户所能接收消息的任务id
     */
    @GetMapping("/search/messageTaskIds")
    @ApiOperation("寻找当前用户能接受的消息任务id")
    public ApiResponse<Map> selectMessageTaskIds(@RequestParam(value = "staffId") Long staffId) {
        List<Long> messageTaskIds = new ArrayList<>();
        List<Message> list = messageService.list(Wrappers.<Message>lambdaQuery()
                .eq(Message::getReceiverId, staffId)
                .eq(Message::getIsRead, YesNoEnum.NO)
                .eq(Message::getIsSend, YesNoEnum.YES)
                .ne(Message::getMessageTaskId, -1)
                .ne(Message::getMessageTaskId, -2));
        messageTaskIds = list.stream().map(Message::getMessageTaskId).collect(Collectors.toList());
        //去重
        List<Long> distinctMessageTaskIds = messageTaskIds.stream().distinct().collect(Collectors.toList());
        HashMap<String, List<Long>> map = new HashMap();
        List<Long> readNo = (List)list.stream().map(Message::getMessageTaskId).collect(Collectors.toList());
        List<Long> distinctReadNo = (List)readNo.stream().distinct().collect(Collectors.toList());
        map.put("readNo", distinctReadNo);
        List<Message> ids = this.messageService.list(Wrappers.<Message>lambdaQuery()
                .eq(Message::getReceiverId, staffId)
                .eq(Message::getIsRead, YesNoEnum.YES)
                .eq(Message::getIsSend, YesNoEnum.YES)
                .ne(Message::getMessageTaskId, -1)
                .ne(Message::getMessageTaskId, -2));
        List<Long> read = (List)ids.stream().map(Message::getMessageTaskId).collect(Collectors.toList());
        List<Long> distinctRead = (List)read.stream().distinct().collect(Collectors.toList());
        map.put("read", read);
        return ApiResponse.ok(map);
    }


    /**
     * 分页查询
     *
     * @param param    查询参数
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<Message>> selectByPage(
            MessageParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        List<Long> messageTaskIds = param.getMessageTaskIds();
        if (!messageTaskIds.isEmpty()){
            messageTaskIds.addAll(Collections.singletonList(param.getMessageTaskId()));
            param.setMessageTaskId(null);
            param.setMessageTaskIds(messageTaskIds);
        }
        QueryWrapper<Message> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage authorityPage = new Page<>(pageNum, pageSize);
        IPage page = this.messageService.page(authorityPage, wrapper);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Message> records = page.getRecords();
        records.forEach(message -> {
            if (message.getMessageTaskId().equals(-1)) {
                message.setMessageTypeEnum(MessageTypeEnum.DUTY); // 值班
                message.setMessageSystemEnum(MessageSystemEnum.INNER);
            } else if (message.getMessageTaskId().equals(-2)) {
                message.setMessageTypeEnum(MessageTypeEnum.NORMAL_RESULT); // 普通消息_成果
                message.setMessageSystemEnum(MessageSystemEnum.TIR_WX_WXC);
            } else if (message.getMessageTaskId() > 0) {
                message.setMessageTypeEnum(MessageTypeEnum.OFFICE); // 办公
                message.setMessageSystemEnum(MessageSystemEnum.TIR_WX_WXC);
            }
        });
        List<StaffMessageRefuse> staffMessageRefuseList = staffMessageRefuseService.list(Wrappers.<StaffMessageRefuse>lambdaQuery()
                .eq(StaffMessageRefuse::getStaffId, user.getStaffId())
                .eq(StaffMessageRefuse::getStatus, YesNoEnum.YES)
        );
        if (CollectionUtils.isNotEmpty(staffMessageRefuseList)) {
            Map<String, List<StaffMessageRefuse>> refuseMap = staffMessageRefuseList.stream().collect(Collectors.groupingBy(StaffMessageRefuse::getName));
            List<MessageDto> messageDtos = (List<MessageDto>) authorityPage.getRecords().stream().map(item -> {
                Message message = (Message) item;
                MessageDto messageDto = new MessageDto();
                BeanUtils.copyProperties(message, messageDto);
                if (Objects.equals(message.getMessageTaskId(), -1L) && refuseMap.containsKey(message.getName())) {
                    messageDto.setRefuseStatus(YesNoEnum.YES);
                }
                return messageDto;
            }).collect(Collectors.toList());
            authorityPage.setRecords(messageDtos);
        }
        return ApiResponse.ok(authorityPage);
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(MessageParam param) {
        QueryWrapper<Message> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.messageService.count(wrapper));
    }

    /**
     * count查询
     *
     * @param messageDto 查询参数
     * @return int
     */
    @PostMapping("/parent/update/message")
    @ApiOperation("家长填写message")
    public ApiResponse<Boolean> parentUpdate(@RequestBody MessageDto messageDto) {
        String username = JWTUtils.getUsername(messageDto.getToken());
        if (!JWTUtils.verify(messageDto.getToken(), username, "password_zhzx")) {
            throw new ApiCode.ApiException(-5, "Invalid auth token!");
        }

        return ApiResponse.ok(this.messageService.update(Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsSend, YesNoEnum.YES)
                .set(Message::getIsRead, YesNoEnum.YES)
                .set(Message::getIsWrite, YesNoEnum.YES)
                .set(Message::getContent, messageDto.getContent())
                .eq(Message::getId, messageDto.getId())
        ));
    }

    /**
     * count查询
     *
     * @param messageDto 查询参数
     * @return int
     */
    @PostMapping("/parent/get/message")
    @ApiOperation("家长获取message")
    public ApiResponse<Message> parentRead(@RequestBody MessageDto messageDto) {
        String username = JWTUtils.getUsername(messageDto.getToken());
        if (!JWTUtils.verify(messageDto.getToken(), username, "password_zhzx")) {
            throw new ApiCode.ApiException(-5, "Invalid auth token!");
        }
        this.messageService.update(Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsRead, YesNoEnum.YES)
                .eq(Message::getId, messageDto.getId())
        );
        return ApiResponse.ok(this.messageService.getById(messageDto.getId()));
    }

    @PostMapping("/read/message")
    @ApiOperation("站内已读消息")
    public ApiResponse<Boolean> readMessage(@RequestBody MessageDto messageDto) {
        return ApiResponse.ok(this.messageService.update(Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsRead, YesNoEnum.YES)
                .eq(Message::getId, messageDto.getId())
        ));
    }
}
