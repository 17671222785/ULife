package com.andywang.ulife.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;

/**
 * 万能的Adapter
 *
 * @param <T> 需要显示的数据的类型
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter{
	
	/**
	 * Context对象
	 */
	private Context context;
	
	/**
	 * 数据源
	 */
	private List<T> data;
	
	/**
	 * LayoutInflater，加载XML布局的工具
	 */
	private LayoutInflater layoutInflater;

	public BaseAdapter(Context context, List<T> data) {
		super();
		setContext(context);
		setData(data);
		layoutInflater = LayoutInflater.from(context);
	}

	/**
	 * 获取LayoutInflater对象
	 * 
	 * @return LayoutInflater对象
	 */
	protected final LayoutInflater getLayoutInflater() {
		return layoutInflater;
	}

	/**
	 * 获取Context对象
	 * 
	 * @return Context对象
	 */
	protected final Context getContext() {
		return context;
	}

	/**
	 * 设置Context对象
	 * 
	 * @param context
	 *            Context对象
	 */
	protected final void setContext(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("参数Context不允许为null！");
		}
		this.context = context;
	}

	/**
	 * 获取数据源
	 * 
	 * @return 数据源
	 */
	protected final List<T> getData() {
		return data;
	}

	/**
	 * 设置数据源，该方法将保证数据源不会为null值
	 * 
	 * @param data
	 *            数据源
	 */
	protected final void setData(List<T> data) {
		if (data == null) {
			data = new ArrayList<T>();
		}
		this.data = data;
	}

	@Override
	public int getCount() {
		// 返回数据的长度
		return data.size();
	}

	// ----------------------------
	// 以下方法暂不实现，子类可以不重写，也可以重写
	// ----------------------------

	@Override
	public Object getItem(int position) {
		// 暂不实现，子类可以不重写，也可以重写
		return null;
	}

	@Override
	public long getItemId(int position) {
		// 暂不实现，子类可以不重写，也可以重写
		return 0;
	}
	
}
