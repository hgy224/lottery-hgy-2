package com.hgy.lottery.rpc.req;

import com.hgy.lottery.common.Result;
import com.hgy.lottery.rpc.dto.ActivityDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityReq implements Serializable {
    private Long activityId;
}
