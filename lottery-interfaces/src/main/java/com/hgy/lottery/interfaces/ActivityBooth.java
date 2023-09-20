package com.hgy.lottery.interfaces;

import cn.hutool.core.bean.BeanUtil;
import com.hgy.lottery.common.Constants;
import com.hgy.lottery.common.Result;
import com.hgy.lottery.infrastructure.dao.IActivityDao;
import com.hgy.lottery.infrastructure.po.Activity;
import com.hgy.lottery.rpc.IActivityBooth;
import com.hgy.lottery.rpc.dto.ActivityDto;
import com.hgy.lottery.rpc.req.ActivityReq;
import com.hgy.lottery.rpc.res.ActivityRes;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class ActivityBooth implements IActivityBooth {
    @Resource
    private IActivityDao activityDao;

    @Override
    public ActivityRes queryActivityById(ActivityReq req) {
        Activity activity = activityDao.queryActivityById(req.getActivityId());
        ActivityDto activityDto = new ActivityDto();
        BeanUtil.copyProperties(activity, activityDto);
        return new ActivityRes(new Result(Constants.ResponseCode.SUCCESS.getCode(),
                Constants.ResponseCode.SUCCESS.getInfo()), activityDto);
    }
}
