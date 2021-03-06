package com.fmning.service.dao;

import java.util.Arrays;
import java.util.List;

import com.fmning.service.dao.impl.QueryTerm;
import com.fmning.service.dao.impl.RelationalOpType;
import com.fmning.service.domain.SurvivalGuideHist;
import com.fmning.util.Pair;

public interface SurvivalGuideHistDao extends CommonDao<SurvivalGuideHist>{
	enum Field implements DaoFieldEnum{
		ID(true),
		OID,
		TITLE,
		CONTENT,
		PARENT_ID,
		POSITION,
		CREATED_AT,
		OWNER_ID,
		ACTION,
		ACTION_DATE;

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
		new Pair<Enum<?>, String>(Field.OID, "INTEGER"),
		new Pair<Enum<?>, String>(Field.TITLE, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.CONTENT, "TEXT"),
		new Pair<Enum<?>, String>(Field.PARENT_ID, "INTEGER"),
		new Pair<Enum<?>, String>(Field.POSITION, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW() DEFAULT NOW()"),
		new Pair<Enum<?>, String>(Field.OWNER_ID, "INTEGER NOT NULL DEFAULT 0"),
		new Pair<Enum<?>, String>(Field.ACTION, "TEXT NOT NULL DEFAULT 'U'"),
		new Pair<Enum<?>, String>(Field.ACTION_DATE, "TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW() DEFAULT NOW()"));

}
