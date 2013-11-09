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
public class Beer2Bar extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord> {

	private static final long serialVersionUID = -723091511;

	/**
	 * The singleton instance of <code>beer2_bar</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.Beer2Bar BEER2_BAR = new com.github.drinking_buddies.jooq.tables.Beer2Bar();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord.class;
	}

	/**
	 * The column <code>beer2_bar.beer_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord, java.lang.Integer> BEER_ID = createField("beer_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>beer2_bar.bar_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord, java.lang.Integer> BAR_ID = createField("bar_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * Create a <code>beer2_bar</code> table reference
	 */
	public Beer2Bar() {
		super("beer2_bar", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>beer2_bar</code> table reference
	 */
	public Beer2Bar(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.Beer2Bar.BEER2_BAR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord, ?>>asList(com.github.drinking_buddies.jooq.Keys.FK_BEER2_BAR_BEER_1, com.github.drinking_buddies.jooq.Keys.FK_BEER2_BAR_BAR_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.Beer2Bar as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.Beer2Bar(alias);
	}
}
