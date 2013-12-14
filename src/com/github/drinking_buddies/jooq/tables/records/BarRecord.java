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
public class BarRecord extends org.jooq.impl.UpdatableRecordImpl<com.github.drinking_buddies.jooq.tables.records.BarRecord> implements org.jooq.Record9<java.lang.Integer, java.lang.String, byte[], java.lang.String, java.lang.String, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.String> {

	private static final long serialVersionUID = -1534339303;

	/**
	 * Setter for <code>bar.id</code>. 
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>bar.id</code>. 
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>bar.name</code>. 
	 */
	public void setName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>bar.name</code>. 
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>bar.photo</code>. 
	 */
	public void setPhoto(byte[] value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>bar.photo</code>. 
	 */
	public byte[] getPhoto() {
		return (byte[]) getValue(2);
	}

	/**
	 * Setter for <code>bar.photo_mime_type</code>. 
	 */
	public void setPhotoMimeType(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>bar.photo_mime_type</code>. 
	 */
	public java.lang.String getPhotoMimeType() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>bar.website</code>. 
	 */
	public void setWebsite(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>bar.website</code>. 
	 */
	public java.lang.String getWebsite() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>bar.address_id</code>. 
	 */
	public void setAddressId(java.lang.Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>bar.address_id</code>. 
	 */
	public java.lang.Integer getAddressId() {
		return (java.lang.Integer) getValue(5);
	}

	/**
	 * Setter for <code>bar.location_x</code>. 
	 */
	public void setLocationX(java.lang.Float value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>bar.location_x</code>. 
	 */
	public java.lang.Float getLocationX() {
		return (java.lang.Float) getValue(6);
	}

	/**
	 * Setter for <code>bar.location_y</code>. 
	 */
	public void setLocationY(java.lang.Float value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>bar.location_y</code>. 
	 */
	public java.lang.Float getLocationY() {
		return (java.lang.Float) getValue(7);
	}

	/**
	 * Setter for <code>bar.url</code>. 
	 */
	public void setUrl(java.lang.String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>bar.url</code>. 
	 */
	public java.lang.String getUrl() {
		return (java.lang.String) getValue(8);
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
	// Record9 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Integer, java.lang.String, byte[], java.lang.String, java.lang.String, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.String> fieldsRow() {
		return (org.jooq.Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Integer, java.lang.String, byte[], java.lang.String, java.lang.String, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.String> valuesRow() {
		return (org.jooq.Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<byte[]> field3() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.PHOTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.PHOTO_MIME_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.WEBSITE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field6() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.ADDRESS_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Float> field7() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.LOCATION_X;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Float> field8() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.LOCATION_Y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return com.github.drinking_buddies.jooq.tables.Bar.BAR.URL;
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
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] value3() {
		return getPhoto();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getPhotoMimeType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getWebsite();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value6() {
		return getAddressId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Float value7() {
		return getLocationX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Float value8() {
		return getLocationY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getUrl();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached BarRecord
	 */
	public BarRecord() {
		super(com.github.drinking_buddies.jooq.tables.Bar.BAR);
	}

	/**
	 * Create a detached, initialised BarRecord
	 */
	public BarRecord(java.lang.Integer id, java.lang.String name, byte[] photo, java.lang.String photoMimeType, java.lang.String website, java.lang.Integer addressId, java.lang.Float locationX, java.lang.Float locationY, java.lang.String url) {
		super(com.github.drinking_buddies.jooq.tables.Bar.BAR);

		setValue(0, id);
		setValue(1, name);
		setValue(2, photo);
		setValue(3, photoMimeType);
		setValue(4, website);
		setValue(5, addressId);
		setValue(6, locationX);
		setValue(7, locationY);
		setValue(8, url);
	}
}
