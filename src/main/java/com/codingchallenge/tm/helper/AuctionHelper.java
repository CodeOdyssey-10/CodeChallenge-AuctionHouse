/**
 * 
 */
package com.codingchallenge.tm.helper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codingchallenge.tm.models.Auction;

/**
 * Auction Helper class
 *
 */
@Component
public class AuctionHelper {
	
	@Value("${output.file.location}")
	public String outputFolder;
	
	public static String[] getDelimitedInputAuctionMessage(String inputAuctionMessage,String delimiter) {
		return  inputAuctionMessage.split("\\".concat(delimiter));
	} 
	
	public void writeToFile(LinkedHashMap<String,Auction> winningBidMap) {
		 try {
				FileWriter writer = new FileWriter(outputFolder, true);
	            writer.write("Output :Auction Completed ");
	            writer.write("\r\n");
	            writer.write("==========================");
	            writer.write("\r\n");
	            writer.write("close_time|item|user_id|status|price_paid|total_bid_count|highest_bid|lowest_bid");
	            writer.write("\r\n");
	            writer.write("==========================");
	            writer.write("\r\n");
	            winningBidMap.forEach((k,v)->{
	        	   try {
					writer.write(v.getCloseTime()+"|"+v.getItem()+"|"+(v.getUserid()!=null?v.getUserid():"")+"|"+v.getStatus()+"|"+v.getPricePaid()+"|"+v.getTotalBidCount()+"|"+v.getHighestBid()+"|"+v.getLowestBid());
					writer.write("\r\n");
	        	   } catch (IOException e) {
	        		   e.printStackTrace();
	        	   }
	           	  
	            });
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
