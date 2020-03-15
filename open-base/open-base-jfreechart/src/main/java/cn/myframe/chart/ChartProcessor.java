package cn.myframe.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
//import org.jfree.ui.TextAnchor;

public class ChartProcessor extends BaseChart
{

	@SuppressWarnings("unused")
	// 背景字列表
	private List<XYTextAnnotation> annoList;

	public static String AXIS_LEFT = "axis_left"; // 左轴;
	private Map<String, NumberAxis> axisMap = new HashMap<String, NumberAxis>(); // 轴映射
	private Map<String, Map<String, ChartBean>> axis_data_map = new HashMap<String, Map<String, ChartBean>>(); // 每条轴的数据集
	private Map<String, XYLineAndShapeRenderer> axit_render_map = new HashMap<String, XYLineAndShapeRenderer>(); // 每条轴和renderer的映射
	private Map<String, Integer> axis_series_index_map = new HashMap<String, Integer>();// 每条轴上的线索引映射

	// 背景颜色
	private Color bgColor;

	// 水平底线的颜色
	private Color bgHorizontalColor;
	// 竖直底线颜色
	private Color bgVerticalColor;

	private NumberAxis domainAxis;

	public ChartProcessor()
	{
		annoList = new ArrayList<XYTextAnnotation>();

		// 初始化轴
		axisMap.put(ChartProcessor.AXIS_LEFT, new NumberAxis());

		// 每条轴的数据集对象初始化
		axis_data_map.put(ChartProcessor.AXIS_LEFT, new HashMap<String, ChartBean>());

		// 每条轴的renderer初始化
		axit_render_map.put(ChartProcessor.AXIS_LEFT, new XYLineAndShapeRenderer());

		// 每条轴上的曲线索引初始化
		axis_series_index_map.put(ChartProcessor.AXIS_LEFT, new Integer(0));

	}

	/**
	 * 画图方法
	 * 
	 * @param title
	 *            图片的标题（图片的描述）
	 * @param xDesc
	 *            x 轴的描述
	 * @param ylDesc
	 *            左y轴的描述
	 * @param yrDesc
	 *            右Y轴的描述
	 * @param showLineDesc
	 *            是否显示每条线的描述（即每条曲线的含义,在x轴下面列出每条线的含义）
	 * @param width
	 *            输出图片的宽度
	 * @param height
	 *            输出图片的高度
	 * @throws Exception
	 */
	public void createChart(String title, String xDesc, String ylDesc, String ylsDesc, String yrDesc,
			boolean showLineDesc, int width, int height,String filename) throws Exception
	{
		JFreeChart chart = ChartFactory.createXYLineChart(title, xDesc, ylDesc,
				createDataset(ChartProcessor.AXIS_LEFT), PlotOrientation.VERTICAL, showLineDesc, true, false);
		// 添加子标题
		this.addTitle(chart);
		XYPlot plot = chart.getXYPlot();
		chart.setTextAntiAlias(false);
		// 设置背景图片
		this.setBgImg(plot);
		// 添加背景字
		for (XYTextAnnotation anno : annoList)
		{
			plot.addAnnotation(anno);
		}

		GradientPaint bg = new GradientPaint(0, 1000, Color.white, 0, 800, Color.white); // 设置背景色
		plot.setBackgroundPaint(bg);
		plot.setRangeGridlinePaint(bgHorizontalColor);
		plot.setDomainGridlinePaint(bgVerticalColor);
		/**
		 * 设置各条轴
		 */
		if (axisMap.get(ChartProcessor.AXIS_LEFT) != null && axis_data_map.get(ChartProcessor.AXIS_LEFT) != null
				&& !axis_data_map.get(ChartProcessor.AXIS_LEFT).isEmpty())
		{
			NumberAxis axisLeft = axisMap.get(ChartProcessor.AXIS_LEFT);
			axisLeft.setLabel(ylDesc);
			plot.setRangeAxis(0, axisLeft);
			plot.setRenderer(0, axit_render_map.get(ChartProcessor.AXIS_LEFT));
		}

		if (domainAxis != null)
		{
			domainAxis.setLabel(xDesc);
			domainAxis.setAutoRangeIncludesZero(false);
			domainAxis.setLowerMargin(0);
			plot.setDomainAxis(domainAxis);
		}
		Shape shape = new Rectangle(20, 10);
		ChartEntity entity = new ChartEntity(shape);
		StandardEntityCollection coll = new StandardEntityCollection();
		coll.add(entity);
		ChartRenderingInfo info = new ChartRenderingInfo(coll);
		File file=new File(filename);
		//ChartUtilities.saveChartAsJPEG(file, chart, width, height);
		ChartUtils.saveChartAsJPEG(file, chart, width, height);
		axis_data_map.get(ChartProcessor.AXIS_LEFT).clear();
        System.out.println("Create image success! path :"+filename);
	}

