package cn.myframe.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateUtils;
public class DateUtil {
	
	/**
	 * 依据给定的开始时间、结束时间按求出相应的日期集合
	 * @param beginDate 开始时间
	 * @param endDate  结束时间
	 * @return 日期集合
	 */
	public static List<String> getBetweenDate(String beginDate,String endDate){
		
		  List<String> dateList=new ArrayList<String>();
		try{
		
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date begin =sdf.parse(beginDate);
			Date end=sdf.parse(endDate);
			if(begin.after(end)){  //开始时间不能大于结束时间
				System.out.println(beginDate+"不能大于"+endDate);
				return dateList;
			}
			Calendar  beginCalendar=GregorianCalendar.getInstance(); 
			beginCalendar.setTime(begin);
			Calendar  endCalendar=GregorianCalendar.getInstance(); 
			endCalendar.setTime(end);
			int beginYear=beginCalendar.get(Calendar.YEAR);   // 开始年份
			int beginMonth=beginCalendar.get(Calendar.MONTH)+1;  //开始月份
			int beginDay=beginCalendar.get(Calendar.DAY_OF_MONTH);  //开始天
			int endYear=endCalendar.get(Calendar.YEAR);
			int endMonth=endCalendar.get(Calendar.MONTH)+1;   //结束月份
			int endDay=endCalendar.get(Calendar.DAY_OF_MONTH);   //结束天
				for(int i=beginYear;i<=endYear;i++){
					int tempEndMonth=12;
					int tempBeginMonth=1;
					if(i==endYear)
						tempEndMonth=endMonth;
					if(i==beginYear)
						tempBeginMonth=beginMonth;
				    for(int j=tempBeginMonth;j<=tempEndMonth;j++){
                          int tempDays=getDays(i, j);
                          int tempBeginDays=1;
                          if(i==endYear&&j==tempEndMonth)
                        	  tempDays=endDay;
                          if(i==beginYear&&j==tempBeginMonth)
                        	  tempBeginDays=beginDay;
                          for(int k=tempBeginDays;k<=tempDays;k++){
                        	  dateList.add(i+"-"+(j<10?"0"+j:j)+"-"+(k<10?"0"+k:k));
                          }
				    }
				}
			
		}catch(Exception e){
			  dateList.clear();
			e.printStackTrace();
		}
		
		
		return dateList;
	}
	
	//判断是否闰年
	public static boolean isRunNian(int year){
		if((year%4==0&&year%100!=0)||year%400==0)
			return true;
		return false;
	}
	
	//根据年度，月份求出当月的天数
	public static int getDays(int year,int month){
		if(month==1 ||month==3||month==5||month==7||month==8||month==10||month==12)
			return 31;
		else if(month==2){
			if(isRunNian(year))
				return 29;
			else 
				return 28;
		}
		else 
			return 30;
	}
	
