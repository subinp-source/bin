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

import org.zkoss.zul.ListModelList;


public class TreeListModel<T extends RuleItemModel> extends ListModelList<TreeNodeModel<T>>
{
	private static final long serialVersionUID = 1L;

	private TreeNodeModel<T> treeNode;

	public TreeNodeModel<T> getTreeNode()
	{
		return treeNode;
	}

	public void setTreeNode(final TreeNodeModel<T> treeNode)
	{
		this.treeNode = treeNode;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		if (!super.equals(o))
		{
			return false;
		}

		final TreeListModel<?> that = (TreeListModel<?>) o;
		return treeNode != null ? treeNode.equals(that.treeNode) : that.treeNode == null;
	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (treeNode != null ? treeNode.hashCode() : 0);
		return result;
	}
}
