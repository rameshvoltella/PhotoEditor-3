package com.example.olga.photoeditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date: 22-Nov-15
 * Time: 17:14
 *
 * @author esorokin
 */
@SuppressWarnings("unused")
public abstract class CollectionAdapter<T> extends BaseAdapter
{
	protected LayoutInflater mInflater;
	private Context mContext;
	private final List<T> mList;

	public CollectionAdapter(Context context)
	{
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mList = new ArrayList<>();
	}

	public CollectionAdapter(Context context, Collection<? extends T> collection)
	{
		this(context);
		
		if (collection != null)
		{
			mList.addAll(collection);
		}
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public T getItem(int position)
	{
		return mList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	protected String getString(int id)
	{
		return mContext.getString(id);
	}

	protected Context getContext()
	{
		return mContext;
	}

	public void setCollection(Collection<? extends T> collection)
	{
		mList.clear();

		if (collection != null)
		{
			mList.addAll(collection);
		}

		notifyDataSetChanged();
	}

	public void clearCollection()
	{
		if (mList != null)
		{
			mList.clear();
		}

		notifyDataSetChanged();
	}

	public void removeItem(T item)
	{
		if (mList != null)
		{
			mList.remove(item);
		}

		notifyDataSetChanged();
	}

	public void removeItem(int position)
	{
		if (mList != null)
		{
			mList.remove(position);
		}

		notifyDataSetChanged();
	}

	public void addItem(T item)
	{
		if (mList != null)
		{
			mList.add(item);
		}

		notifyDataSetChanged();
	}

	public List<T> getCollection()
	{
		return mList;
	}

}
