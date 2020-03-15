package cn.myframe.chart;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.TextTitle;
public abstract class BaseChart 
{
	//背景图片
	protected Image bgImg;
	//子标题列表
	protected List<TextTitle> textTitleList;
	
	/**
	 * 添加子标题, 要在继承类中实现这个方法并且在画图之前调用它<br>
	 * 会用到子标题列表 textTitleList
	 * @param chart
	 */
	protected abstract void addTitle(JFreeChart chart);
	
	/**
	 * 设置背景图片, 要在继承类中实现这个方法并且在画图之前调用它<br>
	 * 会用到背景图片对象bgImg
	 * @param plot
	 */
	protected abstract void setBgImg(Plot plot);
	
	/**
	 * 设置曲线图的背景图片
	 * <pre>setBackgroundImg("images/bg.gif")</pre>
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public void setBackgroundImg(HttpServletRequest request,String fileName) throws Exception
	{
		ImageIcon icon = new ImageIcon(request.getSession().getServletContext().getRealPath("/images"+fileName));
		bgImg = icon.getImage();
	}
	
	
	/**
	 * 添加子标题
	 * @param title
	 */
	public void addSubtitle(String title)
	{
		Font font = new Font("隶书", Font.PLAIN, 15);
		this.addSubtitle(title, font);
	}
	
	/**
	 * 添加子标题并设置标题为指定字体
	 * @param title
	 * @param font
	 */
	public void addSubtitle(String title, Font font)
	{
		if(title == null )
			return;
		if(textTitleList == null)
			textTitleList = new ArrayList<TextTitle>();
		if(font == null)
			font = new Font("隶书", Font.PLAIN, 15);
		TextTitle tt = new TextTitle(title, font);
		textTitleList.add(tt);
	}
	
	
}
