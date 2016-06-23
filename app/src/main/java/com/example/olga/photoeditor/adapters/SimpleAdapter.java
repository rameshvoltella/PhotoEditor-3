package com.example.olga.photoeditor.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

/**
 * Date: 13.10.2015
 * Time: 14:56
 *
 * @author esorokin
 */
public abstract class SimpleAdapter<TypeModel> extends CollectionAdapter<TypeModel>
{
	public SimpleAdapter(Context context)
	{
		super(context);
	}

	public SimpleAdapter(Context context, Collection<? extends TypeModel> collection)
	{
		super(context, collection);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SimpleViewHolder<TypeModel> vh;
		if (convertView == null)
		{
			vh = createViewHolder(parent, getItemViewType(position));
			vh.rootView.setTag(vh);
		}
		else
		{
			//noinspection unchecked
			vh = (SimpleViewHolder) convertView.getTag();
		}

		vh.bind(getItem(position));
		return vh.rootView;
	}

	protected abstract SimpleViewHolder<TypeModel> createViewHolder(ViewGroup parent, int viewType);
}
