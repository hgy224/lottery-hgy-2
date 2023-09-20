package com.hgy.lottery.infrastructure.dao;

import com.hgy.lottery.infrastructure.po.Activity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IActivityDao {

    @Insert("""
            INSERT INTO activity
            (activity_id, activity_name, activity_desc, begin_date_time,
             end_date_time, stock_count, take_count, state, creator)
            VALUE
            (#{activityId}, #{activityName}, #{activityDesc}, #{beginDateTime},
             #{endDateTime}, #{stockCount}, #{takeCount}, #{state}, #{creator})""")
    void insert(Activity req);

    @Select("SELECT * FROM activity")
    Activity queryActivityById(Long activityId);
}
