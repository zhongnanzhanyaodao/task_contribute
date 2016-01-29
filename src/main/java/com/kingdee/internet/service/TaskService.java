package com.kingdee.internet.service;

import com.kingdee.internet.entity.Task;
import com.kingdee.internet.repository.TaskDao;
import com.kingdee.internet.util.CommonUtils;
import com.kingdee.internet.util.RedisManager;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
@Service
public class TaskService {
    public static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private static final String REDIS_QUEUE_SUFFIX = ":QUEUE";

    @Autowired
    private TaskDao taskDao;

    @Value("${task.queue.expire.minutes}")
    private long queueExpireMinutes;

    @Value("${task.expire.minutes}")
    private int taskExpireMinutes;

    @Autowired
    private RedisManager redisManager;

    public Task retrieveTask(String bankType) {
        Task task;
        do {
            task = redisManager.rightPop(bankType + REDIS_QUEUE_SUFFIX, Task.class);
            logger.info("rpop {}, and {} retrieved.", bankType,
                    task != null ? task.getTaskId() : "nothing");
            if(task == null) return task;
        } while (taskExpired(task));
        return task;
    }

    private boolean taskExpired(Task task) {
        if(task == null) return true;
        return DateUtils.addMinutes(task.getRequestDate(), taskExpireMinutes).getTime() < System.currentTimeMillis();
    }

    @Transactional(readOnly = false)
    public Task addTask(Task task) {
        String uuid = CommonUtils.uuid();
        task.setTaskId(uuid);
        task.setRequestDate(new Date());
        taskDao.save(task);
        redisManager.leftPush(task.getBankType() + REDIS_QUEUE_SUFFIX, task, queueExpireMinutes, TimeUnit.MINUTES);
        logger.info("lpush task with id: {} {}, and set the queue expire in {} minutes.",
                uuid, task.getBankType(), queueExpireMinutes);
        return task;
    }
}
