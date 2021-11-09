/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.builders;


import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.SubtypeData;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper method for EditorController
 * Extracted data structure specific methods to this class
 */
public interface DataStructureBuilder
{
	public Map<ComposedTypeModel, List<AbstractListItemDTO>> populateAttributesMap(final ComposedTypeModel typeModel,
	                                                                               final Map<ComposedTypeModel, List<AbstractListItemDTO>> currAttrMap);


	public Map<ComposedTypeModel, List<AbstractListItemDTO>> loadExistingDefinitions(final Map<ComposedTypeModel, List<AbstractListItemDTO>> existingDefinitions,
	                                                                                 final Map<ComposedTypeModel, List<AbstractListItemDTO>> currAttrMap);

	public Set<SubtypeData> compileSubtypeDataSet(final Map<ComposedTypeModel, List<AbstractListItemDTO>> existingDefinitions,
	                                              final Set<SubtypeData> subtypeDataSet);


	public ComposedTypeModel findSubtypeMatch(final ComposedTypeModel parentType, final String attributeQualifier,
	                                          final ComposedTypeModel attributeType,
	                                          final Set<SubtypeData> subtypeDataSet);

}
