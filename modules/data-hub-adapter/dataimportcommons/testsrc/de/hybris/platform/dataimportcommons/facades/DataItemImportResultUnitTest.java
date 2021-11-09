/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.dataimportcommons.facades;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

/**
 * A unit test for <code>ItemImportResult</code>
 */
@UnitTest
public class DataItemImportResultUnitTest
{
	private DataItemImportResult<DataImportError> importResult;

	@Before
	public void setUp()
	{
		importResult = new DataItemImportResult<>();
	}

	@Test
	public void tesResultIsSuccessfulWhenNoErrorsReported()
	{
		assertThat(importResult.isSuccess()).isTrue();
	}

	@Test
	public void testDoesNotContainImportErrorsBeforeTheyAdded()
	{
		final Collection<DataImportError> rejected = importResult.getExportErrorDatas();
		assertThat(rejected).isNotNull()
		                    .isEmpty();
	}

	@Test
	public void testAllAddedErrorsCanBeReadBack()
	{
		final DataImportError err1 = DataImportTestUtils.error("Missing attribute");
		final DataImportError err2 = DataImportTestUtils.error("Unresolved attribute");
		importResult.addErrors(Arrays.asList(err1, err2));

		final Collection<DataImportError> errors = importResult.getExportErrorDatas();

		assertThat(errors).hasSize(2)
		                  .contains(err1, err2);
	}

	@Test
	public void testSameResultIsReturnedAfterAddingAnErrorToIt()
	{
		final DataItemImportResult<DataImportError> orig = new DataItemImportResult<>();
		final DataItemImportResult<DataImportError> returned = orig.addErrors(DataImportTestUtils.errors("some error"));

		assertThat(returned).isEqualTo(orig);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddErrorsDoesNotExpectNullBePassedForTheErrorCollection()
	{
		new DataItemImportResult<>().addErrors(null);
	}

	@Test
	public void testReportedExceptionCanBeReadBack()
	{
		final Exception ex = new Exception("Import exception has occurred");

		final DataItemImportResult<?> res = new DataItemImportResult<>(ex);
		assertThat(res.getImportExceptionMessage()).isEqualTo(ex.getMessage());
	}

	@Test
	public void testReportedExceptionWithoutMessageCanBeReadBack()
	{
		final Exception ex = new Exception();

		final DataItemImportResult<?> res = new DataItemImportResult<>(ex);
		assertThat(res.getImportExceptionMessage()).isEqualTo(ex.getClass().getCanonicalName());
	}

	@Test
	public void testReportedErrorMessageCanBeReadBack()
	{
		final String errorMessage = "Data import has failed";
		final DataItemImportResult<?> res = new DataItemImportResult<>(errorMessage);

		assertThat(res.getImportExceptionMessage()).isEqualTo(errorMessage);
	}

	@Test
	public void testResultIsNotSuccessfulWhenAtLeastOneErrorIsAdded()
	{
		importResult.addErrors(DataImportTestUtils.errors("some error"));

		assertThat(importResult.isSuccess()).isFalse();
	}

	@Test
	public void testResultIsNotSuccessfulWhenCreatedWithException()
	{
		final DataItemImportResult<DataImportError> result = new DataItemImportResult<>(new Exception());
		assertThat(result.isSuccess()).isFalse();
	}

	@Test
	public void testResultIsNotSuccessfulWhenCreatedWithErrorMessage()
	{
		final DataItemImportResult<DataImportError> result = new DataItemImportResult<>("Houston, we have a problem");
		assertThat(result.isSuccess()).isFalse();
	}

	@Test
	public void testAddingErrorClearsTheExceptionMessage()
	{
		final DataItemImportResult<DataImportError> result = new DataItemImportResult<>(new Exception("not empty message"));

		result.addErrors(DataImportTestUtils.errors("text does not matter"));

		assertThat(result.getImportExceptionMessage()).isNull();
	}

	@Test
	public void testAddingErrorClearsTheErrorMessage()
	{
		final DataItemImportResult<DataImportError> result = new DataItemImportResult<>("not empty message");

		result.addErrors(DataImportTestUtils.errors("text does not matter"));

		assertThat(result.getImportExceptionMessage()).isNull();
	}
}
