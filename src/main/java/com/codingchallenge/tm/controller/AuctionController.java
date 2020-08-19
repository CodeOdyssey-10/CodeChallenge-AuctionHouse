package com.codingchallenge.tm.controller;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.codingchallenge.tm.enums.AuctionActionsEnum;
import com.codingchallenge.tm.exceptions.InvalidAuctionException;
import com.codingchallenge.tm.exceptions.InvalidBidException;
import com.codingchallenge.tm.helper.AuctionHelper;
import com.codingchallenge.tm.models.Auction;
import com.codingchallenge.tm.models.Bid;
import com.codingchallenge.tm.service.AuctionServiceImpl;

/**
 * @Description AuctionController decides the action 
 * for the incoming auction messages
 * event/action=SELL - New Auction will be created for the item
 * event/action=BID  - New Bid will be placed against Item Auction
 * event/heartbeat   - Auction close decider
 * Note:Only if valid auction exists bid will be placed
 * Assumptions:action field is mandatory and positions of fields are fixed
 * 
 *
 */
@Controller
public class AuctionController {
 
	@Autowired
	public AuctionServiceImpl auctionService;
	
	@Autowired
	public AuctionHelper auctionHelper;
	
	public static final String DELIMITER="|";
	
	/**Dispatcher function for all incoming auction messages*/
	public void processAuction(String auctionInputMessage){
		
		String[] arrInputAuctionMessageFields = AuctionHelper.getDelimitedInputAuctionMessage(auctionInputMessage,DELIMITER);
		try {
			if(null!=arrInputAuctionMessageFields && 
					 arrInputAuctionMessageFields.length>=3) {
			     String action =  arrInputAuctionMessageFields[2];
			    //SELL Event - Create New Auction
				if(AuctionActionsEnum.SELL.toString().equalsIgnoreCase(action)) {
					Auction auction = new Auction(Integer.valueOf(arrInputAuctionMessageFields[1]), arrInputAuctionMessageFields[3], arrInputAuctionMessageFields[2], Integer.valueOf(arrInputAuctionMessageFields[0]), Integer.valueOf(arrInputAuctionMessageFields[5]), new BigDecimal(arrInputAuctionMessageFields[4]),true);
					auctionService.createAuction(auction);
				}
				//BID Event - Place Bid for Item
				else if(AuctionActionsEnum.BID.toString().equalsIgnoreCase(action)) {
					Bid bid = new Bid(Integer.valueOf(arrInputAuctionMessageFields[0]),Integer.valueOf(arrInputAuctionMessageFields[1]),arrInputAuctionMessageFields[2],arrInputAuctionMessageFields[3],new BigDecimal(arrInputAuctionMessageFields[4]));
					auctionService.placeBidForItem(bid);
				}
			}else {
				//HeartBeat Event - Decide Auction close
				if(null!=arrInputAuctionMessageFields && 
						 arrInputAuctionMessageFields.length==1) {
					auctionService.processCloseTimeHeartBeatMessage(Integer.valueOf(arrInputAuctionMessageFields[0]));
				}
			}
		}catch (InvalidAuctionException |InvalidBidException e) {
				System.out.println("Exception Caught:"+e.getMessage());
		}
	}
	
	/**
	 * Fetch all bids on an Item
	 * @param item
	 * @return
	 */
	public List<Bid> getBidsByItem(String item){
		return auctionService.getBidsByItem(item);
	}
	
	/**
	 * Fetch Current Auction Registry with all auctions
	 * and list of all valid Bids against auction/item
	 * @return
	 */
	public Map<String, Auction> getCurrentAuctionRegistry() {
		return auctionService.getCurrentAuctionRegistry();
	}
	
	/**
	 * Fetch the final winning bid details
	 * after auction closing
	 * @return
	 */
	public LinkedHashMap<String,Auction> getWinningBid(){
		return auctionService.getWinningBid();
	}
	
	/**
	 * Write the Winning Bid Details to
	 * output file
	 */
	public void writeToOutputFile() {
		auctionHelper.writeToFile(getWinningBid());
	}
}
