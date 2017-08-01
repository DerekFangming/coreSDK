package com.fmning.service.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.fmning.service.dao.CommentDao;
import com.fmning.service.dao.DataSourceRegistry;
import com.fmning.service.dao.DataSourceType;
import com.fmning.service.dao.FriendDao;
import com.fmning.service.dao.ImageDao;
import com.fmning.service.dao.FeedDao;
import com.fmning.service.dao.RelationshipDao;
import com.fmning.service.dao.SchemaTable;
import com.fmning.service.dao.SdkDataSource;
import com.fmning.service.dao.SgDao;
import com.fmning.service.dao.WcReportDao;
import com.fmning.service.dao.UserDao;
import com.fmning.service.dao.UserDetailDao;
import com.fmning.service.dao.WcAppVersionDao;
import com.fmning.service.dao.WcArticleDao;
import com.fmning.util.Pair;


public enum CoreTableType implements SchemaTable
{
	USERS(CoreDataSourceType.CORE, UserDao.FieldTypes),
	USER_DETAILS(CoreDataSourceType.CORE, UserDetailDao.FieldTypes),
	IMAGES(CoreDataSourceType.CORE, ImageDao.FieldTypes),
	FRIENDS(CoreDataSourceType.CORE, FriendDao.FieldTypes),
	RELATIONSHIPS(CoreDataSourceType.CORE, RelationshipDao.FieldTypes),
	FEEDS(CoreDataSourceType.CORE, FeedDao.FieldTypes),
	COMMENTS(CoreDataSourceType.CORE, CommentDao.FieldTypes),
	SG(CoreDataSourceType.CORE, SgDao.FieldTypes),
	WC_REPORTS(CoreDataSourceType.CORE, WcReportDao.FieldTypes),
	WC_APP_VERSIONS(CoreDataSourceType.CORE, WcAppVersionDao.FieldTypes),
	WC_ARTICLES(CoreDataSourceType.CORE, WcArticleDao.FieldTypes)
;


  private DataSourceType dsType;
  private SdkDataSource dataSource; 
  private String tableName;
  private List<Pair<Enum<?>, String>> columnDefns;
  private List<String> columnNames = new ArrayList<String>();

  private String pkName;
  private Enum<?>[] types;

  CoreTableType(DataSourceType dbType, List<Pair<Enum<?>, String>> columnDefns)
  {
    this.dsType = dbType;
    this.columnDefns = columnDefns;
    this.types = new Enum<?>[0];
    
    this.tableName = this.name().toLowerCase();
    this.columnNames = SchemaTable.getColumnNames(this.columnDefns);
    this.pkName = SchemaTable.getPkName(this.columnDefns);
  }

  CoreTableType(DataSourceType dbType, List<Pair<Enum<?>, String>> columnDefns, Enum<?>... types)
  {
    this(dbType, columnDefns);
    this.types = types;
  }

  @Override
  public void init(DataSourceRegistry dsr)
  {
    this.dataSource = dsr.getDataSource(this.dsType.getNickname());

    // Keep this at the end so that "this" is fully populated before adding it.
    // In particular addTable() requires that this.tableName have been set
    this.dataSource.addTable(this);
  }

  /**
   * @return the columnDefns
   */
  @Override
  public List<Pair<Enum<?>, String>> getColumnDefns( )
  {
    return columnDefns;
  }

  @Override
  public Enum<?>[] getTypes( )
  {
    return this.types;
  }

  /**
   * @return the data source type
   */
  @Override
  public DataSourceType getDataSourceType( )
  {
    return dsType;
  }

  @Override
  public SdkDataSource getDataSource( )
  {
    return this.dataSource;
  }
  
  @Override
  public String getTableName( )
  {
    return this.tableName;
  }

  @Override
  public String getPrimaryKeyName( )
  {
    return this.pkName;
  }

  /**
   * Returns a list of the table's field (aka column) names. This include the primary key name.
   */
  @Override
  public List<String> getColumnNames( )
  {
    return this.columnNames;
  }
}