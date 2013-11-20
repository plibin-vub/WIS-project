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
public class Bar extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.BarRecord> {

	private static final long serialVersionUID = -20944622;

	/**
	 * The singleton instance of <code>bar</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.Bar BAR = new com.github.drinking_buddies.jooq.tables.Bar();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.BarRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.BarRecord.class;
	}

	/**
	 * The column <code>bar.id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>bar.name</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this);

	/**
	 * The column <code>bar.photo</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, byte[]> PHOTO = createField("photo", org.jooq.impl.SQLDataType.BLOB, this);

	/**
	 * The column <code>bar.website</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, java.lang.String> WEBSITE = createField("website", org.jooq.impl.SQLDataType.CLOB, this);

	/**
	 * The column <code>bar.address_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, java.lang.Integer> ADDRESS_ID = createField("address_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this);

	/**
	 * The column <code>bar.location_x</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, java.lang.Float> LOCATION_X = createField("location_x", org.jooq.impl.SQLDataType.REAL, this);

	/**
	 * The column <code>bar.location_y</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, java.lang.Float> LOCATION_Y = createField("location_y", org.jooq.impl.SQLDataType.REAL, this);

	/**
	 * The column <code>bar.url</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BarRecord, java.lang.String> URL = createField("url", org.jooq.impl.SQLDataType.CLOB, this);

	/**
	 * Create a <code>bar</code> table reference
	 */
	public Bar() {
		super("bar", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>bar</code> table reference
	 */
	public Bar(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.Bar.BAR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.BarRecord> getPrimaryKey() {
		return com.github.drinking_buddies.jooq.Keys.PK_BAR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.BarRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.BarRecord>>asList(com.github.drinking_buddies.jooq.Keys.PK_BAR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.BarRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.BarRecord, ?>>asList(com.github.drinking_buddies.jooq.Keys.FK_BAR_ADDRESS_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.Bar as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.Bar(alias);
	}
}
