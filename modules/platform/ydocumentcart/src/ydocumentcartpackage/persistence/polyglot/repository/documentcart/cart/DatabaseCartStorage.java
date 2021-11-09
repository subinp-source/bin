/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.cart;

import de.hybris.platform.persistence.polyglot.PolyglotPersistence.CoreAttributes;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.search.criteria.AbstractToStringVisitor;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions.ComparisonCondition;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.jdbc.DatabaseNameResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.DocumentConcurrentModificationException;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.EntityCondition;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.serializer.Serializer;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.BaseStorage;


public class DatabaseCartStorage extends BaseStorage
{
	public static final String PROPERTY_SHOULD_THROW_EX_ON_FULL_TBL_SCAN = "ydocumentcart.storage.databaseCartStorage.throwExceptionOnFullScan";
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseCartStorage.class);
	private static final Set<String> SUPPORTED_DBS = Set.of(Config.DatabaseNames.MYSQL, Config.DatabaseNames.HSQLDB,
			Config.DatabaseNames.SQLSERVER);

	private static final int SQL_SERVER_FETCH_SIZE = 10;

	private final NamedParameterJdbcTemplate jdbc;
	private final Serializer serializer;
	private final String databaseName;
	private final ErrorHandler errorHandler;
	private String tableName = "documentcarts";
	private boolean useTenantAwareTableName = true;
	private DbInfo dbInfo;

	public DatabaseCartStorage(final DataSource dataSource, final Serializer serializer)
	{
		this.serializer = Objects.requireNonNull(serializer, "serializer mustn't be null");
		Objects.requireNonNull(dataSource, "dataSource mustn't be null");

		databaseName = DatabaseNameResolver.guessDatabaseNameFromURL(
				Config.getString("ydocumentcart.storage.jdbc.url", ""));

		if (!SUPPORTED_DBS.contains(databaseName))
		{
			throw new IllegalArgumentException(databaseName + " database is not supported");
		}

		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		if (!Config.DatabaseNames.HSQLDB.equals(databaseName))
		{
			jdbcTemplate.setFetchSize(Integer.MIN_VALUE);
		}
		this.jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);

		final boolean throwExceptionsOnFullTableScan = Config.getBoolean(PROPERTY_SHOULD_THROW_EX_ON_FULL_TBL_SCAN, false);

		errorHandler = new ErrorHandler(throwExceptionsOnFullTableScan);
	}

	public void setTableName(final String tableName)
	{
		this.tableName = tableName;
	}

	public void setUseTenantAwareTableName(final boolean useTenantAwareTableName)
	{
		this.useTenantAwareTableName = useTenantAwareTableName;
	}

	@PostConstruct
	public void initialize()
	{
		final String tenantPrefix = useTenantAwareTableName ? Config.getString("db.tableprefix", "") : "";
		final String finalTableName = tenantPrefix + tableName;

		if (databaseName.equals(Config.DatabaseNames.MYSQL))
		{
			dbInfo = new MySQLDbInfo(serializer, finalTableName, errorHandler);
		}
		else if (databaseName.equals(Config.DatabaseNames.HSQLDB))
		{
			dbInfo = new HSQLDbInfo(serializer, finalTableName, errorHandler);
		}
		else if (databaseName.equals(Config.DatabaseNames.SQLSERVER))
		{
			dbInfo = new SqlServerDbInfo(serializer, finalTableName, errorHandler, tenantPrefix);
			jdbc.getJdbcTemplate().setFetchSize(SQL_SERVER_FETCH_SIZE);
		}

		final List<StatementWithParams> statements = dbInfo.getCreateTableStatement();
		statements.forEach(statement -> jdbc.getJdbcTemplate().execute(statement.template));
	}

	private DbInfo getDbInfo()
	{
		if (dbInfo == null)
		{
			throw new IllegalStateException("Storage hasn't been initialized correctly.");
		}
		return dbInfo;
	}

	@Override
	public void save(final Document document)
	{
		final List<StatementWithParams> statements;

		if (document.isNew())
		{
			statements = getDbInfo().getInsertStatement(document);
		}
		else
		{
			statements = getDbInfo().getUpdateStatement(document);
		}

		final Integer updatedRows;
		updatedRows = statements.stream()
		                        .map(s -> jdbc.update(s.template, s.values))
		                        .reduce(Integer::sum)
		                        .orElseThrow(() -> DocumentConcurrentModificationException.documentHasBeenModified(document));

		if (updatedRows == 0)
		{
			throw DocumentConcurrentModificationException.documentHasBeenModified(document);
		}
	}

	@Override
	public void remove(final Document document)
	{
		final StatementWithParams statement = getDbInfo().getRemoveStatement(document);


		final int removedRows = jdbc.update(statement.template, statement.values);

		if (removedRows == 0)
		{
			throw DocumentConcurrentModificationException.documentHasBeenModified(document);
		}

		getDbInfo().getAdditionalRemoveStatements(document).forEach(s -> jdbc.update(s.template, s.values));
	}

	@Override
	protected Document instantiateNewDocument(final Identity rootId)
	{
		return new Document(rootId);
	}

	@Override
	protected QueryResult findByRootId(final Identity id)
	{
		final StatementWithParams statement = getDbInfo().getFindByRootIdStatement(id);

		final List<String> cart = jdbc.queryForList(statement.template, statement.values, String.class);

		return cart.stream().findFirst().map(serializer::deserialize).map(QueryResult::from).orElseGet(QueryResult::empty);
	}

	@Override
	protected QueryResult findByRootAttributes(final EntityCondition condition)
	{
		final StatementWithParams statement = dbInfo.getFindByRootAttributesStatement(condition);

		return QueryResult.from(jdbc.queryForList(statement.template, statement.values, String.class).stream()
		                            .map(serializer::deserialize).collect(Collectors.toList()));
	}

	@Override
	protected QueryResult findByEntityId(final Identity id)
	{
		final StatementWithParams statement = getDbInfo().getFindByEntityIdStatement(id);

		final List<String> cart = jdbc.queryForList(statement.template, statement.values, String.class);

		return cart.stream().findFirst().map(serializer::deserialize).map(QueryResult::from).orElseGet(QueryResult::empty);
	}

	@Override
	protected QueryResult findByEntityAttributes(final EntityCondition condition)
	{
		errorHandler.onFullTableScan(condition);

		final Predicate<ItemStateView> predicate = condition.getPredicate();
		final List<Document> matchingCarts = new ArrayList<>();

		final StatementWithParams statement = getDbInfo().getFindAllStatement();

		jdbc.getJdbcTemplate().query(statement.template, rs -> {
			final Document cart = serializer.deserialize(rs.getString(1));
			if (cart.allEntities().anyMatch(predicate))
			{
				matchingCarts.add(cart);
			}
		});

		return QueryResult.from(matchingCarts);
	}


	private abstract static class DbInfo
	{
		static final String ID_COLUMN = "id";
		static final String VERSION_COLUMN = "version";
		static final String TYPE_ID_COLUMN = "typeid";
		static final String CODE_COLUMN = "code";
		static final String GUID_COLUMN = "guid";
		static final String USER_COLUMN = "userid";
		static final String SITE_COLUMN = "siteid";
		static final String ENTITY_IDS_COLUMN = "entityids";
		static final String CART_JSON_COLUMN = "cart";
		private final Map<SingleAttributeKey, String> cartAttributeToColumnMapping = Map.of(CoreAttributes.pk(), ID_COLUMN,
				CoreAttributes.version(), VERSION_COLUMN,
				CoreAttributes.type(), TYPE_ID_COLUMN,
				CartAttributes.code(), CODE_COLUMN,
				CartAttributes.guid(), GUID_COLUMN,
				CartAttributes.user(), USER_COLUMN,
				CartAttributes.site(), SITE_COLUMN);
		private final Serializer serializer;
		private final ErrorHandler errorHandler;


		public DbInfo(final Serializer serializer,
		              final ErrorHandler errorHandler)
		{
			this.serializer = Objects.requireNonNull(serializer, "serializer mustn't be null.");
			this.errorHandler = Objects.requireNonNullElseGet(errorHandler, () -> new ErrorHandler(false));
		}

		protected String getRootFilteringQuery(final EntityCondition condition)
		{
			final WhereClauseBuildingVisitor whereClauseBuilder = new WhereClauseBuildingVisitor("",
					cartAttributeToColumnMapping);
			condition.getCondition().visit(whereClauseBuilder);

			final StringBuilder queryBuilder = new StringBuilder(getFindAllStatement().template);
			if (whereClauseBuilder.containsAnyCondition)
			{
				queryBuilder.append(" WHERE ").append(whereClauseBuilder.getString());
			}
			else
			{
				errorHandler.onFullTableScan(condition);
			}

			return queryBuilder.toString();
		}

		protected Map<String, Object> getParams(final Document document, final boolean forUpdate)
		{
			final Object id = toSQLParamValue(document.getRootId());
			final long currentVersion = document.getVersion();
			final Entity rootEntity = document.getRootEntity();
			final Object code = Optional.ofNullable(rootEntity.get(CartAttributes.code())).map(this::toSQLParamValue)
			                            .orElseGet(this::unknownStringValue);
			final Object guid = Optional.ofNullable(rootEntity.get(CartAttributes.guid())).map(this::toSQLParamValue)
			                            .orElseGet(this::unknownStringValue);
			final Object entityIds = toSQLParamValue(createEntityIdsParam(document));
			final Object typeId = toSQLParamValue(rootEntity.get(CoreAttributes.type()));
			final Object user = toSQLParamValue(rootEntity.get(CartAttributes.user()));
			final Object site = toSQLParamValue(rootEntity.get(CartAttributes.site()));
			final long newVersion = document.getVersion() + 1;
			final String json = serializer.serializeWithOverriddenVersion(document, newVersion);

			final Map<String, Object> params = new HashMap<>();

			params.put("id", id);
			params.put("newVersion", newVersion);
			params.put("typeId", typeId);
			params.put("code", code);
			params.put("guid", guid);
			params.put("user", user);
			params.put("site", site);
			params.put("entityIds", entityIds);
			params.put("cart", json);

			if (forUpdate)
			{
				params.put("currentVersion", currentVersion);
			}

			return params;
		}

		protected String createEntityIdsParam(final Document document)
		{
			return document.getEntityIds().stream().mapToLong(Identity::toLongValue)
			               .mapToObj(Long::toString).collect(Collectors.joining(","));
		}

		private Object unknownStringValue()
		{
			return toSQLParamValue("UNKNOWN" + UUID.randomUUID().toString());
		}

		public Object toSQLParamValue(final Object obj)
		{
			if (obj instanceof String || obj instanceof Number)
			{
				return obj;
			}
			if (obj instanceof Reference)
			{
				return ((Reference) obj).getIdentity().toLongValue();
			}
			if (obj instanceof Identity)
			{
				return ((Identity) obj).toLongValue();
			}
			return obj;
		}

		public abstract List<StatementWithParams> getCreateTableStatement();

		public abstract StatementWithParams getFindAllStatement();

		public abstract StatementWithParams getFindByRootIdStatement(Identity id);

		public abstract StatementWithParams getFindByRootAttributesStatement(EntityCondition condition);

		public abstract StatementWithParams getFindByEntityIdStatement(Identity id);

		public abstract List<StatementWithParams> getInsertStatement(Document document);

		public abstract List<StatementWithParams> getUpdateStatement(Document document);

		public abstract StatementWithParams getRemoveStatement(Document document);

		public List<StatementWithParams> getAdditionalRemoveStatements(final Document document)
		{
			return List.of();
		}
	}

	public static class MySQLDbInfo extends DatabaseCartStorage.DbInfo
	{

		private final String createTableTemplate;
		private final String findAllTemplate;
		private final String findByRootIdTemplate;
		private final String findByEntityIdTemplate;
		private final String insertTemplate;
		private final String updateTemplate;
		private final String deleteTemplate;

		/**
		 * @deprecated use {@link #MySQLDbInfo(Serializer, String, ErrorHandler)}
		 */
		@Deprecated(since = "2005", forRemoval = true)
		public MySQLDbInfo(final Serializer serializer, final String tableName)
		{
			this(serializer, tableName, null);
		}

		public MySQLDbInfo(final Serializer serializer, final String tableName,
		                   final ErrorHandler errorHandler)
		{
			super(serializer, errorHandler);

			Objects.requireNonNull(tableName, "tableName mustn't be null.");

			createTableTemplate = String.format(
					"CREATE TABLE IF NOT EXISTS %s(%s BIGINT PRIMARY KEY, %s BIGINT NOT NULL, %s BIGINT NOT NULL, %s VARCHAR(255) NOT NULL UNIQUE, %s VARCHAR(255) NOT NULL, %s BIGINT NOT NULL, %s BIGINT, %s VARCHAR(2048) NULL,  %s JSON NOT NULL, FULLTEXT(%s), INDEX by_site_and_user (%s,%s))",
					tableName, ID_COLUMN, VERSION_COLUMN, TYPE_ID_COLUMN, CODE_COLUMN, GUID_COLUMN, USER_COLUMN, SITE_COLUMN,
					ENTITY_IDS_COLUMN,
					CART_JSON_COLUMN, ENTITY_IDS_COLUMN, USER_COLUMN, SITE_COLUMN);

			findAllTemplate = String.format("SELECT %s FROM %s", CART_JSON_COLUMN, tableName);

			findByRootIdTemplate = String.format("select %s from %s where %s=:id", CART_JSON_COLUMN, tableName, ID_COLUMN);

			findByEntityIdTemplate = String.format("select %s from %s where MATCH(%s) AGAINST(:id IN BOOLEAN MODE)",
					CART_JSON_COLUMN,
					tableName, ENTITY_IDS_COLUMN);

			insertTemplate = String.format(
					"INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES(:id, :newVersion, :typeId, :code, :guid, :user, :site, :entityIds, :cart)",
					tableName, ID_COLUMN, VERSION_COLUMN, TYPE_ID_COLUMN, CODE_COLUMN, GUID_COLUMN, USER_COLUMN, SITE_COLUMN,
					ENTITY_IDS_COLUMN,
					CART_JSON_COLUMN);

			updateTemplate = String.format(
					"UPDATE %s SET %s=:newVersion, %s=:code, %s=:guid, %s=:user, %s=:site, %s=:entityIds, %s=:cart WHERE %s=:id and %s=:currentVersion",
					tableName, VERSION_COLUMN, CODE_COLUMN, GUID_COLUMN, USER_COLUMN, SITE_COLUMN, ENTITY_IDS_COLUMN,
					CART_JSON_COLUMN, ID_COLUMN,
					VERSION_COLUMN);

			deleteTemplate = String.format("DELETE FROM %s WHERE %s=:id and %s=:version", tableName, ID_COLUMN, VERSION_COLUMN);
		}

		@Override
		public List<StatementWithParams> getCreateTableStatement()
		{
			return List.of(new StatementWithParams(createTableTemplate, Map.of()));
		}

		@Override
		public StatementWithParams getFindAllStatement()
		{
			return new StatementWithParams(findAllTemplate, Map.of());
		}

		@Override
		public StatementWithParams getFindByRootIdStatement(final Identity id)
		{

			return new StatementWithParams(findByRootIdTemplate, Map.of("id", toSQLParamValue(id)));
		}

		@Override
		public StatementWithParams getFindByRootAttributesStatement(final EntityCondition condition)
		{
			final String query = getRootFilteringQuery(condition);
			final Map<String, Object> jdbcParams = new HashMap<>();
			condition.getParams().forEach((k, v) -> jdbcParams.put(k, toSQLParamValue(v)));

			return new StatementWithParams(query, jdbcParams);
		}

		@Override
		public StatementWithParams getFindByEntityIdStatement(final Identity id)
		{
			return new StatementWithParams(findByEntityIdTemplate, Map.of("id", toBooleanSearchId(id)));
		}

		private String toBooleanSearchId(final Identity id)
		{
			return "+" + toSQLParamValue(id);
		}

		@Override
		public List<StatementWithParams> getInsertStatement(final Document document)
		{
			return List.of(new StatementWithParams(insertTemplate, getParams(document, false)));
		}

		@Override
		public List<StatementWithParams> getUpdateStatement(final Document document)
		{
			return List.of(new StatementWithParams(updateTemplate, getParams(document, true)));
		}

		@Override
		public StatementWithParams getRemoveStatement(final Document document)
		{
			return new StatementWithParams(deleteTemplate,
					Map.of("id", toSQLParamValue(document.getRootId()), "version",
							toSQLParamValue(document.getVersion())));
		}
	}

	public static class HSQLDbInfo extends MySQLDbInfo
	{
		private final String createTableTemplate;
		private final String findByEntityIdTemplate;

		/**
		 * @deprecated use {@link #HSQLDbInfo(Serializer, String, ErrorHandler)}
		 */
		@Deprecated(since = "2005", forRemoval = true)
		public HSQLDbInfo(final Serializer serializer, final String tableName)
		{
			this(serializer, tableName, null);
		}

		public HSQLDbInfo(final Serializer serializer, final String tableName,
		                  final ErrorHandler errorHandler)
		{
			super(serializer, tableName, errorHandler);

			createTableTemplate = String.format(
					"CREATE CACHED TABLE IF NOT EXISTS %s(%s BIGINT PRIMARY KEY, %s BIGINT NOT NULL, %s BIGINT NOT NULL, %s VARCHAR(255) NOT NULL UNIQUE, %s VARCHAR(255) NOT NULL, %s BIGINT NOT NULL, %s BIGINT, %s VARCHAR(2048) NOT NULL, %s LONGVARCHAR NOT NULL)",
					tableName, ID_COLUMN, VERSION_COLUMN, TYPE_ID_COLUMN, CODE_COLUMN, GUID_COLUMN, USER_COLUMN, SITE_COLUMN,
					ENTITY_IDS_COLUMN,
					CART_JSON_COLUMN);

			findByEntityIdTemplate = String.format("select %s from %s where POSITION(CONCAT(',', :id, ',') IN %s) > 0",
					CART_JSON_COLUMN,
					tableName, ENTITY_IDS_COLUMN);

		}

		@Override
		public List<StatementWithParams> getCreateTableStatement()
		{
			return List.of(new StatementWithParams(createTableTemplate, Map.of()));
		}

		@Override
		public StatementWithParams getFindByEntityIdStatement(final Identity id)
		{
			return new StatementWithParams(findByEntityIdTemplate, Map.of("id", toSQLParamValue(id)));
		}

		@Override
		protected String createEntityIdsParam(final Document document)
		{
			return document.getEntityIds().stream().mapToLong(Identity::toLongValue)
			               .mapToObj(Long::toString).collect(Collectors.joining(",", ",", ","));
		}
	}

	public static class SqlServerDbInfo extends MySQLDbInfo
	{
		static final String ENTITY_ID_COLUMN = "entityid";

		private final String createTableTemplate;
		private final String findByEntityIdTemplate;
		private final String createEntityIdsTableTemplate;
		private final String insertEntityIdsTemplate;
		private final String deleteEntityIdsTemplate;

		public SqlServerDbInfo(final Serializer serializer, final String tableName,
		                       final ErrorHandler errorHandler, final String tenantPrefix)
		{
			super(serializer, tableName, errorHandler);

			final String entityIdsTable = tableName + "_entityIds";

			createTableTemplate = String.format(
					"IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='%s' and xtype='U') " +
							"CREATE TABLE %s(" +
							"%s BIGINT PRIMARY KEY, " +
							"%s BIGINT NOT NULL, " +
							"%s BIGINT NOT NULL, " +
							"%s NVARCHAR(255) NOT NULL UNIQUE, " +
							"%s NVARCHAR(255) NOT NULL, " +
							"%s BIGINT NOT NULL, %s BIGINT, " +
							"%s VARCHAR(2048) NOT NULL, %s NTEXT NOT NULL)",
					tableName, tableName, ID_COLUMN, VERSION_COLUMN, TYPE_ID_COLUMN, CODE_COLUMN, GUID_COLUMN, USER_COLUMN,
					SITE_COLUMN, ENTITY_IDS_COLUMN, CART_JSON_COLUMN);

			final String entityIdsTablePK = tenantPrefix + "entity_pks";
			final String entityIdIndex = tenantPrefix + "entityIds";

			createEntityIdsTableTemplate = String.format(
					"IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='%s' and xtype='U') " +
							"CREATE TABLE %s(" +
							"%s BIGINT NOT NULL, " +
							"%s BIGINT NOT NULL INDEX %s, " +
							"CONSTRAINT %s PRIMARY KEY (%s, %s) WITH (IGNORE_DUP_KEY = ON))",
					entityIdsTable, entityIdsTable, ID_COLUMN, ENTITY_ID_COLUMN, entityIdIndex, entityIdsTablePK, ID_COLUMN,
					ENTITY_ID_COLUMN);


			findByEntityIdTemplate = String.format("select %s from %s c join %s e on c.%s = e.%s where e.%s=:id",
					CART_JSON_COLUMN, tableName, entityIdsTable, ID_COLUMN, ID_COLUMN, ENTITY_ID_COLUMN);

			insertEntityIdsTemplate = String.format("insert into %s values ", entityIdsTable);
			deleteEntityIdsTemplate = String.format("delete from %s where %s=:id ", entityIdsTable, ID_COLUMN);
		}

		@Override
		public List<StatementWithParams> getCreateTableStatement()
		{
			return List.of(new StatementWithParams(createTableTemplate, Map.of()),
					new StatementWithParams(createEntityIdsTableTemplate, Map.of()));
		}

		@Override
		public StatementWithParams getFindByEntityIdStatement(final Identity id)
		{
			return new StatementWithParams(findByEntityIdTemplate, Map.of("id", toSQLParamValue(id)));
		}

		@Override
		protected String createEntityIdsParam(final Document document)
		{
			return document.getEntityIds().stream().mapToLong(Identity::toLongValue)
			               .mapToObj(Long::toString).collect(Collectors.joining(",", ",", ","));
		}

		@Override
		public List<StatementWithParams> getInsertStatement(final Document document)
		{
			final List<StatementWithParams> statements = new LinkedList<>(super.getInsertStatement(document));

			getEntityIdsInsertStatement(document).ifPresent(statements::add);

			return statements;
		}

		@Override
		public List<StatementWithParams> getUpdateStatement(final Document document)
		{
			final List<StatementWithParams> statements = new LinkedList<>(super.getUpdateStatement(document));

			getEntityIdsInsertStatement(document).ifPresent(statements::add);
			getEntityIdsDeleteStatement(document).ifPresent(statements::add);

			return statements;
		}

		@Override
		public List<StatementWithParams> getAdditionalRemoveStatements(final Document document)
		{
			return getAllEntityIdsDeleteStatement(document).map(List::of).orElseGet(List::of);
		}

		private Optional<StatementWithParams> getEntityIdsInsertStatement(final Document document)
		{

			if (document.getEntityIds().isEmpty())
			{
				return Optional.empty();
			}

			final Map<String, Object> params = new HashMap<>();
			final AtomicInteger idx = new AtomicInteger();

			params.put("cId", toSQLParamValue(document.getRootId()));

			final Stream<StringBuilder> values = document.getEntityIds().stream().map(entityId -> {
				final String entityIdKey = "eId" + idx.getAndIncrement();
				params.put(entityIdKey, toSQLParamValue(entityId));
				return new StringBuilder("(:cId, :").append(entityIdKey).append(")");
			});

			final String query = insertEntityIdsTemplate + values.collect(Collectors.joining(","));
			return Optional.of(new StatementWithParams(query, params));
		}

		private Optional<StatementWithParams> getEntityIdsDeleteStatement(final Document document)
		{
			if (document.getEntityIds().isEmpty())
			{
				return getAllEntityIdsDeleteStatement(document);
			}

			final Map<String, Object> params = new HashMap<>();

			params.put("id", toSQLParamValue(document.getRootId()));

			final AtomicInteger idx = new AtomicInteger();

			final Stream<StringBuilder> values = document.getEntityIds().stream().map(entityId -> {
				final int i = idx.getAndIncrement();
				final String entityIdKey = "eId" + i;
				params.put(entityIdKey, entityId.toLongValue());
				return new StringBuilder(":").append(entityIdKey);
			});
			final String query = deleteEntityIdsTemplate + String.format(" AND %s NOT IN ",
					ENTITY_ID_COLUMN) + values
					.collect(Collectors.joining(",", "(", ")"));
			return Optional.of(new StatementWithParams(query, params));
		}

		private Optional<StatementWithParams> getAllEntityIdsDeleteStatement(final Document document)
		{
			return Optional.of(
					new StatementWithParams(deleteEntityIdsTemplate, Map.of("id", toSQLParamValue(document.getRootId()))));
		}
	}

	static class StatementWithParams
	{
		final String template;

		private final Map<String, Object> values;

		StatementWithParams(final String template, final Map<String, Object> values)
		{
			this.template = Objects.requireNonNull(template, "queryTemplate mustn't be null.");
			this.values = Objects.requireNonNull(values, "values mustn't be null.");
		}

	}

	private static class WhereClauseBuildingVisitor extends AbstractToStringVisitor
	{
		private final Map<SingleAttributeKey, String> attributeToColumnMapping;
		private final String columnPrefix;

		private boolean containsAnyCondition = false;

		public WhereClauseBuildingVisitor(final String columnPrefix,
		                                  final Map<SingleAttributeKey, String> attributeToColumnMapping)
		{
			this.columnPrefix = Objects.requireNonNull(columnPrefix, "columnPrefix mustn't be null.");
			this.attributeToColumnMapping = Objects.requireNonNull(attributeToColumnMapping,
					"attributeToColumnMapping mustn't be null.");
		}

		@Override
		protected String asString(final ComparisonCondition condition)
		{
			final String columnName = attributeToColumnMapping.get(condition.getKey());
			if (columnName == null)
			{
				return "1=1";
			}
			containsAnyCondition = true;

			final StringBuilder resultBuilder = new StringBuilder(columnPrefix).append(columnName);

			final Optional<String> possibleParamName = condition.getParamNameToCompare();

			if (possibleParamName.isPresent())
			{
				resultBuilder.append(condition.getOperator().getOperatorStr()).append(":").append(possibleParamName.get());
			}
			else
			{
				switch (condition.getOperator())
				{
					case EQUAL:
						resultBuilder.append(" IS NULL");
						break;
					case NOT_EQUAL:
						resultBuilder.append(" IS NOT NULL");
						break;
					default:
						throw new IllegalArgumentException(
								"ComparisionCondition.paramNameToCompare cannot be null with operator '" + condition.getOperator() + "'");
				}
			}

			return resultBuilder.toString();
		}

	}

	public static class ErrorHandler
	{
		private final boolean throwExceptionsOnFullTableScan;

		public ErrorHandler(final boolean throwExceptionsOnFullTableScan)
		{
			this.throwExceptionsOnFullTableScan = throwExceptionsOnFullTableScan;
		}

		public void onFullTableScan(final EntityCondition condition)
		{
			if (throwExceptionsOnFullTableScan)
			{
				throwExOnFullTableScan(condition);
			}
			else
			{
				warnOnFullTableScan(condition);
			}
		}

		private void throwExOnFullTableScan(final EntityCondition condition)
		{
			throw new IllegalArgumentException("Searching by entity attributes '" + condition +
					"' is not supported because it requires to read full carts table. To enable support for such operations set property "
					+ PROPERTY_SHOULD_THROW_EX_ON_FULL_TBL_SCAN + " to 'false'");
		}

		private void warnOnFullTableScan(final EntityCondition condition)
		{
			LOG.warn("Searching by entity attributes '{}'. It will cause reading full carts table.", condition);
		}
	}
}
