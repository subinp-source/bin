/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.rulebuilderbackoffice.editors;

import java.io.Serializable;


public class TreeNodeModel<T extends RuleItemModel> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private TreeNodeModel<T> parent;
	private Boolean allowsChildren;
	private TreeListModel<T> children;
	private Boolean expanded;
	private T data;

	public TreeNodeModel()
	{
		allowsChildren = Boolean.FALSE;
		expanded = Boolean.TRUE;
	}

	public TreeNodeModel<T> getParent()
	{
		return parent;
	}

	public void setParent(final TreeNodeModel<T> parent)
	{
		this.parent = parent;
	}

	public Boolean getAllowsChildren()
	{
		return allowsChildren;
	}

	public void setAllowsChildren(final Boolean allowsChildren)
	{
		this.allowsChildren = allowsChildren;
	}

	public TreeListModel<T> getChildren()
	{
		return children;
	}

	public void setChildren(final TreeListModel<T> children)
	{
		this.children = children;
	}

	public Boolean getExpanded()
	{
		return expanded;
	}

	public void setExpanded(final Boolean expanded)
	{
		this.expanded = expanded;
	}

	public T getData()
	{
		return data;
	}

	public void setData(final T data)
	{
		this.data = data;
	}

	public boolean addChild(final TreeNodeModel<T> child)
	{
		return getChildren().add(child);
	}

	public void addChild(final int index, final TreeNodeModel<T> child)
	{
		getChildren().add(index, child);
	}

	public boolean removeChild(final TreeNodeModel<T> child)
	{
		return getChildren().remove(child);
	}

	public int indexOfChild(final TreeNodeModel<T> child)
	{
		return getChildren().indexOf(child);
	}

	public boolean hasParent(final TreeNodeModel<T> node)
	{
		TreeNodeModel<T> parentNode = getParent();
		while (parentNode != null)
		{
			if (parentNode.equals(node))
			{
				return true;
			}

			parentNode = parentNode.getParent();
		}

		return false;
	}
}
