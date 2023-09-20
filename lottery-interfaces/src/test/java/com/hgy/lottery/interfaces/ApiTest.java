package com.hgy.lottery.interfaces;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.hgy.lottery.infrastructure.dao.IActivityDao;
import com.hgy.lottery.infrastructure.po.Activity;

import com.hgy.lottery.rpc.req.ActivityReq;
import com.hgy.lottery.rpc.res.ActivityRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
@Slf4j
public class ApiTest {
    @Resource
    private IActivityDao activityDao;

    /**
     * 测试ActivityDao的两个方法
     */
    @Test
    public void testActivityDao(){
        // 测试插入操作
        Activity activity = new Activity();
        activity.setActivityId(10000L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("用于测试");
        activity.setBeginDateTime(new Date());
        activity.setEndDateTime(new Date());
        activity.setStockCount(5);
        activity.setTakeCount(10);
        activity.setState(1);
        activity.setCreator("hgy");
        activityDao.insert(activity);

        // 测试查询操作
        Activity activityRes = activityDao.queryActivityById(10000L);
        log.info(JSONUtil.toJsonStr(activityRes));
    }

    @Resource
    private ActivityBooth activityBooth;

    @Test
    public void testActivityBooth(){
        ActivityReq activityReq = new ActivityReq();
        activityReq.setActivityId(10000L);
        ActivityRes activityRes = activityBooth.queryActivityById(activityReq);
        log.info(JSONUtil.toJsonStr(activityRes));
    }
}
