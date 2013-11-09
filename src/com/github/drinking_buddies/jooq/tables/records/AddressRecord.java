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
public class AddressRecord extends org.jooq.impl.UpdatableRecordImpl<com.github.drinking_buddies.jooq.tables.records.AddressRecord> implements org.jooq.Record6<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = 597560730;

	/**
	 * Setter for <code>address.id</code>. 
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>address.id</code>. 
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>address.street</code>. 
	 */
	public void setStreet(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>address.street</code>. 
	 */
	public java.lang.String getStreet() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>address.number</code>. 
	 */
	public void setNumber(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>address.number</code>. 
	 */
	public java.lang.String getNumber() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>address.zipcode</code>. 
	 */
	public void setZipcode(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>address.zipcode</code>. 
	 */
	public java.lang.String getZipcode() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>address.city</code>. 
	 */
	public void setCity(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>address.city</code>. 
	 */
	public java.lang.String getCity() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>address.country</code>. 
	 */
	public void setCountry(java.lang.String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>address.country</code>. 
	 */
	public java.lang.String getCountry() {
		return (java.lang.String) getValue(5);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record6 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row6) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row6) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return com.github.drinking_buddies.jooq.tables.Address.ADDRESS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return com.github.drinking_buddies.jooq.tables.Address.ADDRESS.STREET;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return com.github.drinking_buddies.jooq.tables.Address.ADDRESS.NUMBER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return com.github.drinking_buddies.jooq.tables.Address.ADDRESS.ZIPCODE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return com.github.drinking_buddies.jooq.tables.Address.ADDRESS.CITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field6() {
		return com.github.drinking_buddies.jooq.tables.Address.ADDRESS.COUNTRY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getStreet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getNumber();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getZipcode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getCity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value6() {
		return getCountry();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached AddressRecord
	 */
	public AddressRecord() {
		super(com.github.drinking_buddies.jooq.tables.Address.ADDRESS);
	}

	/**
	 * Create a detached, initialised AddressRecord
	 */
	public AddressRecord(java.lang.Integer id, java.lang.String street, java.lang.String number, java.lang.String zipcode, java.lang.String city, java.lang.String country) {
		super(com.github.drinking_buddies.jooq.tables.Address.ADDRESS);

		setValue(0, id);
		setValue(1, street);
		setValue(2, number);
		setValue(3, zipcode);
		setValue(4, city);
		setValue(5, country);
	}
}
