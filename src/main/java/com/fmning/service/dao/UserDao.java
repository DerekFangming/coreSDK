package com.fmning.service.dao;

import java.util.Arrays;
import java.util.List;

import com.fmning.service.dao.impl.QueryTerm;
import com.fmning.service.dao.impl.RelationalOpType;
import com.fmning.service.domain.User;
import com.fmning.util.Pair;

public interface UserDao extends CommonDao<User>{
	enum Field implements DaoFieldEnum{
		ID(true),
		USERNAME,
		PASSWORD,
		ACCESS_TOKEN,
		VERI_TOKEN,
		CREATED_AT,
		EMAIL_CONFIRMED,
		SALT,
		ROLE_ID,
		UPDATED_BY;

		public boolean isPK = false;
		public String name;

		Field(boolean isPK) {
			this.isPK = isPK;
			this.name = this.name().toLowerCase();
		}

		Field() {
			this(false);
		}

		@Override
		public QueryTerm getQueryTerm(Object value) {
			return new QueryTerm(this.name, value);
		}

		@Override
		public QueryTerm getQueryTerm(RelationalOpType op, Object value) {
			return new QueryTerm(this.name, op, value);
		}
	}

	List<Pair<Enum<?>, String>> FieldTypes = Arrays.asList(
		new Pair<Enum<?>, String>(Field.ID, "SERIAL NOT NULL"),
		new Pair<Enum<?>, String>(Field.USERNAME, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.PASSWORD, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.ACCESS_TOKEN, "TEXT"),
		new Pair<Enum<?>, String>(Field.VERI_TOKEN, "TEXT"),
		new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"),
		new Pair<Enum<?>, String>(Field.EMAIL_CONFIRMED, "BOOLEAN NOT NULL"),
		new Pair<Enum<?>, String>(Field.SALT, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.ROLE_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.UPDATED_BY, "INTEGER"));

}
