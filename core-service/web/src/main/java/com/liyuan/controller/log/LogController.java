package com.liyuan.controller.log;

import com.liyuan.api.Result;
import com.liyuan.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author liyuan
 * @date 2023/3/11
 * @project exam-cloud
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("log")
@Api(tags = "日志接口")
public class LogController {

    private final LogService logService;

    @ApiOperation("日志查询（分页）")
    @GetMapping("/page/{page}/{size}")
    public Result logByPage(@ApiParam("页码") @PathVariable(value = "page") Integer pageNum,
                            @ApiParam("条数") @PathVariable(value = "size") Integer pageSize,
                            @ApiParam("服务名称") @RequestParam(value = "serviceName") String serviceName,
                            @ApiParam("日志类型") @RequestParam(value = "type") String type,
                            @ApiParam("日期") @RequestParam(value = "date") Date date){
        Result result = logService.logByPage(pageNum, pageSize, serviceName, type, date);
        return result;
    }

    @ApiOperation("登录日志查询（分页）")
    @GetMapping("login/page/{page}/{size}")
    public Result loginLogByPage(@ApiParam("页码") @PathVariable(value = "page") Integer pageNum,
                            @ApiParam("条数") @PathVariable(value = "size") Integer pageSize,
                            @ApiParam("日期") @RequestParam(value = "date") Date date,
                            @ApiParam("用户名") @RequestParam(value = "username",required = false) String username,
                            @ApiParam("客户端ip") @RequestParam(value = "clientIP",required = false) String clientIP,
                            @ApiParam("开始时间") @RequestParam(value = "startTime",required = false) Date startTime,
                            @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) Date endTime
                                 ){
        Result result = logService.loginLogByPage(pageNum,pageSize,date,username,clientIP,startTime,endTime);
        return result;
    }

    /**
     * @description 注销
     * @route DELETE: /forcedOffline
     */
    @ApiOperation(value = "强制下线")
    @DeleteMapping("/forcedOffline")
    public Result forcedOffline(@ApiParam("令牌") @RequestParam(value = "token")String token) {
        logService.forcedOffline(token);
        return Result.success("注销成功");
    }


}
