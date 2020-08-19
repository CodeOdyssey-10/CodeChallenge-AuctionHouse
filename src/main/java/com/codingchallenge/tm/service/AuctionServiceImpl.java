package com.codingchallenge.tm.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codingchallenge.tm.enums.AuctionStatusEnum;
import com.codingchallenge.tm.exceptions.InvalidAuctionException;
import com.codingchallenge.tm.exceptions.InvalidBidException;
import com.codingchallenge.tm.models.Auction;
import com.codingchallenge.tm.models.Bid;

/**
 * AuctionHouse services implementation
 * @author hiran
 *
 */
@Service
public class AuctionServiceImpl implements AuctionService{
	
	private final Map<String, Auction> auctionRegistry;
	
	public AuctionServiceImpl(){
		auctionRegistry = new LinkedHashMap<>();
	}
	
	@Override
	public synchronized void createAuction(Auction auction) throws InvalidAuctionException{
		auctionRegistry.put(auction.getItem(), auction);
	}
	
	public Map<String, Auction> getCurrentAuctionRegistry() {
	    return new HashMap<>(auctionRegistry);
	  }
	
	public List<Bid> getBidsByItem(String item) {
	    return getCurrentAuctionRegistry().get(item).getListOfBids();
	  }
	
	@Override
	public synchronized boolean placeBidForItem(Bid bid) throws InvalidBidException{
		boolean isBidCreated = false;
		Auction itemAuction  = getCurrentAuctionRegistry().get(bid.getItem());
		
		if(null!=itemAuction) {
			List<Bid> currentBidList=itemAuction.getListOfBids();
			if(isValidBid(bid)) {
				currentBidList.add(bid);
				itemAuction.setTotalBidCount(currentBidList.size());
				itemAuction.setLowestBid(findLowestBid(currentBidList).getBidAmount());
				isBidCreated=true;
			}
		}
		else {
			throw new InvalidBidException(String.format("Invalid Bid:Auction doesn't exists for this item:", bid.getItem()));
		}
		return isBidCreated;
	}

	@Override
	public void processCloseTimeHeartBeatMessage(Integer timestamp) {
		getCurrentAuctionRegistry().forEach((k,v)->{
			if(v.getCloseTime()==timestamp) {
				getCurrentAuctionRegistry().get(k).setAuctionOpen(false);
				}	
			}
		);
	}
	
	/**Valid Bid Rules:
	 * a) Bid should be received withing aution starttime and closetime
	 * b) Bidding amount should be greater than previous highest bid amount
	 * assumptions: -First bid by default will be added without checking against the auction reserve price
	 * ambiguity :Rule :When 2 bids are placed with sameamount(HighestBid) then earliest will be considered winner.
	 * However Validation Rule says new bid amount has to be greater than previous highest bid amount hence bid amount doesn't qualifies
	 * as valid bid*/
	private boolean isValidBid(Bid bid) {
		boolean isValidBidFlag=false;
		Auction itemAuction=getCurrentAuctionRegistry().get(bid.getItem());
		if(bid.getTimeStamp()>itemAuction.getStartTime()
				&& bid.getTimeStamp()<=itemAuction.getCloseTime()) {
			//Highest Bid Amount
			List<Bid> currentBidList=itemAuction.getListOfBids();
			if(null!=currentBidList 
					&& 1<=currentBidList.size()) {
				  	BigDecimal currentHighestBidAmount=findHighestBid(currentBidList).getBidAmount();
				  	if(bid.getBidAmount().compareTo(currentHighestBidAmount)==1) {
						itemAuction.setHighestBid(bid.getBidAmount());
						isValidBidFlag=true;
				}
			}else {
				itemAuction.setHighestBid(bid.getBidAmount());
				itemAuction.setLowestBid(bid.getBidAmount());
				isValidBidFlag=true;
			}
		}
		return isValidBidFlag;
	}
	
	/**
	 * Find the Highest bidding Amount from list of bids on the item
	 * @param currentBidList
	 * @return
	 */
	private Bid findHighestBid(List<Bid> currentBidList) {
		Bid highestBid = currentBidList
	    	      .stream()
	    	      .max(Comparator.comparing(Bid::getBidAmount))
	    	      .orElseThrow(NoSuchElementException::new);
		return highestBid;
	}
	
	/**
	 * Find the Lowest bidding Amount from list of bids on the item
	 * @param currentBidList
	 * @return
	 */
	private Bid findLowestBid(List<Bid> currentBidList) {
		Bid lowestBid = currentBidList
	    	      .stream()
	    	      .min(Comparator.comparing(Bid::getBidAmount))
	    	      .orElseThrow(NoSuchElementException::new);
		return lowestBid;
	}
	
	/**
	 * Fetch the winning bid by getting the
	 * highest bid from the auction registry
	 * at the time auction close
	 * ambiguity:Qualified Bids has to have bid amount>= reserve price
	 * However if this rule is implemented then the in the Expected Output:
	 * 20|tv_1||UNSOLD|0.00|2|200.00|150.00 will not be coming.
	 * Hence ignored.
	 */
	public LinkedHashMap<String,Auction> getWinningBid() {
		Map<String,Auction> auctionRegistry=getCurrentAuctionRegistry();
		LinkedHashMap<String, Auction> winningBidDetailsMap=new LinkedHashMap<>();
		auctionRegistry.forEach((k,v)->{
			Auction auction=auctionRegistry.get(k);
			if(!auction.isAuctionOpen()) {
				BigDecimal highestBidAmount=auction.getHighestBid();
				//BigDecimal auctionReservePrice=auction.getReservePrice();
				List<Bid> listOfBids=auction.getListOfBids();
				listOfBids.forEach(item->{
					//Retrieve the HighestBid details from AuctionRegistry
					if (item.getBidAmount().compareTo(highestBidAmount)==0) {
						Auction winnerBid= new Auction();
						boolean isAuctionSold=highestBidAmount.compareTo(auction.getReservePrice())==1||
								highestBidAmount.compareTo(auction.getReservePrice())==0;
						winnerBid.setCloseTime(auction.getCloseTime());
						winnerBid.setItem(auction.getItem());
						winnerBid.setUserid(isAuctionSold?item.getUserId():null);
						winnerBid.setStatus(isAuctionSold?AuctionStatusEnum.SOLD.toString():AuctionStatusEnum.UNSOLD.toString());
						winnerBid.setPricePaid(isAuctionSold?calculatePricePaid(listOfBids.stream()
								  .collect(Collectors.toList()),auction):new BigDecimal("0.00"));
						winnerBid.setTotalBidCount(auction.getTotalBidCount());
						winnerBid.setHighestBid(auction.getHighestBid());
						winnerBid.setLowestBid(auction.getLowestBid());
						winningBidDetailsMap.put(k, winnerBid);
					}
				});
		}});
		 return winningBidDetailsMap;
		}
	
		
		
		/**
		 * Compute the Price Paid
		 * Rules :a) For Single bid ,pricepaid=auction reserver price
		 * 		 :b) FOr Multi bid,pricepaid=second highest bid amount
		 * @param listOfBids
		 * @param auction
		 * @return
		 */
		private BigDecimal calculatePricePaid(List<Bid> listOfBids,Auction auction) {
			  if(1==listOfBids.size()) {
				  return auction.getReservePrice();
			  }else {
				  listOfBids.sort((Bid bid1, Bid bid2)->bid1.getBidAmount().compareTo(bid2.getBidAmount()));
				  return listOfBids.get(listOfBids.size()-2).getBidAmount();//second highest
			  }
		  }
		 
}
