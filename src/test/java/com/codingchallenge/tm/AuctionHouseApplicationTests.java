package com.codingchallenge.tm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;
import com.codingchallenge.tm.controller.AuctionController;
import com.codingchallenge.tm.helper.AuctionHelper;
import com.codingchallenge.tm.models.Auction;
import com.codingchallenge.tm.service.AuctionServiceImpl;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AuctionController.class,AuctionServiceImpl.class,AuctionHelper.class})
class AuctionHouseApplicationTests {

	@Autowired
	private AuctionController auctionController;
	
	private BufferedReader in = null;
	
	
	/**Check if input.txt file exists in classpath*/
	@Test
	@DisplayName("InputFileExistsCheck")
    public void checkInputFileExists()
        throws IOException
    {
       Assert.notNull(in);
    }
	
	@Test
	@DisplayName("Users listing items for sale")
	public void shouldCreateAuctionForSellOrder()throws IOException {
		  String auctionOrderMessage;
			while((auctionOrderMessage=in.readLine())!=null) {
				if(auctionOrderMessage.indexOf("SELL")!=-1) {
					auctionController.processAuction(auctionOrderMessage);
					assertNotNull(auctionController.getCurrentAuctionRegistry());
				}
			}
		}
	
	@Test
	@DisplayName("Bids on items")
	public void shouldPlaceItemBidForValidBidOrder()throws IOException {
		  String auctionOrderMessage;
			while((auctionOrderMessage=in.readLine())!=null) {
				auctionController.processAuction(auctionOrderMessage);
			}
			Map<String, Auction> currentAuctionRegistry=auctionController.getCurrentAuctionRegistry();
			assertEquals(3,((Auction)currentAuctionRegistry.get("toaster_1")).getListOfBids().size());
		}
	
	@Test
	@DisplayName("Check Auction Close on HeartBeat message equals closetime")
	public void shouldCloseAuctionCloseTimeHeartTimeReceival()throws IOException {
		  String auctionOrderMessage;
			while((auctionOrderMessage=in.readLine())!=null) {
				auctionController.processAuction(auctionOrderMessage);
			}
			Map<String, Auction> currentAuctionRegistry=auctionController.getCurrentAuctionRegistry();
			assertFalse(((Auction)currentAuctionRegistry.get("toaster_1")).isAuctionOpen());
		}
	
	@Test
	@DisplayName("Check Winning Bid")
	public void shouldGiveHighestBidAsWinningBid()throws IOException {
		  String auctionOrderMessage;
			while((auctionOrderMessage=in.readLine())!=null) {
				auctionController.processAuction(auctionOrderMessage);
			}
			LinkedHashMap<String, Auction> winningBid=auctionController.getWinningBid();
			assertEquals(8, winningBid.get("toaster_1").getUserid());
		}
	
	@Test
	@DisplayName("Check pricepaid for UNSOLD Bid")
	public void shouldPricePaidAsZeroForUnsoldBid()throws IOException {
		  String auctionOrderMessage;
			while((auctionOrderMessage=in.readLine())!=null) {
				auctionController.processAuction(auctionOrderMessage);
			}
			LinkedHashMap<String, Auction> winningBid=auctionController.getWinningBid();
			assertEquals(new BigDecimal("0.00"), winningBid.get("tv_1").getPricePaid());
		}
	
	/**Instantiate Input.txt Reader*/
	@BeforeAll
	public void setUp() throws IOException{
		in = new BufferedReader(
	            new InputStreamReader(getClass().getResourceAsStream("/input.txt")));
	}
	
	/**Close the Stream Reader*/
	@AfterAll
	public void tearDown() throws IOException{
		 if (in != null)
	        {
	            in.close();
	        }
	        in = null;
	}
}
