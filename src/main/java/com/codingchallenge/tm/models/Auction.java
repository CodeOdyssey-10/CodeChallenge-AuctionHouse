/**
 * 
 */
package com.codingchallenge.tm.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Auction Entity Class
 * 
 *
 */
public class Auction {
	
	private Integer userid;
	private String action;
	private String item;
	private Integer startTime;
	private Integer closeTime;
	private BigDecimal reservePrice;
	private List<Bid> listOfBids;
	
	private Integer totalBidCount;
	private BigDecimal highestBid;
	private BigDecimal lowestBid;
	private boolean isAuctionOpen;
	private String status;
	private BigDecimal pricePaid;
	
	public Auction() {
		
	}
	public Auction(Integer userid,String item,String action,Integer startTime,Integer closeTime,BigDecimal reservePrice,boolean isAuctionOpen) {
		this.userid=userid;
		this.item=item;
		this.action=action;
		this.startTime=startTime;
		this.closeTime=closeTime;
		this.reservePrice=reservePrice;
		this.isAuctionOpen=isAuctionOpen;
	}

	/**
	 * @return the userid
	 */
	public Integer getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
	 * @return the startTime
	 */
	public Integer getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the closeTime
	 */
	public Integer getCloseTime() {
		return closeTime;
	}

	/**
	 * @param closeTime the closeTime to set
	 */
	public void setCloseTime(Integer closeTime) {
		this.closeTime = closeTime;
	}

	/**
	 * @return the reservePrice
	 */
	public BigDecimal getReservePrice() {
		return reservePrice;
	}

	/**
	 * @param reservePrice the reservePrice to set
	 */
	public void setReservePrice(BigDecimal reservePrice) {
		this.reservePrice = reservePrice;
	}

	/**
	 * @return the listOfBids
	 */
	public List<Bid> getListOfBids() {
		if(null==listOfBids)
			return listOfBids= new ArrayList<Bid>();
		return listOfBids;
	}

	/**
	 * @param listOfBids the listOfBids to set
	 */
	public void setListOfBids(List<Bid> listOfBids) {
		this.listOfBids = listOfBids;
	}

	
	/**
	 * @return the totalBidCount
	 */
	public Integer getTotalBidCount() {
		return totalBidCount;
	}

	/**
	 * @param totalBidCount the totalBidCount to set
	 */
	public void setTotalBidCount(Integer totalBidCount) {
		this.totalBidCount = totalBidCount;
	}

	/**
	 * @return the highestBid
	 */
	public BigDecimal getHighestBid() {
		return highestBid;
	}

	/**
	 * @param highestBid the highestBid to set
	 */
	public void setHighestBid(BigDecimal highestBid) {
		this.highestBid = highestBid;
	}

	/**
	 * @return the lowestBid
	 */
	public BigDecimal getLowestBid() {
		return lowestBid;
	}

	/**
	 * @param lowestBid the lowestBid to set
	 */
	public void setLowestBid(BigDecimal lowestBid) {
		this.lowestBid = lowestBid;
	}

	

	/**
	 * @return the isAuctionOpen
	 */
	public boolean isAuctionOpen() {
		return isAuctionOpen;
	}

	/**
	 * @param isAuctionOpen the isAuctionOpen to set
	 */
	public void setAuctionOpen(boolean isAuctionOpen) {
		this.isAuctionOpen = isAuctionOpen;
	}

	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the pricePaid
	 */
	public BigDecimal getPricePaid() {
		return pricePaid;
	}
	/**
	 * @param pricePaid the pricePaid to set
	 */
	public void setPricePaid(BigDecimal pricePaid) {
		this.pricePaid = pricePaid;
	}
	@Override
	public String toString() {
		return "Auction [userid=" + userid + ", action=" + action + ", item=" + item + ", startTime=" + startTime
				+ ", closeTime=" + closeTime + ", reservePrice=" + reservePrice + ", listOfBids=" + listOfBids
				+ ", totalBidCount=" + totalBidCount + ", highestBid=" + highestBid + ", lowestBid=" + lowestBid
				+ ", isAuctionOpen=" + isAuctionOpen + ", status=" + status + ", pricePaid=" + pricePaid + "]";
	}

	

	
}
