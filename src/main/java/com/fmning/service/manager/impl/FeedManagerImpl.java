package com.fmning.service.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fmning.service.dao.ImageDao;
import com.fmning.service.dao.RelationshipDao;
import com.fmning.service.dao.FeedDao;
import com.fmning.service.dao.impl.CoreTableType;
import com.fmning.service.dao.impl.InnerQueryTerm;
import com.fmning.service.dao.impl.LogicalOpType;
import com.fmning.service.dao.impl.NVPair;
import com.fmning.service.dao.impl.QueryBuilder;
import com.fmning.service.dao.impl.QueryTerm;
import com.fmning.service.dao.impl.QueryType;
import com.fmning.service.dao.impl.RelationalOpType;
import com.fmning.service.dao.impl.ResultsOrderType;
import com.fmning.service.domain.Feed;
import com.fmning.service.exceptions.NotFoundException;
import com.fmning.service.manager.FeedManager;
import com.fmning.util.ErrorMessage;
import com.fmning.util.ImageType;
import com.fmning.util.Util;

@Component
public class FeedManagerImpl implements FeedManager{

	@Autowired private FeedDao feedDao;

	@Override
	public int createFeed(String title, String type, String body, int ownerId){
		
		return createFeed(title, type, body, ownerId, Util.nullInt);
	}
	
	@Override
	public int createFeed(String title, String type, String body, int ownerId, int updatedBy){
		
		Feed feed = new Feed();
		feed.setTitle(title);
		feed.setType(type);
		feed.setBody(body);
		feed.setEnabled(true);
		feed.setOwnerId(ownerId);
		feed.setCreatedAt(Instant.now());
		feed.setUpdatedBy(updatedBy);
		
		return feedDao.persist(feed);
	}
	
	@Override
	public Feed getFeedById(int feedId) throws NotFoundException {
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(FeedDao.Field.ID.getQueryTerm(feedId));
		values.add(FeedDao.Field.ENABLED.getQueryTerm(true));
		
		try{
			return feedDao.findObject(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.FEED_NOT_FOUND.getMsg());
		}
	}
	
	@Override
	public void softDeleteFeed(int feedId, int ownerId) throws NotFoundException, IllegalStateException{
		softDeleteFeed(feedId, ownerId, Util.nullInt);
	}
	
	@Override
	public void softDeleteFeed(int feedId, int ownerId, int updatedBy) throws NotFoundException, IllegalStateException {
		QueryTerm value = FeedDao.Field.ID.getQueryTerm(feedId);
		Feed feed;
		try{
			feed = feedDao.findObject(value);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.NO_FEED_TO_DELETE.getMsg());
		}
		
