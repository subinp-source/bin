/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class TestJdbcLogUtils
{
	private final JDBCLogUtils jdbcLogUtils;

	private final Set<JDBCLogUtils.StatementsListener> listeners = new HashSet<>();

	private final TypeService typeService;

	public TestJdbcLogUtils(final JDBCLogUtils jdbcLogUtils, final TypeService typeService)
	{
		this.jdbcLogUtils = jdbcLogUtils;
		this.typeService = typeService;
	}

	public TestStatementListener addListener(final Set<String> includeTypes, final Set<String> excludeTypes)
	{
		final TestStatementListener testStatementListener = new TestStatementListener(
				toTableNamesSet(includeTypes), toTableNamesSet(excludeTypes));

		listeners.add(testStatementListener);
		jdbcLogUtils.addListener(testStatementListener);

		return testStatementListener;
	}

	private Set<String> toTableNamesSet(final Set<String> includeTypes)
	{
		return includeTypes.stream().map(t -> typeService.getComposedTypeForCode(t).getTable()).collect(Collectors.toSet());
	}

	public void reset()
	{
		listeners.forEach(jdbcLogUtils::removeListener);
		listeners.clear();
	}


	public static class TestStatementListener implements JDBCLogUtils.StatementsListener
	{
		private final String[] includeTables;
		private final String[] excludeTables;
		private final List<String> loggedStatements = new ArrayList<>();
		private final List<String> excludedStatements = new ArrayList<>();

		public TestStatementListener(final Set<String> includeTables, final Set<String> excludeTables)
		{
			this.includeTables = includeTables.stream()
			                                  .map(StringUtils::lowerCase)
			                                  .collect(Collectors.toSet())
			                                  .toArray(new String[]{});
			this.excludeTables = excludeTables.stream()
			                                  .map(StringUtils::lowerCase)
			                                  .collect(Collectors.toSet())
			                                  .toArray(new String[]{});
		}

		public void statementLogged(final String statement)
		{
			loggedStatements.add(statement);
		}


		@Override
		public void statementExecuted(final String statement)
		{
			if (included(statement))
			{
				if (excluded(statement))
				{
					statementExcluded(statement);
				}
				else
				{
					statementLogged(statement);
				}
			}
		}

		private void statementExcluded(final String statement)
		{
			excludedStatements.add(statement);
		}

		private boolean included(final String sql)
		{
			return ArrayUtils.isEmpty(includeTables) || foundTable(sql, includeTables);
		}

		private boolean excluded(final String sql)
		{
			return ArrayUtils.isNotEmpty(excludeTables) && foundTable(sql, excludeTables);
		}

		private boolean foundTable(final String sql, final String[] tables)
		{
			return StringUtils.containsAny(sql.toLowerCase(), tables);
		}

		public List<String> getLoggedStatements()
		{
			return loggedStatements;
		}

		public List<String> getExcludedStatements()
		{
			return excludedStatements;
		}
	}

}
