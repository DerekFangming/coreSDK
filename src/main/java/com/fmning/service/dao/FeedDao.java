package com.fmning.service.dao;

import java.util.Arrays;
import java.util.List;

import com.fmning.service.dao.impl.QueryTerm;
import com.fmning.service.dao.impl.RelationalOpType;
import com.fmning.service.domain.Feed;
import com.fmning.util.Pair;

public interface FeedDao extends CommonDao<Feed>{
	enum Field implements DaoFieldEnum{
		ID(true),
		TITLE,
		TYPE,
		BODY,
		OWNER_ID,
		ENABLED,
		CREATED_AT,
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
		new Pair<Enum<?>, String>(Field.TITLE, "TEXT"),
		new Pair<Enum<?>, String>(Field.TYPE, "TEXT"),
		new Pair<Enum<?>, String>(Field.BODY, "TEXT"),
		new Pair<Enum<?>, String>(Field.OWNER_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.ENABLED, "BOOLEAN NOT NULL"),
		new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"),
		new Pair<Enum<?>, String>(Field.UPDATED_BY, "INTEGER"));

}
