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
public class ReviewRecord extends org.jooq.impl.UpdatableRecordImpl<com.github.drinking_buddies.jooq.tables.records.ReviewRecord> implements org.jooq.Record9<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = -1748292089;

	/**
	 * Setter for <code>review.id</code>. 
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>review.id</code>. 
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>review.beer_id</code>. 
	 */
	public void setBeerId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>review.beer_id</code>. 
	 */
	public java.lang.Integer getBeerId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>review.user_id</code>. 
	 */
	public void setUserId(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>review.user_id</code>. 
	 */
	public java.lang.Integer getUserId() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>review.visual_score</code>. 
	 */
	public void setVisualScore(java.lang.Float value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>review.visual_score</code>. 
	 */
	public java.lang.Float getVisualScore() {
		return (java.lang.Float) getValue(3);
	}

	/**
	 * Setter for <code>review.smell_score</code>. 
	 */
	public void setSmellScore(java.lang.Float value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>review.smell_score</code>. 
	 */
	public java.lang.Float getSmellScore() {
		return (java.lang.Float) getValue(4);
	}

	/**
	 * Setter for <code>review.taste_score</code>. 
	 */
	public void setTasteScore(java.lang.Float value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>review.taste_score</code>. 
	 */
	public java.lang.Float getTasteScore() {
		return (java.lang.Float) getValue(5);
	}

	/**
	 * Setter for <code>review.feel_score</code>. 
	 */
	public void setFeelScore(java.lang.Float value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>review.feel_score</code>. 
	 */
	public java.lang.Float getFeelScore() {
		return (java.lang.Float) getValue(6);
	}

	/**
	 * Setter for <code>review.text</code>. 
	 */
	public void setText(java.lang.String value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>review.text</code>. 
	 */
	public java.lang.String getText() {
		return (java.lang.String) getValue(7);
	}

	/**
	 * Setter for <code>review.post_time</code>. 
	 */
	public void setPostTime(java.lang.String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>review.post_time</code>. 
	 */
	public java.lang.String getPostTime() {
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
	public org.jooq.Row9<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row9<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.BEER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Float> field4() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.VISUAL_SCORE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Float> field5() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.SMELL_SCORE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Float> field6() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.TASTE_SCORE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Float> field7() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.FEEL_SCORE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field8() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.TEXT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return com.github.drinking_buddies.jooq.tables.Review.REVIEW.POST_TIME;
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
		return getBeerId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Float value4() {
		return getVisualScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Float value5() {
		return getSmellScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Float value6() {
		return getTasteScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Float value7() {
		return getFeelScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value8() {
		return getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getPostTime();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ReviewRecord
	 */
	public ReviewRecord() {
		super(com.github.drinking_buddies.jooq.tables.Review.REVIEW);
	}

	/**
	 * Create a detached, initialised ReviewRecord
	 */
	public ReviewRecord(java.lang.Integer id, java.lang.Integer beerId, java.lang.Integer userId, java.lang.Float visualScore, java.lang.Float smellScore, java.lang.Float tasteScore, java.lang.Float feelScore, java.lang.String text, java.lang.String postTime) {
		super(com.github.drinking_buddies.jooq.tables.Review.REVIEW);

		setValue(0, id);
		setValue(1, beerId);
		setValue(2, userId);
		setValue(3, visualScore);
		setValue(4, smellScore);
		setValue(5, tasteScore);
		setValue(6, feelScore);
		setValue(7, text);
		setValue(8, postTime);
	}
}