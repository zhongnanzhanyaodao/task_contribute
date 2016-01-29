package com.kingdee.internet.rest;

import com.alibaba.fastjson.JSON;
import com.kingdee.internet.entity.Task;
import com.kingdee.internet.service.TaskService;
import com.kingdee.internet.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/task")
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/retrieve", produces = "application/json; charset=utf-8")
    public Task retrieveTask(@RequestParam(value = "bankType") String bankType) {
        logger.debug("retrieve task with bankType: {}", bankType);
        return taskService.retrieveTask(bankType);
    }

    @RequestMapping(value = "/add", produces = "application/json; charset=utf-8")
    public Response<String> addTask(@RequestParam(value = "params") String params) {
        logger.debug("add task {}", params);
        if(StringUtils.isBlank(params)) {
            return Response.INVALID_PARAMETER;
        }
        Task task = JSON.parseObject(params, Task.class);
        if(StringUtils.isAnyBlank(task.getUserId(), task.getCardNum(), task.getPasswd())) {
            return Response.INVALID_PARAMETER;
        }
        task = taskService.addTask(task);
        return Response.create(200, "success", task.getTaskId());
    }
}
