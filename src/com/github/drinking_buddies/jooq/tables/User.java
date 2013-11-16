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
public class User extends org.jooq.impl.TableImpl<com.github.drinking_buddies.jooq.tables.records.UserRecord> {

	private static final long serialVersionUID = -759683771;

	/**
	 * The singleton instance of <code>user</code>
	 */
	public static final com.github.drinking_buddies.jooq.tables.User USER = new com.github.drinking_buddies.jooq.tables.User();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.github.drinking_buddies.jooq.tables.records.UserRecord> getRecordType() {
		return com.github.drinking_buddies.jooq.tables.records.UserRecord.class;
	}

	/**
	 * The column <code>user.id</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>user.name</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this);

	/**
	 * The column <code>user.facebook_name</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.String> FACEBOOK_NAME = createField("facebook_name", org.jooq.impl.SQLDataType.CLOB, this);

	/**
	 * The column <code>user.email</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.String> EMAIL = createField("email", org.jooq.impl.SQLDataType.CLOB, this);

	/**
	 * The column <code>user.city</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.String> CITY = createField("city", org.jooq.impl.SQLDataType.CLOB, this);

	/**
	 * The column <code>user.country</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.String> COUNTRY = createField("country", org.jooq.impl.SQLDataType.CLOB, this);

	/**
	 * The column <code>user.phone</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.Integer> PHONE = createField("phone", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>user.location_x</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.Float> LOCATION_X = createField("location_x", org.jooq.impl.SQLDataType.REAL, this);

	/**
	 * The column <code>user.location_y</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.Float> LOCATION_Y = createField("location_y", org.jooq.impl.SQLDataType.REAL, this);

	/**
	 * The column <code>user.birthdate</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, java.lang.String> BIRTHDATE = createField("birthdate", org.jooq.impl.SQLDataType.CHAR.length(8).nullable(false), this);

	/**
	 * The column <code>user.picture</code>. 
	 */
	public final org.jooq.TableField<com.github.drinking_buddies.jooq.tables.records.UserRecord, byte[]> PICTURE = createField("picture", org.jooq.impl.SQLDataType.BLOB, this);

	/**
	 * Create a <code>user</code> table reference
	 */
	public User() {
		super("user", com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>user</code> table reference
	 */
	public User(java.lang.String alias) {
		super(alias, com.github.drinking_buddies.jooq.DefaultSchema.DEFAULT_SCHEMA, com.github.drinking_buddies.jooq.tables.User.USER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.UserRecord> getPrimaryKey() {
		return com.github.drinking_buddies.jooq.Keys.PK_USER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.UserRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.github.drinking_buddies.jooq.tables.records.UserRecord>>asList(com.github.drinking_buddies.jooq.Keys.PK_USER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.github.drinking_buddies.jooq.tables.User as(java.lang.String alias) {
		return new com.github.drinking_buddies.jooq.tables.User(alias);
	}
}