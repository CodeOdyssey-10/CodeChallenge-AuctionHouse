
package com.codingchallenge.tm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.codingchallenge.tm.exceptions.InvalidAuctionException;
import com.codingchallenge.tm.exceptions.InvalidBidException;
import com.codingchallenge.tm.models.Auction;
import com.codingchallenge.tm.models.Bid;

/**
 * AuctionHouse services for 
 * handling various events in the incoming 
 * auction messages
 * @author hiran
 *
 */
public interface AuctionService {
	
	/**
	 * Create Auction for SELL order
	 * @param auction
	 * @return
	 */
	void createAuction(Auction auction) throws InvalidAuctionException;
	
	/**
	 * Place bid for item for BID order
	 * @param bid
	 * @return
	 */
	boolean placeBidForItem(Bid bid) throws InvalidBidException;
	
	/**
	 * Fetch all bids on an item
	 * @param item
	 * @return
	 */
	public List<Bid> getBidsByItem(String item);
	
	/**
	 * Fetch current auction registry containing 
	 * all auction and all valid bid info 
	 * @return
	 */
	public Map<String, Auction> getCurrentAuctionRegistry();
	
	/**
	 * Process the Heartbeat message for auction closing time
	 * @param timestamp
	 */
	public void processCloseTimeHeartBeatMessage(Integer timestamp);
	
	/**
	 * Fetch the Winning bid information after auction closing
	 * @return
	 */
	public LinkedHashMap<String,Auction> getWinningBid();
}
