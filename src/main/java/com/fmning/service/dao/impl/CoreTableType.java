package com.fmning.service.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.fmning.service.dao.CommentDao;
import com.fmning.service.dao.DataSourceRegistry;
import com.fmning.service.dao.EditingQueueDao;
import com.fmning.service.dao.ErrorLogDao;
import com.fmning.service.dao.EventDao;
import com.fmning.service.dao.FriendDao;
import com.fmning.service.dao.ImageDao;
import com.fmning.service.dao.PaymentDao;
import com.fmning.service.dao.FeedDao;
import com.fmning.service.dao.RelationshipDao;
import com.fmning.service.dao.SchemaTable;
import com.fmning.service.dao.SdkDataSource;
import com.fmning.service.dao.SdkDataSourceType;
import com.fmning.service.dao.SurvivalGuideDao;
import com.fmning.service.dao.SurvivalGuideHistDao;
import com.fmning.service.dao.TicketDao;
import com.fmning.service.dao.TicketTemplateDao;
import com.fmning.service.dao.WcReportDao;
import com.fmning.service.temp.CmainDao;
import com.fmning.service.temp.EcgDao;
import com.fmning.service.temp.PpgDao;
import com.fmning.service.dao.UserDao;
import com.fmning.service.dao.UserDetailDao;
import com.fmning.service.dao.WcAppVersionDao;
import com.fmning.service.dao.WcArticleDao;
import com.fmning.util.Pair;


public enum CoreTableType implements SchemaTable
{
	USERS(SdkDataSourceType.CORE, UserDao.FieldTypes),
	USER_DETAILS(SdkDataSourceType.CORE, UserDetailDao.FieldTypes),
	IMAGES(SdkDataSourceType.CORE, ImageDao.FieldTypes),
	FRIENDS(SdkDataSourceType.CORE, FriendDao.FieldTypes),
	RELATIONSHIPS(SdkDataSourceType.CORE, RelationshipDao.FieldTypes),
	FEEDS(SdkDataSourceType.CORE, FeedDao.FieldTypes),
	EVENTS(SdkDataSourceType.CORE, EventDao.FieldTypes),
	COMMENTS(SdkDataSourceType.CORE, CommentDao.FieldTypes),
	TICKETS(SdkDataSourceType.CORE, TicketDao.FieldTypes),
	TICKET_TEMPLATES(SdkDataSourceType.CORE, TicketTemplateDao.FieldTypes),
	PAYMENTS(SdkDataSourceType.CORE, PaymentDao.FieldTypes),
	ERROR_LOGS(SdkDataSourceType.CORE, ErrorLogDao.FieldTypes),

	SURVIVAL_GUIDES(SdkDataSourceType.CORE, SurvivalGuideDao.FieldTypes),
	SURVIVAL_GUIDE_HISTS(SdkDataSourceType.CORE, SurvivalGuideHistDao.FieldTypes),
	WC_REPORTS(SdkDataSourceType.CORE, WcReportDao.FieldTypes),
	WC_APP_VERSIONS(SdkDataSourceType.CORE, WcAppVersionDao.FieldTypes),
	WC_ARTICLES(SdkDataSourceType.CORE, WcArticleDao.FieldTypes),
	EDITING_QUEUES(SdkDataSourceType.CORE, EditingQueueDao.FieldTypes),
	
	//TEMPS
	CMAIN(SdkDataSourceType.CORE, CmainDao.FieldTypes),
	PPG(SdkDataSourceType.CORE, PpgDao.FieldTypes),
	ECG(SdkDataSourceType.CORE, EcgDao.FieldTypes),
	;


	private String dsNickname;
	  private SdkDataSource dataSource; 
	  private String tableName;
	  private List<Pair<Enum<?>, String>> columnDefns;
	  private List<String> columnNames = new ArrayList<String>();
	  private boolean isExactFieldCountRequired = true;
	  
	  private String pkName;
	  private Enum<?>[] types;

	  CoreTableType(SdkDataSourceType dsType, List<Pair<Enum<?>, String>> columnDefns)
	  {
	    this.dsNickname = dsType.getNickname();
	    this.columnDefns = columnDefns;
	    this.types = new Enum<?>[0];
	    
	    this.tableName = this.name().toLowerCase();
	    this.columnNames = SchemaTable.getColumnNames(this.columnDefns);
	    this.pkName = SchemaTable.getPkName(this.columnDefns);
	  }

	  CoreTableType(SdkDataSourceType dsType, List<Pair<Enum<?>, String>> columnDefns, boolean isExactFieldCountRequired)
	  {
	   this(dsType, columnDefns);
	   this.isExactFieldCountRequired = isExactFieldCountRequired;
	  }
	  
	  CoreTableType(SdkDataSourceType dsType, List<Pair<Enum<?>, String>> columnDefns, Enum<?>... types)
	  {
	    this(dsType, columnDefns);
	    this.types = types;
	  }

	  @Override
	  public void init(DataSourceRegistry dsr)
	  {
	    this.dataSource = dsr.getDataSource(this.dsNickname);

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
	  public String getDataSourceType( )
	  {
	    return dsNickname;
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

	  @Override
	  public boolean isExactFieldCountRequired( )
	  {    
	    return this.isExactFieldCountRequired;
	  }
	  
	//  @Component
	//  public static class DSInjector
	//  { 
//	    private static String CLASS_NAME = DSInjector.class.getSimpleName();
	//    
//	    @Autowired DataSourceRegistry dsr;
	//    
//	    DSInjector()
//	    {
//	      System.out.println(CLASS_NAME + " constructor");
//	    }
	//    
//	    @PostConstruct
//	    void init()
//	    {
//	      for(CoreTableType tt : EnumSet.allOf(CoreTableType.class))
//	      {
//	        tt.init(dsr);
//	      }
//	      System.out.println("database count: " + dsr.getDatabaseCount());
//	    }    
	//  }  

	}