		if(feed.getOwnerId() != ownerId)
			throw new IllegalStateException(ErrorMessage.UNAUTHORIZED_FEED_DELETE.getMsg());
		List<NVPair> newValues = new ArrayList<>();
		newValues.add(new NVPair(FeedDao.Field.ENABLED.name, false));
		if (updatedBy != Util.nullInt) {
			newValues.add(new NVPair(FeedDao.Field.UPDATED_BY.name, updatedBy));
		}
		feedDao.update(feed.getId(), newValues);
	}
	
	@Override
	public void softUpdateFeed(int feedId, String title, String body, String type, int updatedBy) throws NotFoundException {
		Feed feed = getFeedById(feedId);
		
		List<NVPair> newValues = new ArrayList<>();
		if (title != null) {
			newValues.add(new NVPair(FeedDao.Field.TITLE.name, title));
		}
		
		if (body != null) {
			newValues.add(new NVPair(FeedDao.Field.BODY.name, body));
		}
		
		if (type != null) {
			newValues.add(new NVPair(FeedDao.Field.TYPE.name, type));
		}
		
		newValues.add(new NVPair(FeedDao.Field.UPDATED_BY.name, updatedBy));
		
		feedDao.update(feed.getId(), newValues);
	}
	
	@Override
	public List<Feed> searchFeed(String type, String keyword) throws NotFoundException {
		boolean addFirst = true;
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.FEEDS, QueryType.FIND);
		
		if (type != null) {
			if (addFirst) {
				addFirst = false;
				qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.TYPE.name, RelationalOpType.EQ, type));
			}
		}
		
		if(keyword != null) {
			keyword = "%" + keyword.toUpperCase() + "%";
			if (addFirst) {
				addFirst = false;
				qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.BODY.name, RelationalOpType.LIKE, keyword));
				qb.addNextQueryExpression(LogicalOpType.AND,
						new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
				qb.addNextQueryExpression(LogicalOpType.OR,
						new QueryTerm(FeedDao.Field.TITLE.name, RelationalOpType.LIKE, keyword));
				qb.addNextQueryExpression(LogicalOpType.AND,
						new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
			} else {
				qb.addNextQueryExpression(LogicalOpType.AND, 
						new QueryTerm(FeedDao.Field.BODY.name, RelationalOpType.LIKE, keyword));
				qb.addNextQueryExpression(LogicalOpType.AND,
						new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
				qb.addNextQueryExpression(LogicalOpType.OR,
						new QueryTerm(FeedDao.Field.TYPE.name, RelationalOpType.EQ, type));
				qb.addNextQueryExpression(LogicalOpType.AND,
						new QueryTerm(FeedDao.Field.TITLE.name, RelationalOpType.LIKE, keyword));
				qb.addNextQueryExpression(LogicalOpType.AND,
						new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
			}
		} else {
			if (addFirst) {
				qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
			} else {
				qb.addNextQueryExpression(LogicalOpType.AND,
						new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
			}
		}
		qb.setOrdering(FeedDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    
	    try{
	    		return feedDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    		throw new NotFoundException(ErrorMessage.NO_MORE_FEEDS_FOUND.getMsg());
	    }
	}
	
	@Override
	public List<Feed> getRecentFeedByDate (Instant date, int limit) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.FEEDS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.CREATED_AT.name, RelationalOpType.LT, Date.from(date)));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    qb.setOrdering(FeedDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(limit);
	    
	    try{
	    	return feedDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_FEEDS_FOUND.getMsg());
	    }
	}
	
	@Override
	public List<Feed> getRecentFeedByPageIndex(int index, int limit) throws NotFoundException {
		
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.FEEDS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    qb.setOrdering(FeedDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(limit);
	    qb.setOffset(index * limit);
	    
	    try{
	    	return feedDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_FEEDS_FOUND.getMsg());
	    }
	}
	
	@Override
	public List<Feed> getRecentFeedByCreator(int ownerId, int limit) throws NotFoundException {

		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.FEEDS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.setOrdering(FeedDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(limit);
	    
	    try{
	    	return feedDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_FEEDS_FOUND.getMsg());
	    }
	}
	
	@Override
	public int getFeedCount() {
		return feedDao.getCount(new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	}

	@Override
	public List<Integer> getFeedPreviewImageIdList(int ownerId){
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.IMAGES, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(ImageDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(ImageDao.Field.TYPE.name, RelationalOpType.EQ, ImageType.FEED_COVER.getName()));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    qb.setOrdering(ImageDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(4);
	    
	    try{
	    	return feedDao.findAllIds(qb.createQuery());
	    }catch(NotFoundException e){
	    	return new ArrayList<Integer>();
	    }
	}

	@Override
	public List<Feed> getRecentFeedFromFriends(int userId, Instant date, int limit) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.FEEDS, QueryType.FIND);
		
		QueryBuilder inner = qb.getInnerQueryBuilder(CoreTableType.RELATIONSHIPS, QueryType.FIND);
		inner.addFirstQueryExpression(new QueryTerm(RelationshipDao.Field.SENDER_ID.name, userId));
		inner.setReturnField(RelationshipDao.Field.RECEIVER_ID.name);
		
		qb.addFirstQueryExpression(new InnerQueryTerm(FeedDao.Field.OWNER_ID.name, RelationalOpType.IN, inner));
		qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.CREATED_AT.name, RelationalOpType.LT, Date.from(date)));
		qb.addNextQueryExpression(LogicalOpType.OR, new QueryTerm(FeedDao.Field.OWNER_ID.name, userId));
		qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.CREATED_AT.name, RelationalOpType.LT, Date.from(date)));
		qb.setOrdering(FeedDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
		qb.setLimit(limit);
		try{
	    	return feedDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_FEEDS_FOUND.getMsg());
	    }
	}

}
