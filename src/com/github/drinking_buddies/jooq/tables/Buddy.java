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
public class Buddy extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.BuddyRecord> {

	private static final long serialVersionUID = -1615128082;

	/**
	 * The singleton instance of <code>buddy</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.Buddy BUDDY = new com.github.drinking_buddies.jooq.tables.Buddy();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.BuddyRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.BuddyRecord.class;
	}

	/**
	 * The column <code>buddy.user_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BuddyRecord, java.lang.Integer> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>buddy.buddy_id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.BuddyRecord, java.lang.Integer> BUDDY_ID = createField("buddy_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * Create a <code>buddy</code> table reference
	 */
	public Buddy() {
		super("buddy", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>buddy</code> table reference
	 */
	public Buddy(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.Buddy.BUDDY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.BuddyRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.github.drinking_buddies.jooq.tables.records.BuddyRecord, ?>>asList(com.github.drinking_buddies.jooq.Keys.FK_BUDDY_USER_2, com.github.drinking_buddies.jooq.Keys.FK_BUDDY_USER_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.Buddy as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.Buddy(alias);
	}
}
