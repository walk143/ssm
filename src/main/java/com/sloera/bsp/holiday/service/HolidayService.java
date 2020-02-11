package com.sloera.bsp.holiday.service;

import com.sloera.bsp.holiday.dao.HolidayDao;
import com.sloera.bsp.holiday.po.HolidayBean;
import com.sloera.mng.core.utils.CTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("holidayService")
@Transactional
public class HolidayService {
    @Autowired
    private HolidayDao holidayDao;
    private Logger logger = LogManager.getLogger(HolidayService.class);

    public int initHoliday(String year, String month) {
        int start = 0;
        int end = 0;
        Calendar calendar = Calendar.getInstance();
        if (null == month) {
            start = 0;
            end = 12;
            try {
                holidayDao.executeSQL("delete HOLIDAY where year = '" + year + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // 只初始化第 month 月
            start = Integer.valueOf(month) - 1;
            end = start + 1;
            try {
                holidayDao.executeSQL("delete HOLIDAY where year = '" + year + "' and month = '" + end + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        List<HolidayBean> list = new ArrayList<>();
        for (int i = start; i < end; i++) {
            calendar.set(Calendar.MONTH, i);//月份从0开始
            for (int startDay = 1; startDay <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); startDay++) {
                calendar.set(Calendar.DAY_OF_MONTH, startDay);
                HolidayBean holidayBean = new HolidayBean();
                holidayBean.setId(CTools.getUUID());
                holidayBean.setYear(calendar.get(Calendar.YEAR));
                holidayBean.setMonth(calendar.get(Calendar.MONTH) + 1);
                holidayBean.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                holidayBean.setName("测试");
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    holidayBean.setType("1");
                } else {
                    holidayBean.setType("0");
                }
                holidayBean.setDateTime(calendar.getTime());
                holidayBean.setCreator("ceshi");
                holidayBean.setCreateTime(new Date());
                holidayBean.setStatus("1");
                //logger.info(holidayBean);
                list.add(holidayBean);
            }
        }
        int res = -1;
        try {
            res = holidayDao.save("com.sloera.bsp.holiday.insertForeach", list);
            System.out.println("res:"+res);
        } catch (Exception e) {
            logger.error(e);
        }
        return res;
    }
}
