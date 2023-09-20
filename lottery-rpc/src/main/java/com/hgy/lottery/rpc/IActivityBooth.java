package com.hgy.lottery.rpc;

import com.hgy.lottery.rpc.req.ActivityReq;
import com.hgy.lottery.rpc.res.ActivityRes;

public interface IActivityBooth {
    ActivityRes queryActivityById(ActivityReq req);
}
