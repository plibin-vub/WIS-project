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
public class Bar2BarComment extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.Bar2BarCommentRecord> {

	private static final long serialVersionUID = 239918247;

	/**
	 * The singleton instance of <code>bar2_bar_comment</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.Bar2BarComment BAR2_BAR_COMMENT = new com.github.drinking_buddies.jooq.tables.Bar2BarComment();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.Bar2BarCommentRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.Bar2BarCommentRecord.class;
	}

	/**
	 * The column <code>bar2_bar_comment.bar_comment_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.Bar2BarCommentRecord, java.lang.Integer> BAR_COMMENT_ID = createField("bar_comment_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>bar2_bar_comment.bar_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.Bar2BarCommentRecord, java.lang.Integer> BAR_ID = createField("bar_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * Create a <code>bar2_bar_comment</code> table reference
	 */
	public Bar2BarComment() {
		super("bar2_bar_comment", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>bar2_bar_comment</code> table reference
	 */
	public Bar2BarComment(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.Bar2BarComment.BAR2_BAR_COMMENT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.Bar2BarCommentRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.Bar2BarCommentRecord, ?>>asList(com.github.drinking_buddies.jooq.Keys.FK_BAR2_BAR_COMMENT_BAR_COMMENT_1, com.github.drinking_buddies.jooq.Keys.FK_BAR2_BAR_COMMENT_BAR_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.Bar2BarComment as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.Bar2BarComment(alias);
	}
}