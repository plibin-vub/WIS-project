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
public class BarCommentRecord extends org.jooq.impl.UpdatableRecordImpl<com.github.drinking_buddies.jooq.tables.records.BarCommentRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = 76164655;

	/**
	 * Setter for <code>bar_comment.id</code>. 
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>bar_comment.id</code>. 
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>bar_comment.user_id</code>. 
	 */
	public void setUserId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>bar_comment.user_id</code>. 
	 */
	public java.lang.Integer getUserId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>bar_comment.text</code>. 
	 */
	public void setText(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>bar_comment.text</code>. 
	 */
	public java.lang.String getText() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>bar_comment.timestamp</code>. 
	 */
	public void setTimestamp(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>bar_comment.timestamp</code>. 
	 */
	public java.lang.String getTimestamp() {
		return (java.lang.String) getValue(3);
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
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return com.github.drinking_buddies.jooq.tables.BarComment.BAR_COMMENT.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return com.github.drinking_buddies.jooq.tables.BarComment.BAR_COMMENT.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return com.github.drinking_buddies.jooq.tables.BarComment.BAR_COMMENT.TEXT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return com.github.drinking_buddies.jooq.tables.BarComment.BAR_COMMENT.TIMESTAMP;
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
	public java.lang.Integer value2() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getTimestamp();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached BarCommentRecord
	 */
	public BarCommentRecord() {
		super(com.github.drinking_buddies.jooq.tables.BarComment.BAR_COMMENT);
	}

	/**
	 * Create a detached, initialised BarCommentRecord
	 */
	public BarCommentRecord(java.lang.Integer id, java.lang.Integer userId, java.lang.String text, java.lang.String timestamp) {
		super(com.github.drinking_buddies.jooq.tables.BarComment.BAR_COMMENT);

		setValue(0, id);
		setValue(1, userId);
		setValue(2, text);
		setValue(3, timestamp);
	}
}