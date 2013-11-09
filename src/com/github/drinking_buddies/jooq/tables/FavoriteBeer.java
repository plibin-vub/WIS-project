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
public class FavoriteBeer extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.FavoriteBeerRecord> {

	private static final long serialVersionUID = 2060304188;

	/**
	 * The singleton instance of <code>favorite_beer</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.FavoriteBeer FAVORITE_BEER = new com.github.drinking_buddies.jooq.tables.FavoriteBeer();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.FavoriteBeerRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.FavoriteBeerRecord.class;
	}

	/**
	 * The column <code>favorite_beer.user_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.FavoriteBeerRecord, java.lang.Integer> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>favorite_beer.beer_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.FavoriteBeerRecord, java.lang.Integer> BEER_ID = createField("beer_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * Create a <code>favorite_beer</code> table reference
	 */
	public FavoriteBeer() {
		super("favorite_beer", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>favorite_beer</code> table reference
	 */
	public FavoriteBeer(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.FavoriteBeer.FAVORITE_BEER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.FavoriteBeerRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.FavoriteBeerRecord, ?>>asList(com.github.drinking_buddies.jooq.Keys.FK_FAVORITE_BEER_USER_1, com.github.drinking_buddies.jooq.Keys.FK_FAVORITE_BEER_BEER_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.FavoriteBeer as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.FavoriteBeer(alias);
	}
}
