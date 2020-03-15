package cn.myframe.chart;

import java.awt.BasicStroke;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.*;

public  class ChartShape 
{
	public static int ROUND = 1; //圆
	public static int RECTANGLE = 2;// 矩形
	public static int LINE = 3; //线
	public static int TRIANGLE = 4; //三角形
	public static float LINE_TINY = 0.7f; //线的粗细(最细)
	public static float LINE_THIN = 1.0f; //线的粗细(细)
	public static float LINE_NORMAL = 1.5f; //线的粗细(一般)
	public static float LINE_WIDE = 2.0f; //线的粗细(宽)
	public static float LINE_BOLD = 3.0f; //线的粗细(最宽)
	
	public static Shape getShape(Integer shape)
	{
		return getShape(shape, 8, 8);
	}
	
	public static Shape getShape(int shape, double width, double height)
	{
		if(shape == ROUND)
		{
			return new Ellipse2D.Double(-width/2 ,-height/2, width, height);
		}
		if(shape == RECTANGLE)
		{
			return new Rectangle2D.Double(0,0, width, height);
		}
		if(shape == TRIANGLE)
		{
			int w = new Double(width).intValue();
			Polygon poly = new Polygon();
			
			poly.addPoint(0, - 2 * w);
			poly.addPoint(-w, 0);
			poly.addPoint(w, 0);
			return poly;
		}
		return new Ellipse2D.Double(0,0, width, height);
	}
	
	public static Shape getLine(int shape, Point2D point1, Point2D point2)
	{
		return new Line2D.Double(point1, point2);
	}
	public static Shape getLine(int shape, double x1, double y1, double x2, double y2)
	{
		if(shape == LINE)
		{
			return new Line2D.Double(x1, y1, x2, y2);
		}
		
		return new Line2D.Double(x1, y1, x2, y2);
	}
	
	/**
	 * 绘制线的画笔
	 * @param width 线的宽度 ChartShape.LINE_NORMAL
	 * @return
	 */
	public static Stroke getLineStoke(float width, boolean isDash)
	{
		if(width < 0)
		{
			width = 0.0f;
		}
		if(isDash)
		{
			return new BasicStroke(width, 1, 1, 1.0f, new float[]{0.5f,3.0f},0.0f);
		}
		else
		{
			return new BasicStroke(width, 1, 1, 1.0f);
		}
		
	}
}
