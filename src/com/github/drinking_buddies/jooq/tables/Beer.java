/**
 * This class is generated by jOOQ
 */
package com.github.drinking_buddies.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.2.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Beer extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.BeerRecord> {

	private static final long serialVersionUID = -2049742280;

	/**
	 * The singleton instance of <code>beer</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.Beer BEER = new com.github.drinking_buddies.jooq.tables.Beer();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.BeerRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.BeerRecord.class;
	}

	/**
	 * The column <code>beer.id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BeerRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>beer.url</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BeerRecord, java.lang.String> URL = createField("url", org.jooq.impl.SQLDataType.CLOB.nullable(false), this);

	/**
	 * The column <code>beer.name</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BeerRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this);

	/**
	 * The column <code>beer.webservice_name</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BeerRecord, java.lang.String> WEBSERVICE_NAME = createField("webservice_name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this);

	/**
	 * Create a <code>beer</code> table reference
	 */
	public Beer() {
		super("beer", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>beer</code> table reference
	 */
	public Beer(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.Beer.BEER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.BeerRecord> getPrimaryKey() {
		return com.github.drinking_buddies.jooq.Keys.PK_BEER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.BeerRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.BeerRecord>>asList(com.github.drinking_buddies.jooq.Keys.PK_BEER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.Beer as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.Beer(alias);
	}
}
