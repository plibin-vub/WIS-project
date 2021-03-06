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
public class Bar2BarScore extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.Bar2BarScoreRecord> {

	private static final long serialVersionUID = -646505434;

	/**
	 * The singleton instance of <code>bar2_bar_score</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.Bar2BarScore BAR2_BAR_SCORE = new com.github.drinking_buddies.jooq.tables.Bar2BarScore();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.Bar2BarScoreRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.Bar2BarScoreRecord.class;
	}

	/**
	 * The column <code>bar2_bar_score.bar_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.Bar2BarScoreRecord, java.lang.Integer> BAR_ID = createField("bar_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>bar2_bar_score.bar_score_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.Bar2BarScoreRecord, java.lang.Integer> BAR_SCORE_ID = createField("bar_score_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * Create a <code>bar2_bar_score</code> table reference
	 */
	public Bar2BarScore() {
		super("bar2_bar_score", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>bar2_bar_score</code> table reference
	 */
	public Bar2BarScore(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.Bar2BarScore.BAR2_BAR_SCORE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.Bar2BarScoreRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.Bar2BarScoreRecord, ?>>asList(com.github.drinking_buddies.jooq.Keys.FK_BAR2_BAR_SCORE_BAR_1, com.github.drinking_buddies.jooq.Keys.FK_BAR2_BAR_SCORE_BAR_SCORE_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.Bar2BarScore as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.Bar2BarScore(alias);
	}
}
