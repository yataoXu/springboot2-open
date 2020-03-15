package cn.myframe.main;

import cn.myframe.chart.ChartProcessor;
import cn.myframe.chart.TwdChartService;
import cn.myframe.entity.PTempDetailInfo;
import cn.myframe.entity.PTempInfo;
import cn.myframe.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TwdDemo
{
	/**
	 * 生成体温单 函数
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)throws Exception
	{
		 ChartProcessor chart=new ChartProcessor();
		 TwdChartService service=new TwdChartService();
		 Map map=new HashMap();
		 List dateList=new ArrayList();
		 dateList.add("2013-04-21");
		 dateList.add("2013-04-22");
		 dateList.add("2013-04-23");
		 dateList.add("2013-04-24");
		 dateList.add("2013-04-25");
		 dateList.add("2013-04-26");
		 map.put("indate", "2013-04-20");
		 map.put("dateList", dateList);  //日期集合
		 map.put("twdDateList", dateList);  //体温单日期集合
		 List<PTempInfo> tempInfoList=new ArrayList<PTempInfo>();  //初始化体温单数据
		 for(int i=0;i<6;i++)
		 {
			 PTempInfo info=new PTempInfo();
			 info.setInspectionDate(DateUtil.parseDate(DateUtil.addDay("2013-04-21",i)));
			 info.setHeight(175+i);
			 info.setWeight(75.5+i);
			 info.setIntake(50+i);
			 info.setOutput(50+i);
			 info.setPoopCount(i);
			 info.setBloodPressure1("85/120");
			 info.setBloodPressure2("90/100");
			 info.setUrineVolume(15+i);
			 tempInfoList.add(info);
			 Set<PTempDetailInfo> detailList=new HashSet<PTempDetailInfo>();
			 for(int j=0;j<6;j++)
			 {
				 PTempDetailInfo detailInfo=new PTempDetailInfo();
				 detailInfo.setBreathe(25+i+j);
				 detailInfo.setHeartRate(60+j+i);
				 detailInfo.setPulse(80+j+i);
				 detailInfo.setTemperature(36.0+i+(j%2==0?0.3*j/2:-0.5));
				 detailInfo.setTemperatureType(1);
				 int hour=2+j*4;
				 detailInfo.setInspectionTime(DateUtil.parseDate(DateUtil.addHour(DateUtil.formatDate(info.getInspectionDate()), hour),"yyyy-MM-dd HH:mm:ss"));
				 detailList.add(detailInfo);
			 }
			 info.setDetailInfo(detailList);
		 }
		 map.put("tempInfoList", tempInfoList);
		 service.initAllData(chart, map);
         chart.createChart(null, null, "", "", "", false, 595, 842,"D:\\twd.png");
	}

}

