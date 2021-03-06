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
public class Beer2BarRecord extends org.jooq.impl.TableRecordImpl<com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord> implements org.jooq.Record2<java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = -118445512;

	/**
	 * Setter for <code>beer2_bar.beer_id</code>. 
	 */
	public void setBeerId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>beer2_bar.beer_id</code>. 
	 */
	public java.lang.Integer getBeerId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>beer2_bar.bar_id</code>. 
	 */
	public void setBarId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>beer2_bar.bar_id</code>. 
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
		return com.github.drinking_buddies.jooq.tables.Beer2Bar.BEER2_BAR.BEER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return com.github.drinking_buddies.jooq.tables.Beer2Bar.BEER2_BAR.BAR_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getBeerId();
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
	 * Create a detached Beer2BarRecord
	 */
	public Beer2BarRecord() {
		super(com.github.drinking_buddies.jooq.tables.Beer2Bar.BEER2_BAR);
	}

	/**
	 * Create a detached, initialised Beer2BarRecord
	 */
	public Beer2BarRecord(java.lang.Integer beerId, java.lang.Integer barId) {
		super(com.github.drinking_buddies.jooq.tables.Beer2Bar.BEER2_BAR);

		setValue(0, beerId);
		setValue(1, barId);
	}
}
