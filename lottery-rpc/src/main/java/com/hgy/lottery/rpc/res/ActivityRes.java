package com.hgy.lottery.rpc.res;

import com.hgy.lottery.common.Result;
import com.hgy.lottery.rpc.dto.ActivityDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityRes implements Serializable {
    private Result result;
    private ActivityDto activity;

    public ActivityRes(){}

    public ActivityRes(Result result, ActivityDto activity){
        this.result = result;
        this.activity = activity;

    }
}
