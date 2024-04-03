/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Settings;
import com.zhzx.server.domain.WxXcxMessage;
import com.zhzx.server.dto.MessageCombineDto;
import com.zhzx.server.dto.xcx.WxXcxChatBookDto;
import com.zhzx.server.dto.xcx.WxXcxContactsDto;
import com.zhzx.server.rest.req.WxXcxMessageParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.SettingsService;
import com.zhzx.server.service.WxXcxMessageService;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.MessageCombineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
@Api(tags = "WxXcxMessageController", description = "微信小程序通知表管理")
@RequestMapping("/v1/tir/wx-xcx-message")
public class WxXcxMessageController {
    @Resource
    private WxXcxMessageService wxXcxMessageService;
    @Resource
    private SettingsService settingsService;

    @GetMapping("/chat-book")
    @ApiOperation("查询通讯录")
    @SneakyThrows
    public ApiResponse<Object> queryChatBook(@RequestParam(value = "schoolId") Long schoolId,
                                                 @RequestParam(value = "keyword", required = false) String keyword) {
        Settings chatBookSettings = settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .eq(Settings::getCode,"CHAT_BOOK_CACHE")
        );
        List<WxXcxChatBookDto> wxXcxChatBookDtoList = new ArrayList<>();
        if (null != chatBookSettings) {
            JSONObject jsonObject = JSONObject.parseObject(chatBookSettings.getParams());
            wxXcxChatBookDtoList = JSONObject.parseObject(jsonObject.getString(schoolId.toString()), new TypeReference<List<WxXcxChatBookDto>>() {
            });
            if (!StringUtils.isNullOrEmpty(keyword)) {
                wxXcxChatBookDtoList.removeIf(t -> {
                    List<WxXcxContactsDto> wxXcxContactsDtoList = t.getList();
                    wxXcxContactsDtoList.removeIf(p -> (!StringUtils.isNullOrEmpty(p.getName()) && !p.getName().contains(keyword)) || (!StringUtils.isNullOrEmpty(p.getPhone()) && p.getPhone().contains(keyword)));
                    return CollectionUtils.isEmpty(wxXcxContactsDtoList);
                });
            }
            //将编码转义的手机号码转义回数字
            wxXcxChatBookDtoList.forEach(dto -> {
                List<WxXcxContactsDto> wxXcxContactsDtos = dto.getList();
                wxXcxContactsDtos.forEach(wxXcxContacts -> {
                    String phone = wxXcxContacts.getPhone();
                    phone = new String(Base64Utils.decodeFromString(phone));
                    wxXcxContacts.setPhone(phone);
                });
            });
        }
            return ApiResponse.ok(wxXcxChatBookDtoList);
    }

    @GetMapping("/chat-book-situation")
    @ApiOperation("查询通讯录")
    @SneakyThrows
    public ApiResponse<Object> queryChatBookAll() {
        Settings chatBookSettings = settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .eq(Settings::getCode,"CHAT_BOOK_CACHE")
        );
        Settings schoolyardSettings = settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .eq(Settings::getCode,"SCHOOLYARD_CACHE")
        );
        Map<String, Object> res = new HashMap<String, Object>() {
            {
                this.put("all", JSONObject.parseObject(chatBookSettings.getParams()).getInteger("all"));
                this.put("schoolList", JSONObject.parseArray(schoolyardSettings.getParams()));
            }
        };

        return ApiResponse.ok(res);
    }

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<WxXcxMessage> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.wxXcxMessageService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<WxXcxMessage> add(@RequestBody WxXcxMessage entity) {
        entity.setDefault().validate(true);
        this.wxXcxMessageService.save(entity);
        return ApiResponse.ok(this.wxXcxMessageService.getById(entity.getId()));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<WxXcxMessage>> selectByPage(
        WxXcxMessageParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<WxXcxMessage> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<WxXcxMessage> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.wxXcxMessageService.page(page, wrapper));
    }

    @GetMapping("/search-app")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<MessageCombineDto>> selectApp(
            MessageCombineVo messageCombineVo,
            @RequestParam(value = "orderByClause", defaultValue = "time desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.ok(this.wxXcxMessageService.pageApp(orderByClause, pageNum, pageSize, messageCombineVo));
    }

    @GetMapping("/count-app")
    @ApiOperation("获取数量")
    public ApiResponse<Object> countApp(
            MessageCombineVo messageCombineVo
    ) {
        return ApiResponse.ok(this.wxXcxMessageService.countApp(messageCombineVo));
    }

    @PutMapping("/update-is-read-app")
    @ApiOperation("状态更新")
    public ApiResponse<Object> updateIsReadApp(@RequestBody MessageCombineDto messageCombineDto) {
        return ApiResponse.ok(this.wxXcxMessageService.updateIsReadApp(messageCombineDto));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(WxXcxMessageParam param) {
        QueryWrapper<WxXcxMessage> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.wxXcxMessageService.count(wrapper));
    }

}
