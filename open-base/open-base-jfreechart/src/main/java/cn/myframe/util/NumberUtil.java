package cn.myframe.util;
import java.math.BigDecimal;
import java.text.DecimalFormat;
public class NumberUtil {
	
	
	public static double add(Double d1,Double d2){
		return add(d1, d2, 2);
	}
	
	public static double sub(Double d1,Double d2){
	   return sub(d1, d2,2);
	}
	
	public static double mul(Double d1,Double d2){
		BigDecimal decimal1 =new BigDecimal(d1);
		BigDecimal decimal2 =new BigDecimal(d2);
		return mul(d1, d2, 2);
	}
	
	public static double div(Double d1,Double d2){
		BigDecimal decimal1 =new BigDecimal(d1);
		BigDecimal decimal2 =new BigDecimal(d2);
		return div(d1, d2,2);
	}
	
	public static double add(Double d1,Double d2,int scale){
		BigDecimal decimal1 =new BigDecimal(d1);
		BigDecimal decimal2 =new BigDecimal(d2);
		return decimal1.add(decimal2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double sub(Double d1,Double d2,int scale){
		BigDecimal decimal1 =new BigDecimal(d1);
		BigDecimal decimal2 =new BigDecimal(d2);
		return decimal1.subtract(decimal2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double mul(Double d1,Double d2,int scale){
		BigDecimal decimal1 =new BigDecimal(d1);
		BigDecimal decimal2 =new BigDecimal(d2);
		return decimal1.multiply(decimal2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double div(Double d1,Double d2,int scale){
		BigDecimal decimal1 =new BigDecimal(d1);
		BigDecimal decimal2 =new BigDecimal(d2);
		return decimal1.divide(decimal2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	
	public static double round(Double value){
		return new Double( new DecimalFormat( ".00" ).format(value) );
	}
	public static void main(String[] args) {
		System.out.println(sub(8.5163,8.48,2));
	}

}
