package cn.myframe.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class TwdConstant
{
	
	
	public static final Integer[] HOURS={2,6,10,14,18,22};  //时间段
	
	/**
     * 上注释说明 1:入院; 2：出院;3:转入;4:手术;5:转院;6:死亡;7:分娩
     */
	public static class Comment1Type
	{

		public static final long RUYUAN = 1l;

		public static final long CHUYUAN = 2l;

		public static final long ZHUANRU = 3l;

		public static final long SHOUSHU = 4l;

		public static final long ZHUANYUAN = 5l;

		public static final long SIWANG = 6l;
		
		public static final long FENMIAN = 7l;

		public static final String getName(long lCode)
		{
			String strReturn = "";
			switch ((int) lCode)
			{
				case (int) RUYUAN:
					strReturn = "入院";
					break;
				case (int) CHUYUAN:
					strReturn = "出院";
					break;
				case (int) ZHUANRU:
					strReturn = "转入";
					break;
				case (int) SHOUSHU:
					strReturn = "手术";
					break;
				case (int) ZHUANYUAN:
					strReturn = "转院";
					break;
				case (int) SIWANG:
					strReturn = "死亡";
					break;
				case (int) FENMIAN:
					strReturn = "分娩";
					break;
			}
			return strReturn;
		}
	}
	
	
	/**
     * 身高类型 1:数值; 2：其它;
     */
	public static class HeightType
	{

		public static final long NUMBER = 1l;

		public static final long OTHER = 2l;

		public static final String getName(long lCode)
		{
			String strReturn = "";
			switch ((int) lCode)
			{
				case (int) NUMBER:
					strReturn = "数值";
					break;
				case (int) OTHER:
					strReturn = "其它";
					break;
			}
			return strReturn;
		}
	}
	
	/**
	 * 体重类型
	 * 	{"id":"1","text":"数值"},{"id":"2","text":"平车"},{"id":"3","text":"卧床"}
	 *  {"id":"4","text":"抱入"},{"id":"5","text":"轮椅"}
	 */
	public static class WeightType
	{
		
		public static final long NUMBER = 1l;

		public static final long PINGCHE = 2l;
		
		public static final long WOCHUANG = 3l;
		
		public static final long BAORU = 4l;
		
		public static final long LUNYI = 5l;

		public static final String getName(long lCode)
		{
			String strReturn = "";
			switch ((int) lCode)
			{
				case (int) NUMBER:
					strReturn = "数值";
					break;
				case (int) PINGCHE:
					strReturn = "平车";
					break;
				case (int) WOCHUANG:
					strReturn = "卧床";
					break;	
				case (int) BAORU:
					strReturn = "抱入";
					break;		
				case (int) LUNYI:
					strReturn = "轮椅";
					break;			
			}
			return strReturn;
		}
	}
	
	

}