/**
 * This class is generated by jOOQ
 */
package com.github.drinking_buddies.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.2.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FavoriteBarRecord extends org.jooq.impl.TableRecordImpl<com.github.drinking_buddies.jooq.tables.records.FavoriteBarRecord> implements org.jooq.Record2<java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = 110103088;

	/**
	 * Setter for <code>favorite_bar.user_id</code>. 
	 */
	public void setUserId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>favorite_bar.user_id</code>. 
	 */
	public java.lang.Integer getUserId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>favorite_bar.bar_id</code>. 
	 */
	public void setBarId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>favorite_bar.bar_id</code>. 
	 */
	public java.lang.Integer getBarId() {
		return (java.lang.Integer) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return com.github.drinking_buddies.jooq.tables.FavoriteBar.FAVORITE_BAR.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return com.github.drinking_buddies.jooq.tables.FavoriteBar.FAVORITE_BAR.BAR_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getBarId();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached FavoriteBarRecord
	 */
	public FavoriteBarRecord() {
		super(com.github.drinking_buddies.jooq.tables.FavoriteBar.FAVORITE_BAR);
	}

	/**
	 * Create a detached, initialised FavoriteBarRecord
	 */
	public FavoriteBarRecord(java.lang.Integer userId, java.lang.Integer barId) {
		super(com.github.drinking_buddies.jooq.tables.FavoriteBar.FAVORITE_BAR);

		setValue(0, userId);
		setValue(1, barId);
	}
}