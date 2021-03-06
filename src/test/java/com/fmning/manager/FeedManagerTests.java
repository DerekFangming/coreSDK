package com.fmning.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fmning.service.domain.Feed;
import com.fmning.service.manager.FeedManager;
import com.fmning.util.ErrorMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/sdkUnitTesting.xml")
public class FeedManagerTests {
	
	@Autowired private FeedManager feedManager;
	
	@BeforeClass
    public static void setUpBaseClass() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
	
	@Test
	public void testGetFeed(){
		Feed feed = feedManager.getFeedById(3);
		assertEquals(feed.getOwnerId(), 4);
	}
	
	@Test
	public void testGetFeed2(){
		Feed feed = feedManager.getFeedById(4);
		assertEquals(feed.getOwnerId(), 4);
	}
	
	@Test
	public void testPersistFeed() {
		try {
			feedManager.createFeed("title", "very very long type that will cause exception", "the body", 1);
			fail(ErrorMessage.SHOULD_NOT_PASS_ERROR.getMsg());
		} catch (Exception e) {
			assertEquals(e.getClass().toString(), "class org.springframework.dao.DataIntegrityViolationException");
		}
	}
	
	@Test
	public void testSearchFeed(){
		List<Feed> feedList = feedManager.searchFeed("Trade", null);
		assertEquals(feedList.size() > 1, true);
	}
	
	@Test
	public void testSearchFeed2(){
		List<Feed> feedList = feedManager.searchFeed("Event", "%DRAGON NIGHT%");
		assertEquals(feedList.size(), 1);
	}
	
	@Test
	public void testGetFeedByPageIndex(){
		List<Feed> feedList = feedManager.getRecentFeedByPageIndex(0, 10);
		assertEquals(feedList.size(), 10);
	}
	
	@Test
	public void testGetFeedCount(){
		int count = feedManager.getFeedCount();
		Assert.assertTrue(count > 10);
	}

}
