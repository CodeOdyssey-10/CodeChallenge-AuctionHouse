package com.codingchallenge.tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import com.codingchallenge.tm.controller.AuctionController;
import com.codingchallenge.tm.exceptions.InvalidAuctionException;

/**
 * @Description AuctionHouse Main entry point
 * @author hiran
 *
 */
@SpringBootApplication
public class AuctionHouseApplication {
    
	@Autowired
	private static AuctionController auctionController;
	
	static ApplicationContext context;
	
	public static void main(String[] args) throws InvalidAuctionException, IOException {
		
		context           = SpringApplication.run(AuctionHouseApplication.class, args);
		auctionController = (AuctionController) context.getBean("auctionController");
	 
		 Scanner reader ;
		//Read input file from command line
		if(args.length>0) {
			reader = new Scanner(new FileInputStream(args[0]));
		}else {
			//Read the input file from classpath-**For localhost Testing
			Resource resource = new ClassPathResource("input.txt");
			File file = resource.getFile();
			reader = new Scanner(new FileInputStream(file));
		}
		while (reader.hasNext()) {
			auctionController.processAuction(reader.next());
		}
		System.out.println("Expected Output :Completed Auction");
		System.out.println("===================================");
		System.out.println("close_time|item|user_id|status|price_paid|total_bid_count|highest_bid|lowest_bid");
		auctionController.getWinningBid().forEach((k,v)->{
			System.out.println(v.getCloseTime()+"|"+v.getItem()+"|"+(v.getUserid()!=null?v.getUserid():"")+"|"+v.getStatus()+"|"+v.getPricePaid()+"|"+v.getTotalBidCount()+"|"+v.getHighestBid()+"|"+v.getLowestBid());
			
		});
       
		auctionController.writeToOutputFile();
	}
}
