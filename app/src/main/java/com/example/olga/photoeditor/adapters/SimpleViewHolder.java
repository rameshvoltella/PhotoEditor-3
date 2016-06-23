package com.example.olga.photoeditor.adapters;

import android.view.View;

/**
 * Date: 20-Oct-15
 * Time: 11:24
 *
 * @author esorokin
 */
public abstract class SimpleViewHolder<ModelType>
{
	public View rootView;

	protected SimpleViewHolder(View rootView)
	{
		this.rootView = rootView;
		create(rootView);
	}

	final public View getRoot()
	{
		return rootView;
	}

	abstract protected void create(View rootView);

	abstract public void bind(ModelType model);
}