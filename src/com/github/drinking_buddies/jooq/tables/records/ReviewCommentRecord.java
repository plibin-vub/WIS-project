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
public class ReviewCommentRecord extends org.jooq.impl.UpdatableRecordImpl<com.github.drinking_buddies.jooq.tables.records.ReviewCommentRecord> implements org.jooq.Record5<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = -165285330;

	/**
	 * Setter for <code>review_comment.id</code>. 
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>review_comment.id</code>. 
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>review_comment.id_review</code>. 
	 */
	public void setIdReview(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>review_comment.id_review</code>. 
	 */
	public java.lang.Integer getIdReview() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>review_comment.id_user</code>. 
	 */
	public void setIdUser(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>review_comment.id_user</code>. 
	 */
	public java.lang.Integer getIdUser() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>review_comment.text</code>. 
	 */
	public void setText(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>review_comment.text</code>. 
	 */
	public java.lang.String getText() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>review_comment.post_time</code>. 
	 */
	public void setPostTime(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>review_comment.post_time</code>. 
	 */
	public java.lang.String getPostTime() {
		return (java.lang.String) getValue(4);
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
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return com.github.drinking_buddies.jooq.tables.ReviewComment.REVIEW_COMMENT.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return com.github.drinking_buddies.jooq.tables.ReviewComment.REVIEW_COMMENT.ID_REVIEW;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return com.github.drinking_buddies.jooq.tables.ReviewComment.REVIEW_COMMENT.ID_USER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return com.github.drinking_buddies.jooq.tables.ReviewComment.REVIEW_COMMENT.TEXT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return com.github.drinking_buddies.jooq.tables.ReviewComment.REVIEW_COMMENT.POST_TIME;
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
		return getIdReview();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getIdUser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getPostTime();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ReviewCommentRecord
	 */
	public ReviewCommentRecord() {
		super(com.github.drinking_buddies.jooq.tables.ReviewComment.REVIEW_COMMENT);
	}

	/**
	 * Create a detached, initialised ReviewCommentRecord
	 */
	public ReviewCommentRecord(java.lang.Integer id, java.lang.Integer idReview, java.lang.Integer idUser, java.lang.String text, java.lang.String postTime) {
		super(com.github.drinking_buddies.jooq.tables.ReviewComment.REVIEW_COMMENT);

		setValue(0, id);
		setValue(1, idReview);
		setValue(2, idUser);
		setValue(3, text);
		setValue(4, postTime);
	}
}
