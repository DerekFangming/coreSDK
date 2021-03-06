package com.fmning.service.manager;

import java.time.Instant;
import java.util.List;

import com.fmning.service.domain.Feed;
import com.fmning.service.exceptions.NotFoundException;

public interface FeedManager {
	
	/**
	 * Create a feed and save into db
	 * @param title the feed title
	 * @param type the type of the feed
	 * @param body the feed body of this 
	 * @param ownerId the user id of the owner of this feed
	 * @param updatedBy the user who updated the article
	 * @return the id of this feed
	 */
	public int createFeed(String title, String type, String body, int ownerId);
	public int createFeed(String title, String type, String body, int ownerId, int updatedBy);
	
	/**
	 * Get feed object from feed id
	 * @param feedId the DB ID of a feed
	 * @return a feed object
	 * @throws NotFoundException if the feed does not exist
	 */
	public Feed getFeedById(int feedId) throws NotFoundException;
	
	/**
	 * Disable a feed
	 * @param feedId the Id of the feed object
	 * @param ownserId the owner id of the feed
	 * @param updatedBy the user who updated the article
	 * @throws NotFoundException if the feed does not exist
	 * @throws IllegalStateException if the user is not the owner of the image
	 */
	public void softDeleteFeed(int feedId, int ownerId) throws NotFoundException, IllegalStateException;
	public void softDeleteFeed(int feedId, int ownerId, int updatedBy) throws NotFoundException, IllegalStateException;
	
	/**
	 * Update feed if input value is not null
	 * @param feedId the Id of the feed
	 * @param title new title if not null
	 * @param body new body if not null
	 * @param type new type if not null
	 * @param updatedBy the user who updates this article
	 * @throws NotFoundException if the article is not found by id
	 */
	public void softUpdateFeed(int feedId, String title, String body, String type, int updatedBy) throws NotFoundException;
	
	/**
	 * Search feeds by type and & or keyword. If they are null, skip them
	 * @param type the feed type
	 * @param keyword keyword of the feed
	 * @return list of feed matching the search cretira
	 * @throws NotFoundException if no feed found matching the search
	 */
	public List<Feed> searchFeed(String type, String keyword) throws NotFoundException;
	
	/**
	 * Get a list of most recent feed by the provided date. The maximum number of feed returned
	 * is smaller or equal to the limit
	 * All feeds are posted by the given user
	 * @param date the date for the most recent line
	 * @param limit the maximum of feed that will be returned at once
	 * @return a list of feeds that meet the criteria
	 * @throws NotFoundException if no feed meets the criteria
	 */
	public List<Feed> getRecentFeedByDate (Instant date, int limit) throws NotFoundException;
	
	/**
	 * Get a list of most recent feed by page index. The maximum number of feed returned
	 * is smaller or equal to the limit.
	 * @param index the page index, starts from 0
	 * @param limit number of feeds per page
	 * @return a list of feeds that meet the criteria
	 * @throws NotFoundException
	 */
	public List<Feed> getRecentFeedByPageIndex(int index, int limit) throws NotFoundException;
	
	/**
	 * Get a list of most recent feed by creator id. The maximum number of feed returned
	 * is smaller or equal to the limit.
	 * @param index the page index, starts from 0
	 * @param limit number of feeds per page
	 * @return a list of feeds that meet the criteria
	 * @throws NotFoundException
	 */
	public List<Feed> getRecentFeedByCreator(int ownerId, int limit) throws NotFoundException;
	
	/**
	 * Get the number of all enabled feeds in database
	 * @return the count of all feeds
	 */
	public int getFeedCount();
	
	/**
	 * Get feed preview image Ids. Will return 4 of them maximumly.
	 * Ids are ordered by time.
	 * All feeds are posted by the given user
	 * @param ownerId the user that the feed images come from
	 * @return a list of ids. If nothing, return empty list
	 */
	public List<Integer> getFeedPreviewImageIdList(int ownerId);
	
	/**
	 * Get a list of most recent feed by the provided date. The maximum number of feed returned
	 * is smaller or equal to the limit
	 * All feeds are posted by the friends of the given user or the user himself
	 * @param userId the given user
	 * @param date the date for the most recent line
	 * @param limit the maximum of feed that will be returned at once
	 * @return a list of feeds that meet the criteria above
	 * @throws NotFoundException if no feed meets the criteria
	 */
	public List<Feed> getRecentFeedFromFriends(int userId, Instant date, int limit) throws NotFoundException;
}