	/**
	 * 设置某条轴想要显示的数据<br>
	 * 
	 * @param axisName
	 *            轴的名称
	 * @param lineName
	 *            线的名称
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 * @return 数据是否加入成功
	 */
	public boolean addData(String axisName, String lineName, Number x, Number y)
	{
		if (lineName == null || "".equals(lineName.trim()) || x == null || y == null)
			return false;
		if (axisName == null || axisMap.get(axisName) == null)
			return false;
		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (map.containsKey(lineName))
			{
				ChartBean seriesbean = map.get(lineName);
				if (seriesbean.getSeries() != null)
					seriesbean.getSeries().add(x, y);
			}
			else
			{
				XYSeries series = new XYSeries(lineName);
				series.add(x, y);
				ChartBean bean = new ChartBean();
				bean.setSeries(series);
				map.put(lineName, bean);
			}
		}

		return true;

	}

	/**
	 * 设置某条轴曲线是否显示各个坐标点( 平滑曲线或者标明坐标点的曲线,设置某条轴上的所有线的点)
	 * 
	 * @param lineShapeVisible
	 *            (true:显示 false:不显示)
	 */
	public void setLineShapeVisible(String axisName, boolean lineShapeVisible)
	{
		if (axisName != null && !"".equals(axisName.trim()))
		{
			if (axit_render_map.get(axisName) != null)
			{
			//	axit_render_map.get(axisName).setBaseShapesVisible(lineShapeVisible);
			}
		}
	}

	/**
	 * 设置某条轴具体某条曲线是否显示各个坐标点
	 * 
	 * @param axisName
	 *            轴名称
	 * @param lineName
	 *            曲线名
	 * @param flag
	 */
	public void setLineShapeVisible(String axisName, String lineName, boolean flag)
	{

		if (lineName == null)
			return;

		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (map.containsKey(lineName))
			{
				ChartBean bean = map.get(lineName);
				bean.setShapeVisible(flag);
			}
			else
			{
				ChartBean bean = new ChartBean();
				bean.setShapeVisible(flag);
				map.put(lineName, bean);
			}
		}

	}

	/**
	 * 设置某条轴具体某条曲线是否是虚线
	 * 
	 * @param lineName
	 *            曲线名
	 * @param flag
	 */
	public void setLineDash(String axisName, String lineName, boolean flag)
	{

		if (lineName == null)
			return;
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (map.containsKey(lineName))
			{
				ChartBean bean = map.get(lineName);
				bean.setLineDash(flag);
			}
			else
			{
				ChartBean bean = new ChartBean();
				bean.setLineDash(flag);
				map.put(lineName, bean);
			}
		}

	}

	/**
	 * 设置某条轴具体某条曲线是否显示线条本身
	 * 
	 * @param lineName
	 *            曲线名
	 * @param flag
	 */
	public void setLineVisible(String axisName, String lineName, boolean flag)
	{
		if (lineName == null)
			return;
		if (axisName == null || axisMap.get(axisName) == null)
			return;

		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (map.containsKey(lineName))
			{
				ChartBean bean = map.get(lineName);
				bean.setLineVisible(flag);
			}
			else
			{
				ChartBean bean = new ChartBean();
				bean.setLineVisible(flag);
				map.put(lineName, bean);
			}
		}

	}

	/**
	 * 设置背景颜色
	 * 
	 * @param color
	 */
	public void setBackgroundColor(Color color)
	{
		if (color != null)
			this.bgColor = color;
	}

	/**
	 * 设置水平底线的颜色
	 * 
	 * @param color
	 */
	public void setBgHorizontalLineColor(Color color)
	{
		if (color != null)
		{
			this.bgHorizontalColor = color;
		}
	}

	/**
	 * 设置竖直底线的颜色
	 * 
	 * @param color
	 */
	public void setBgVerticalLineColor(Color color)
	{
		if (color != null)
		{
			this.bgVerticalColor = color;
		}
	}

	/**
	 * 设置某条轴各曲线的颜色
	 * 
	 * @param lineName
	 *            要设置颜色的线的名称
	 * @param color
	 *            线的颜色
	 */
	public void setLineColor(String axisName, String lineName, Color color)
	{
		if (lineName == null)
			return;
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (map.containsKey(lineName))
			{
				ChartBean bean = map.get(lineName);
				bean.setLineColor(color);
			}
			else
			{
				ChartBean bean = new ChartBean();
				bean.setLineColor(color);
				map.put(lineName, bean);
			}
		}

	}

	/**
	 * 设置某条轴各曲线的点的形状
	 * 
	 * @param lineName
	 *            要设置的线的名称
	 * @param shape
	 *            形状
	 */
	public void setLineShape(String axisName, String lineName, Shape shape)
	{
		if (lineName == null)
			return;
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (!map.containsKey(lineName))
			{
				ChartBean bean = new ChartBean();
				bean.setShape(shape);
				map.put(lineName, bean);
			}
			else
			{
				ChartBean bean = map.get(lineName);
				bean.setShape(shape);
			}
		}

	}

	/**
	 * 设置某条轴各曲线的宽度
	 * 
	 * @param lineName
	 *            要设置的线的名称
	 * @param width
	 *            宽度
	 */
	public void setLineWidth(String axisName, String lineName, float width)
	{
		if (lineName == null)
			return;
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (!map.containsKey(lineName))
			{
				ChartBean bean = new ChartBean();
				bean.setLineWidth(width);
			}
			else
			{
				ChartBean bean = map.get(lineName);
				bean.setLineWidth(width);
			}
		}

	}

	/**
	 * 设置某条轴各曲线的点的填充颜色
	 * 
	 * @param lineName
	 *            要设置的线的名称
	 * @param color
	 *            颜色
	 */
	public void setLineShapeFilledColor(String axisName, String lineName, Color color)
	{
		if (lineName == null)
			return;
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		if (axis_data_map.get(axisName) != null)
		{
			Map<String, ChartBean> map = axis_data_map.get(axisName);
			if (!map.containsKey(lineName))
			{
				ChartBean bean = new ChartBean();
				bean.setShapeFilled(true);
				bean.setShapeColor(color);
				map.put(lineName, bean);
			}
			else
			{
				ChartBean bean = map.get(lineName);
				bean.setShapeFilled(true);
				bean.setShapeColor(color);
			}
		}

	}

	/**
	 * 设置某条Y轴的间隔
	 * 
	 * <pre>
	 * 如果想设置左Y轴的两个刻度之间间隔为10 则setYUnit(MulAxisLineChart.AXIS_LEFT,10)
	 * </pre>
	 * 
	 * @param unit
	 */
	public void setYUnit(String axisName, double unit)
	{
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		NumberTickUnit tickUnit = new NumberTickUnit(unit);
		axisMap.get(axisName).setAutoTickUnitSelection(false);
		axisMap.get(axisName).setTickUnit(tickUnit);
	}

	/**
	 * 设置X轴的范围
	 * 
	 * <pre>
	 * 如果想设置X轴的范围为0-30 则setXRange(0,30)
	 * </pre>
	 */
	public void setXRange(double lower, double upper)
	{
		if (domainAxis == null)
			domainAxis = new NumberAxis();
		domainAxis.setRange(lower, upper);
	}

	/**
	 * 设置Y轴的范围
	 * 
	 * <pre>
	 * 如果想设置左Y轴的范围为0-30 则setYRangeLeft(MulAxisLineChart.AXIS_LEFT,0,30)
	 * </pre>
	 */
	public void setYRange(String axisName, double lower, double upper)
	{
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		axisMap.get(axisName).setRange(lower, upper);
	}

	/**
	 * 设置X轴的间隔
	 * 
	 * <pre>
	 * 如果想设置X轴的两个刻度之间间隔为10 则setXUnit(10)
	 * </pre>
	 * 
	 * @param unit
	 */
	public void setXUnit(double unit)
	{
		if (domainAxis == null)
		{
			domainAxis = new NumberAxis();
		}
		NumberTickUnit tickUnit = new NumberTickUnit(unit);
		domainAxis.setAutoTickUnitSelection(false);
		domainAxis.setTickUnit(tickUnit);
	}

	/**
	 * 设置X轴是否可见
	 * 
	 * @param flag
	 */
	public void setXVisible(boolean flag)
	{
		if (domainAxis == null)
		{
			domainAxis = new NumberAxis();
		}
		domainAxis.setVisible(flag);
	}

	/**
	 * 设置某条Y轴是否可见
	 * 
	 * @param flag
	 */
	public void setYVisible(String axisName, boolean flag)
	{
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		axisMap.get(axisName).setVisible(flag);
	}

	/**
	 * 设置X轴是否从零开始
	 * 
	 * @param flag
	 *            true:是 false:否
	 */
	public void setXStartWithZero(boolean flag)
	{
		if (domainAxis == null)
		{
			domainAxis = new NumberAxis();
		}
		domainAxis.setAutoRangeIncludesZero(flag);
	}

	/**
	 * 设置某条Y轴是否从零开始
	 * 
	 * @param flag
	 *            true:是 false:否
	 */
	public void setYStartWithZero(String axisName, boolean flag)
	{
		if (axisName == null || axisMap.get(axisName) == null)
			return;
		axisMap.get(axisName).setAutoRangeIncludesZero(flag);
	}

	/**
	 * 设置图片的背景字
	 * 
	 * @param value
	 *            要显示的字
	 * @param x
	 *            要显示的字在整个图片中的横坐标
	 * @param y
	 *            要显示的字在整个图片中的纵坐标
	 */
	public void addBackgroundValue(String value, double x, double y)
	{
		Font font = new Font("隶书", Font.PLAIN, 15);
		this.addBackgroundValue(value, x, y, font);
	}

	/**
	 * 设置图片的背景字
	 * 
	 * @param value
	 *            要显示的字
	 * @param x
	 *            要显示的字在整个图片中的横坐标
	 * @param y
	 *            要显示的字在整个图片中的纵坐标
	 * @param font
	 *            要显示字的样式
	 */
	public void addBackgroundValue(String value, double x, double y, Font font)
	{
		if (value == null)
			value = new String("");
		if (font == null)
		{
			font = new Font("隶书", Font.PLAIN, 15);
		}
		addBackgroundValue(value, x, y, font, null);
	}

	public void addBackgroundValue(String value, double x, double y, Font font, Color color)
	{
		if (value == null)
			value = new String("");
		XYTextAnnotation anno = new XYTextAnnotation(value, x, y);
		if (font == null)
		{
			font = new Font("隶书", Font.PLAIN, 15);
		}
		if (color == null)
			color = Color.black;
		anno.setFont(font);
		anno.setTextAnchor(TextAnchor.BASELINE_CENTER);
		anno.setPaint(color);
		annoList.add(anno);
	}

	/**
	 * 下面两个方法是构造左右轴数据集 构造完成后调动左右轴各条曲线初始化方法
	 * 
	 * @return
	 */
	private XYSeriesCollection createDataset(String axisName)
	{
		if (axisName == null)
			return null;
		XYSeriesCollection collection = new XYSeriesCollection();
		if (axis_data_map.get(axisName) != null && !axis_data_map.get(axisName).isEmpty())
		{
			if (axis_data_map.get(axisName) == null)
				return null;
			Integer index = axis_series_index_map.get(axisName);
			for (ChartBean bean : axis_data_map.get(axisName).values())
			{
				if (bean.getSeries() == null)
					continue;
				collection.addSeries(bean.getSeries());
				if (index != null)
				{
					bean.setIndex(index);
					index++;
				}
			}

			initSeries(axisName);
		}
		return collection;
	}

	/**
	 * 下面几个方法是设置左右轴的初始数据 要在构造完数据集过后 知道每条曲线的索引才可以设置
	 */
	private void initSeries(String axisName)
	{
		if (axisName == null)
			return;
		if (axis_data_map.get(axisName) != null && !axis_data_map.get(axisName).isEmpty())
		{
			XYLineAndShapeRenderer renderer = axit_render_map.get(axisName);

			for (ChartBean bean : axis_data_map.get(axisName).values())
			{
				renderer.setSeriesStroke(bean.getIndex(), bean.getLineStroke()); // 线的笔触
				renderer.setUseFillPaint(true);
				if (bean.getLineColor() != null) // 线的颜色
				{
					renderer.setSeriesPaint(bean.getIndex(), bean.getLineColor());
				}
				if (bean.getShapeVisible() != null) // 点是否可见
				{
					renderer.setSeriesShapesVisible(bean.getIndex(), bean.getShapeVisible());
				}
				if (bean.getLineVisible() != null)
				{
					renderer.setSeriesLinesVisible(bean.getIndex(), bean.getLineVisible()); // 线是否可见
				}
				if (bean.getShape() != null)
				{
					renderer.setSeriesShape(bean.getIndex(), bean.getShape()); // 设置点的形状
				}
				if (bean.getShapeFilled() && bean.getShapeColor() != null) // 设置对点的具体填充色
				{
					renderer.setSeriesFillPaint(bean.getIndex(), bean.getShapeColor());
				}
				else if (!bean.getShapeFilled() && renderer.getUseFillPaint()) // 没有设置对点的具体填充色，则默认用线的颜色填充
				{
					renderer.setSeriesFillPaint(bean.getIndex(), bean.getLineColor());
				}
			}

		}

	}

	@Override
	protected void addTitle(JFreeChart chart)
	{
		if (textTitleList != null)
		{
			for (TextTitle tt : textTitleList)
			{
				chart.addSubtitle(tt);
			}
		}
	}

	@Override
	protected void setBgImg(Plot plot)
	{
		if (bgImg != null)
			plot.setBackgroundImage(bgImg);
	}

}