	//判断是否是周末
	public  static boolean isWeekend(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date time = null;
		try {
			time = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 格式化日期 
	 * @param date   日期
	 * @param pattern  格式
	 * @return  日期字符串
	 */
	public static String formatDate(Date date,String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
	/**
	 * 格式化日期 格式为"yyyy-MM-dd"
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatDate(Date date){
		return formatDate(date,"yyyy-MM-dd");
	}
	
	/**
	 * 获得当前日期 指定格式为"yyyy-MM-dd"
	 * @return
	 */
	public static String getCurrentDateStr(){
		return formatDate(new Date());
	}
	
	public static String getCurrentDateStr(String pattern){
		return formatDate(new Date(),pattern);
	}
	
	/**
	 * 获得当前日期，不带时、分、秒
	 * @return
	 */
	public static Date getCurrentDate(){
		Date date =DateUtil.parseDate(DateUtil.formatDate(new Date()));
		return date;
	}
	
	/**
	 * 获得当前日期，带时、分、秒
	 * @return
	 */
	public static Date getCurrentDateTime()
	{
		return new Date();
	}
	
	
	/**
	 * 把字符串类型的日期转换成Date类型
	 * @param strDate
	 * @return
	 */
	public static Date parseDate(String strDate){
		return parseDate(strDate,"yyyy-MM-dd");
	}
	
	/**
	 * 把字符串类型的日期转换成Date类型
	 * @param strDate
	 * @param  转换日期格式 
	 * @return
	 */
	public static Date parseDate(String strDate,String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date date=null;
		try {
		   date=df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date ;
	}
	
	public static int getYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	public static int getMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+1;
	}
	
	public static int getDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getHour(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public static String addDay(String strDate,int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.parseDate(strDate));
		cal.add(Calendar.DAY_OF_MONTH, days);
		return DateUtil.formatDate(cal.getTime());
	}
	
	public static String addHour(String strDate,int hours){
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.parseDate(strDate));
		cal.add(Calendar.HOUR_OF_DAY, hours);
		return DateUtil.formatDate(cal.getTime(),"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 
	 * @param d1
	 * @param d2
	 * @return  两个日期相差的天数
	 */
	public static long getDateDiff(Date d1,Date d2){
	   return  (d1.getTime()   -   d2.getTime())   /   (24   *   60   *   60   *   1000); 
	}
	
	public static String getDateOfWeekMondy(){
		Calendar calendar=Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return DateUtil.formatDate(calendar.getTime());
	}
	
	public static Map<String, List<String>> getAllWeekdays(Date indate,Date outdate)
	{
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		if (indate == null )
		{
			return resultMap;
		}
		if (outdate == null) //出院日期为空
			outdate = getCurrentDate();
		List<String> dateList = getBetweenDate(formatDate(indate),
				formatDate(outdate));
		int index = 0;
		List<String> list = new ArrayList<String>();
		for (String strdate : dateList)
		{
			index++;
			list.add(strdate);
			if (index > 0 && (index % 7 == 0 || index == dateList.size()))
			{

				if (list.size() != 7)
				{
					String lastdate = list.get(list.size() - 1);
					int count = 0;
					for (int i = list.size(); i < 7; i++)
					{
						count++;
						list.add(addDay(lastdate, count));
					}
				}
				resultMap
						.put((index % 7 == 0 ? index / 7 : index / 7 + 1) + "",
								list);
				list = new ArrayList<String>();
			}
		}

		return resultMap;
	}
	

	/**
	 * 通过入院时间、出院时间查询病人住院周期
	 * @param indate
	 * @param outdate
	 * @return
	 */
	public static Map getWeekMap(Date indate, Date outdate)
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (indate == null )
		{
			return map;
		}
		if (outdate == null) //出院日期为空
			outdate = getCurrentDate();
		List<String> dateList = getBetweenDate(formatDate(indate),formatDate(outdate));
		int days = dateList.size();
		for (int i = 1; i <= (days % 7 == 0 ? days / 7 : days / 7 + 1); i++)
		{
			map.put(i + "", i + "");
		}
		return map;
	}
	
	public static Object[] getOneWeekDays(Date indate, Date outdate, Date date)
	{
		Object[] obj = new Object[2];
		List<String> tempList = new ArrayList<String>();
		String week = "";
		Map<String, List<String>> resultMap = getAllWeekdays(indate, outdate);
		for (Iterator<Entry<String, List<String>>> it = resultMap.entrySet().iterator(); it.hasNext();)
		{
			Entry<String, List<String>> entry = it.next();
			boolean flag = false;
			for (String strdate : entry.getValue())
			{
				if (strdate.equals(formatDate(date)))
				{
					flag = true;
					break;
				}
			}
			if (flag)
			{
				week = entry.getKey();
				tempList = entry.getValue();
				break;
			}

		}
		obj[0] = week;
		obj[1] = tempList;
		return obj;
	}
	
	/**
	 * 把给定的日期转换成体温单需要的日期格式
	 * @param date
	 * @return
	 */
	public static List<String> getOneWeekDaysForTwd(List<String> dateList)
	{
		if(dateList==null||dateList.size()==0)
		{
			return new ArrayList<String>();
		}
		List<String> resultList=new ArrayList<String>() ;
		resultList.add(dateList.get(0));
		for(int i=0;i<dateList.size()-1;i++)
		{
		compareDate(dateList.get(i),dateList.get(i+1),resultList);
		}
		return resultList;
	}
	
	private static void compareDate(String strdate1 ,String strdate2 ,List<String> dateList)
	{
		Date date1=parseDate(strdate1);
		Date date2=parseDate(strdate2);
		int year1=getYear(date1);
		int month1=getMonth(date1);
		int day1=getDay(date1);
		int year2=getYear(date2);
		int month2=getMonth(date2);
		int day2=getDay(date2);
		if(year1!=year2)
		{
			dateList.add(formatDate(date2));
		}
		else if(year1==year2&&month1!=month2)
		{
			dateList.add(formatDate(date2,"MM-dd"));
		}
		else if(year1==year2&&month1==month2)
		{
			dateList.add(formatDate(date2,"dd"));
		}
	}
	
	public static void main(String[] args)
	{
		String date="2012-04-23";
		System.out.println(DateUtil.addHour(date, 2));
	}

	
	

}
