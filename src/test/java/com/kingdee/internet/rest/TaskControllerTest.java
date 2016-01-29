package com.kingdee.internet.rest;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class TaskControllerTest {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    public static final String RETRIEVE_URL = "http://localhost:8080/task_distribution/api/v1/task/retrieve";
    public static final String ADD_URL = "http://localhost:8080/task_distribution/api/v1/task/add";
    public static final String ADD_PARAMS = "{     \"cardNum\": \"6240000104534786\",     \"params\": {         \"respData\": {             \"bankLoginNo\": \"6240000103008089\",             \"bankType\": \"icbc\",             \"bankPassword\": \"9eegA8GcpgK1EpAnnSYcBA==\",             \"startDate\": \"20150901\",             \"userId\": \"188888\",             \"isUpdate\": \"no\",             \"isAgreeProtocol\": \"yes\"         }     },     \"passwd\": \"9eegA8GcpgK1EpAnnSYcBA==\",     \"bankType\": \"icbc\",     \"userId\": \"123\" }";
    public static final int COUNT = 0;

    @Test
    public void taskRetrieve() throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    String result = Request.Post(RETRIEVE_URL)
                            .bodyForm(Form.form().add("bankType", "icbc").build())
                            .execute()
                            .returnContent()
                            .asString();
                    logger.info(result);
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        for(int i = 0; i < COUNT; i++) {
            executor.execute(task);
        }
        countDownLatch.await(100, TimeUnit.SECONDS);
    }

    @Test
    public void taskAdd() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        Executor executor = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    String result = Request.Post(ADD_URL)
                            .bodyForm(Form.form().add("params", ADD_PARAMS).build())
                            .execute()
                            .returnContent()
                            .asString();
                    logger.info(result);
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        for(int i = 0; i < COUNT; i++) {
            executor.execute(task);
        }
        countDownLatch.await(COUNT, TimeUnit.SECONDS);
    }
}
