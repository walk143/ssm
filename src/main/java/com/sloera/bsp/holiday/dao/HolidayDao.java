package com.sloera.bsp.holiday.dao;

import com.sloera.bsp.holiday.po.HolidayBean;
import com.sloera.mng.core.ibatis.MapperBaseDao;
import org.springframework.stereotype.Repository;

@Repository("holidayDao")
public class HolidayDao extends MapperBaseDao<HolidayBean> {
}
