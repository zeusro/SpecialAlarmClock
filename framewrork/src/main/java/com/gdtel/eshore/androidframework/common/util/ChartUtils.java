package com.gdtel.eshore.androidframework.common.util;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
/**
 * 
 * 图形绘制工具类
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2014-11-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ChartUtils {
	// ------------------------折线图-----------------------
	/**
	 * 创建折线图上显示的数据
	 * 
	 * @param x
	 *            X轴上的相关坐标,每一个数组代表一条折线的X轴坐标
	 * @param y
	 *            Y轴上的相关坐标,每一个数组代表一条折线的Y轴坐标
	 * @return
	 */
	public static XYMultipleSeriesDataset buildLineDataset(List<double[]> x,
			List<double[]> y) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		final int nr = x.get(0).length;
		for (int i = 0; i < x.size(); i++) {
			// XYSeries series = new XYSeries("说明");
			XYSeries series = new XYSeries("");
			for (int k = 0; k < nr; k++) {
				series.add(x.get(i)[k], y.get(i)[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	/**
	 * 设置主标题
	 * 
	 * @param renderer
	 * @param chartTitle
	 *            标题
	 */
	public static void setChartTitle(XYMultipleSeriesRenderer renderer,
			String chartTitle) {
		if (chartTitle != null && !"".equals(chartTitle))
			renderer.setChartTitle(chartTitle);
	}

	/**
	 * 设置XY轴上的标题
	 * 
	 * @param renderer
	 * @param xTitle
	 * @param yTitle
	 */
	public static void setChartXYTitle(XYMultipleSeriesRenderer renderer,
			String xTitle, String yTitle) {
		if (xTitle != null && !"".equals(xTitle))
			renderer.setXTitle(xTitle);
		if (yTitle != null && !"".equals(yTitle))
			renderer.setYTitle(yTitle);
	}

	/**
	 * 设置XY轴上的坐标显示范围
	 * 
	 * @param renderer
	 * @param xMin
	 *            X轴上的最小显示范围
	 * @param xMax
	 *            X轴上的最大显示范围
	 * @param yMin
	 *            Y轴上的最小显示范围
	 * @param yMax
	 *            Y轴上的最大显示范围
	 */
	public static void setXYAxisMM(XYMultipleSeriesRenderer renderer,
			double xMin, double xMax, double yMin, double yMax) {
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
	}

	public static GraphicalView createLineChartView(Context ctx, int colors[],
			List<double[]> x, List<double[]> y) {

		return createLineChartView(ctx, colors, null, x, y);

	}

	/**
	 * 生成折线图
	 * 
	 * @param ctx
	 * @param colors
	 *            线条颜色
	 * @param labels
	 *            x轴上显示的文字
	 * @param x
	 * @param y
	 * @return
	 */
	public static GraphicalView createLineChartView(Context ctx, int colors[],
			ArrayList<String> labels, List<double[]> x, List<double[]> y) {
		GraphicalView mChartView = null;
		XYMultipleSeriesRenderer mrenderer;
		mrenderer = ChartUtils.getDefaultRendererAnalysis(ctx);
		ChartUtils.addSRendererAnalysis(ctx, mrenderer, colors);
		if (null != labels) {
			ChartUtils.addTextLabels(mrenderer, labels);
		} else {
			mrenderer.setXLabels(x.get(0).length);
		}

		// 获得Y轴上的值的最大值
		double maxY = 0;
		for (int i = 0; i < y.size(); i++) {
			for (int j = 0; j < y.get(i).length; j++) {
				maxY = maxY > y.get(i)[j] ? maxY : y.get(i)[j];
			}
		}
		ChartUtils.setXYAxisMM(mrenderer, 0, x.get(0).length - 1, 0, maxY + 5);
		mChartView = ChartFactory.getLineChartView(ctx,
				ChartUtils.buildLineDataset(x, y), mrenderer);

		return mChartView;
	}

	/**
	 * 获取默认设置的渲染器
	 * 
	 * @return
	 */
	public static XYMultipleSeriesRenderer getDefaultRendererAnalysis(
			Context ctx) {
		int margins = 50;// 外边距宽度
		int point_size = 10;// 标记点大小
		float chart_text_size = 20f;// 文本大小

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setXLabels(0);
		renderer.setYLabels(15);// 控制Y轴上的标签的密集度
		renderer.setApplyBackgroundColor(false);// 是否启用背景颜色
		// renderer.setBackgroundColor(Color.rgb(99, 174, 90));// 设置xy坐标轴内的颜色
		// renderer.setMarginsColor(Color.WHITE);// 设置外边距的颜色
		// renderer.setAxisTitleTextSize(16);// 设置轴标题的文本大小
		// renderer.setChartTitleTextSize(20);// 设置图表的标题大小
		renderer.setChartTitleTextSize(chart_text_size * 3 / 2);
		renderer.setLabelsTextSize(chart_text_size);// 设置标签的字体大小，例如坐标轴上的文字
		// renderer.setXLabelsColor(Color.YELLOW);// 设置x坐标轴上的文字颜色,用于单独设置x轴上文字颜色
		// renderer.setYLabelsColor(0, Color.MAGENTA);//
		// 设置y坐标轴上的文字颜色,用于单独设置y轴上文字颜色
		renderer.setLegendTextSize(15);// 设置说明文字的大小，例如坐标轴下面的文字
		renderer.setPointSize(point_size);// 设置标记点的大小
		renderer.setMargins(new int[] { margins, margins, margins / 3, margins });// 设置外边距上左下右,通过这个可以设置x轴与底部说明文字的间隔
		renderer.setLabelsColor(Color.WHITE);// 设置标签的颜色
		// renderer.setAxesColor(Color.rgb(99, 174, 90));// 设置轴线的颜色
		renderer.setShowGridX(true); // x轴的刻度线
		renderer.setShowGridY(false); // y轴的刻度线
		renderer.setShowLegend(false); // 是否显示系列说明,底部的说明文字前的图形跟标记点是一致的
		renderer.setGridColor(0xff629C4D);// 刻度线颜色
		renderer.setMarginsColor(Color.argb(0, 99, 174, 90));// 设置外边距颜色,如果不设置，外部是黑色
		renderer.setPanEnabled(false, false);//设置是否可以向水平方向或垂直方向移动
		renderer.setZoomEnabled(false, false);//设置是否可以缩放
		return renderer;
	}

	/**
	 * 添加x轴显示的内容
	 * 
	 * @param renderer
	 * @param labels
	 */
	public static void addTextLabels(XYMultipleSeriesRenderer renderer,
			ArrayList<String> labels) {
		renderer.setXLabels(0);
		for (int i = 0; i < labels.size(); i++)
			renderer.addXTextLabel(i, labels.get(i));
	}

	/**
	 * 添加折线图线条样式
	 * 
	 * @param renderer
	 * @param color
	 *            颜色
	 * @param style
	 *            风格(正方、菱形......)
	 */
	public static void addSRendererAnalysis(Context ctx,
			XYMultipleSeriesRenderer renderer, int color[]) {
		float line_size = 5f;// 线宽
		float text_size = 20f;// 折线上的文本大小
		float spacing = 20f;// 文本与标记点间隔
		PointStyle style = PointStyle.CIRCLE;// 标记点样式
		for (int i = 0; i < color.length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();// XY坐标序列的渲染器
			r.setColor(color[i]);// 设置填充颜色
			r.setPointStyle(style);// 设置标记点为圆形
			r.setFillPoints(true);// 设置是否填充标记点
			r.setLineWidth(line_size);// 设置折线线宽
			r.setDisplayChartValues(true);// 设置是否显示折线上的文本
			r.setChartValuesTextSize(text_size);// 设置折线上的文本大小
			r.setChartValuesSpacing(spacing);// 设置折线上的文本与标记点之间的间隔
			renderer.addSeriesRenderer(r);
		}
	}

	// ---------------------饼图-----------------------
	/**
	 * 生成饼图
	 * 
	 * @param ctx
	 * @param chartTitle
	 *            标题
	 * @param labels
	 *            块描述
	 * @param values
	 *            块值
	 * @param colors
	 *            块颜色
	 * @return
	 */
	public static GraphicalView createPieChartView(Context ctx,
			String chartTitle, String labels[], double[] values, int colors[]) {
		DefaultRenderer mRenderer = new DefaultRenderer();
		// mRenderer.setZoomButtonsVisible(true);//是否显示放大缩小控件
		mRenderer.setStartAngle(180);
		mRenderer.setDisplayValues(true);// 是否显示饼块上的值
		mRenderer.setLabelsColor(Color.RED);// 设置标签的颜色
		mRenderer.setLabelsTextSize(20f);// 设置标签的字体大小
		mRenderer.setShowLabels(true);// 是否显示标签
		mRenderer.setShowLegend(true);// 是否显示下面的说明文字
		mRenderer.setFitLegend(false);// 貌似是设置是否把说明文字显示在最下面
		mRenderer.setApplyBackgroundColor(true);// 是否使用背景色
		// mRenderer.setBackgroundColor(Color.CYAN);//设置背景色颜色
		mRenderer.setLegendTextSize(30f);// 设置说明文字字体大小
		mRenderer.setChartTitle(chartTitle);// 设置标题
		mRenderer.setChartTitleTextSize(30f);// 设置标题文本大小

		CategorySeries mSeries = new CategorySeries("");
		for (int i = 0; i < values.length; i++) {
			mSeries.add(labels[mSeries.getItemCount()], values[i]);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(colors[i]);
			mRenderer.addSeriesRenderer(renderer);
		}

		mRenderer.getSeriesRendererAt(0).setHighlighted(true);// 设置哪一块被选中

		return ChartFactory.getPieChartView(ctx, mSeries, mRenderer);
	}

	// ------------------柱状图-------------------
	/**
	 * 生成柱状图
	 * 
	 * @param ctx
	 * @param chartTitle
	 *            标题
	 * @param colors
	 *            柱形颜色
	 * @param legendTitle
	 *            柱形说明
	 * @param value
	 *            柱形值
	 * @return
	 */
	public static GraphicalView createBarChartView(Context ctx,
			String chartTitle, int[] colors, String[] legendTitle,
			List<double[]> value) {
		XYMultipleSeriesRenderer renderer = getBarRenderer(colors,
				value.get(0).length);
		setChartTitle(renderer, chartTitle);
		setBarChartSettings(renderer, value);
		return ChartFactory.getBarChartView(ctx,
				buildBarDataset(legendTitle, value), renderer, Type.DEFAULT);

	}

	/**
	 * 设置XY坐标轴的起始和结束位置
	 * 
	 * @param renderer
	 * @param value
	 *            柱形值
	 */
	private static void setBarChartSettings(XYMultipleSeriesRenderer renderer,
			List<double[]> value) {
		double max = 0;
		// renderer.setXTitle("x values");// 设置X轴的标题
		// renderer.setYTitle("y values");// 设置Y轴的标题
		// 选出最大值
		for (int i = 0; i < value.size(); i++) {
			for (int j = 0; j < value.get(i).length; j++) {
				max = max - value.get(i)[j] > 0 ? max : value.get(i)[j];
			}
		}
		// 设置X轴的开始位置,0.5最佳
		setXYAxisMM(renderer, 0.5, value.get(0).length + 0.5, 0, max / 4 * 5);
	}

	/**
	 * 配置柱形图的相关参数
	 * 
	 * @param colors
	 * @param length
	 * @return
	 */
	public static XYMultipleSeriesRenderer getBarRenderer(int[] colors,
			int length) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// renderer.setXLabels(0);
		renderer.setXLabels(length);
		renderer.setApplyBackgroundColor(false);// 是否使用背景色
		renderer.setMarginsColor(Color.argb(0, 99, 174, 90));
		renderer.setAxisTitleTextSize(20);// 设置轴标题字体大小
		renderer.setChartTitleTextSize(30);// 设置标题字体大小
		renderer.setLabelsTextSize(20);// 设置坐标轴上的文字大小
		renderer.setLegendTextSize(20);// 设置底部说明文字大小
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		renderer.setLabelsColor(Color.CYAN);// 设置标题颜色
		renderer.setXLabelsAlign(Align.LEFT);// 设置X轴文字字体对齐方式
		renderer.setYLabelsAlign(Align.LEFT);// 设置X轴文字字体对齐方式
		renderer.setPanEnabled(true, false);// 设置是否可以向X轴、Y轴方向移动
		// renderer.setZoomEnabled(false);
		renderer.setZoomRate(1.1f);// 设置缩放比率
		renderer.setBarSpacing(0.5f);// 设置柱形间距
		for (int i = 0; i < colors.length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
//			r.setDisplayChartValues(true);// 是否显示柱形上的值
//			r.setChartValuesTextSize(15f);// 设置柱形上的值的文本大小
//			r.setChartValuesTextAlign(Align.RIGHT);// 设置柱形上的值的对齐方式
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	private static XYMultipleSeriesDataset buildBarDataset(
			String[] legendTitle, List<double[]> value) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < value.size(); i++) {
			CategorySeries series = new CategorySeries(legendTitle[i]);
			for (int k = 0; k < value.get(i).length; k++) {
				series.add(value.get(i)[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	/**
	 * 根据手机的分辨率从dp 的单位转成px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
