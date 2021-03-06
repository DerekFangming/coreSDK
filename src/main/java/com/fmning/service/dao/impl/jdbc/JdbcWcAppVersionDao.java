package com.fmning.service.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fmning.service.dao.WcAppVersionDao;
import com.fmning.service.dao.impl.CoreTableType;
import com.fmning.service.dao.impl.NVPairList;
import com.fmning.service.domain.WcAppVersion;

@Repository
@Jdbc
public class JdbcWcAppVersionDao extends JdbcBaseDao<WcAppVersion> implements WcAppVersionDao{
	public JdbcWcAppVersionDao() {
		super(CoreTableType.WC_APP_VERSIONS);
	}

	@Override
	protected NVPairList getNVPairs(WcAppVersion obj) {
		NVPairList params = new NVPairList();

		params.addValue(WcAppVersionDao.Field.APP_VERSION.name, obj.getAppVersion());
		params.addValue(WcAppVersionDao.Field.STATUS.name, obj.getStatus());
		params.addValue(WcAppVersionDao.Field.TITLE.name, obj.getTitle());
		params.addValue(WcAppVersionDao.Field.MESSAGE.name, obj.getMessage());
		params.addValue(WcAppVersionDao.Field.UPDATES.name, obj.getUpdates());

		return params;
	}

	@Override
	protected RowMapper<WcAppVersion> getRowMapper() {
		RowMapper<WcAppVersion> rm = new RowMapper<WcAppVersion>() {
			@Override
			public WcAppVersion mapRow(ResultSet rs, int row) throws SQLException {
				WcAppVersion obj = new WcAppVersion();

				obj.setId(rs.getInt(WcAppVersionDao.Field.ID.name));
				obj.setAppVersion(rs.getString(WcAppVersionDao.Field.APP_VERSION.name));
				obj.setStatus(rs.getString(WcAppVersionDao.Field.STATUS.name));
				obj.setTitle(rs.getString(WcAppVersionDao.Field.TITLE.name));
				obj.setMessage(rs.getString(WcAppVersionDao.Field.MESSAGE.name));
				obj.setUpdates(rs.getString(WcAppVersionDao.Field.UPDATES.name));

				return obj;
			}
		};
		return rm;
	}

}