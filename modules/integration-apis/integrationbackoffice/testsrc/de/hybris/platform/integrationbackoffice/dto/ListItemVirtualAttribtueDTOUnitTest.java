package de.hybris.platform.integrationbackoffice.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ListItemVirtualAttribtueDTOUnitTest
{
	@Test
	public void testListItemVirtualAttributeDTOConstructor()
	{
		TypeModel typeModel = new TypeModel();
		typeModel.setCode("Product");

		IntegrationObjectVirtualAttributeDescriptorModel retrievalDescriptor = new IntegrationObjectVirtualAttributeDescriptorModel();
		retrievalDescriptor.setCode("Test retrieval");
		retrievalDescriptor.setLogicLocation("testpath");
		retrievalDescriptor.setType(typeModel);

		AbstractListItemDTO actual = new ListItemVirtualAttributeDTO(true, false, false,
				retrievalDescriptor, "");

		assertEquals(typeModel.getCode(), actual.getDescription());
	}

	@Test
	public void testFindMatchFound()
	{
		TypeModel typeModel = new TypeModel();
		typeModel.setCode("Product");
		IntegrationObjectVirtualAttributeDescriptorModel retrievalModel = new IntegrationObjectVirtualAttributeDescriptorModel();
		retrievalModel.setType(typeModel);

		IntegrationObjectVirtualAttributeDescriptorModel retrievalModel2 = new IntegrationObjectVirtualAttributeDescriptorModel();
		retrievalModel2.setType(typeModel);

		ComposedTypeModel compType1 = new ComposedTypeModel();

		ListItemVirtualAttributeDTO dtoToMatch = new ListItemVirtualAttributeDTO(true, false, false, retrievalModel, "product");
		ListItemVirtualAttributeDTO dto2 = new ListItemVirtualAttributeDTO(true, false, false, retrievalModel, "product2");

		Map<ComposedTypeModel, List<AbstractListItemDTO>> testMap = new HashMap<>();
		List<AbstractListItemDTO> dtos = new ArrayList<>();
		dtos.add(dtoToMatch);
		dtos.add(dto2);
		testMap.put(compType1, dtos);

		AbstractListItemDTO result = dtoToMatch.findMatch(testMap, compType1);

		assertEquals(dtoToMatch, result);
	}

	@Test
	public void testFindMatchNotFound() throws NoSuchElementException
	{
		TypeModel typeModel = new TypeModel();
		typeModel.setCode("Product");
		IntegrationObjectVirtualAttributeDescriptorModel retrievalModel = new IntegrationObjectVirtualAttributeDescriptorModel();
		retrievalModel.setType(typeModel);
		ComposedTypeModel compType1 = new ComposedTypeModel();

		IntegrationObjectVirtualAttributeDescriptorModel retrievalModel2 = new IntegrationObjectVirtualAttributeDescriptorModel();
		retrievalModel2.setType(typeModel);

		ListItemVirtualAttributeDTO dtoToMatch = new ListItemVirtualAttributeDTO(true, false, false, retrievalModel, "product");
		ListItemVirtualAttributeDTO dto2 = new ListItemVirtualAttributeDTO(true, false, false, retrievalModel2, "product2");

		Map<ComposedTypeModel, List<AbstractListItemDTO>> testMap = new HashMap<>();
		List<AbstractListItemDTO> dtos = new ArrayList<>();
		dtos.add(dto2);
		dtos.add(dto2);
		testMap.put(compType1, dtos);

		assertThatThrownBy(() -> dtoToMatch.findMatch(testMap, compType1))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("No matching VirtualAttribute was found.");
	}
}
