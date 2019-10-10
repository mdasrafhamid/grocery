package com.grocery;
//import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class StoreLogic {

	/**
	* Log4j logger
	*/
//	static Logger log4j = Logger.getLogger("com.grocery");
//	log4j.debug("WTF?");
	
	static Scanner sc = new Scanner(System.in);
	static DecimalFormat df = new DecimalFormat("0.00");
	static String dashN = "\n---------------------------------------------------";
	static String dash = "---------------------------------------------------";
	
	
	public void load() {
		
		//list of store items -- more can be added
		Item.addToList(new Item("pizza",3,2.0f));
		Item.addToList(new Item("marker",5,1.0f));
		Item.addToList(new Item("drink",6,2.25f));
		
		// additional
		Item.addToList(new Item("beer",10,12.25f));
		
		
	}
	
	public void start() {
		firstMenu();
	}
	
	private int inputChoice(int min, int max){
		
		int choice;
		
		try {
			if(sc.hasNextInt()) {
				
				choice = Integer.parseInt(sc.nextLine());
				
				if(choice>=min && choice<=max)
					return choice;
				else if(choice==0)
					throw new InvalidInputException("0 not allowed!");
				else
					throw new InvalidInputException("Choice out of range!");
			}
			else {
				sc.nextLine();
				throw new InvalidInputException("Choice not an integer!");
			}
		} catch (InvalidInputException e) {
			System.out.println("\n"+e);
			System.out.println("Please reenter choice: ");
			choice = inputChoice(min, max);
			
		}
		return choice;
	}
	
	private void firstMenu(){

		System.out.println(dashN);
		System.out.println("\t\tWelcome to SriniMart!");
		System.out.println(dash);

		System.out.println("1. Select Item");
		System.out.println("2. Show Items");
		System.out.println("3. Exit");

		

		switch (inputChoice(1,3)) {

		case 1:
			Basket b = new Basket();
			printItems();
			chooseItem(b);
			break;
		case 2:
			printItems();
			firstMenu();
			break;
		case 3:
			System.out.println("See you again! Goodbye!");
			System.exit(1);
			break;
		default:
			System.out.println("not an option");
		}
		firstMenu();

	}

	private void mainMenu(Basket b){
		System.out.println(dashN);
		System.out.println("\t\tSriniMart");
		System.out.println(dash);

		System.out.println("1. Select Item");
		System.out.println("2. Show Store");
		System.out.println("3. Show Basket");
		System.out.println("4. Checkout");
		System.out.println("5. Cancel");
		System.out.println("6. Exit");


		switch (inputChoice(1,6)) {

		case 1:
			printItems();
			chooseItem(b);
			break;
		case 2:
			printItems();
			break;
		case 3:
			printBasketItem(b);
			break;
		case 4:
			checkOut(b);
			break;
		case 5:
			cancelOrder(b);
			break;
		case 6:
			System.exit(1);
			break;
		default:
			System.out.println("not an option");

		}
		mainMenu(b);

	}

	private void checkOut(Basket b){
		if (b.isEmpty())
			System.out.println("Your basket is empty! Pls select an item");
		else {
			System.out.println("Your order is succesfull!");
			System.out.println("Thank you for shopping with us!\n");
			firstMenu();
		}
	}

	private void cancelOrder(Basket b){
		List<Item> bList = b.getList();
		int size = bList.size();

		//putting items from basket to store
		for (int i = 0; i < size; i++) {
			Item tempItem = bList.remove(0);
			Item storeItem = getStoreItemByName(tempItem.getName());
			storeItem.setQty(tempItem.getQty() + storeItem.getQty());
		}
		if (b.isEmpty())
			System.out.println("Basket is now empty!");
		
		firstMenu();
	}

	private void printItems() {
		
		System.out.println(dashN);
		System.out.println("\tList of store items");
		System.out.println(dash);
		System.out.println("No.\tItem\t\tQty\tPrice");
		System.out.println(dash);

		for (int i = 0; i < Item.getList().size(); i++) {
			System.out.println((i + 1) + ")\t" + Item.getList().get(i).getName() + "\t\t "
					+ Item.getList().get(i).getQty()+"\t"+df.format(Item.getList().get(i).getPrice()));
		}

	}
	private Item getStoreItemByName(String s) {
	
		for (Item item : Item.getList()) {
			if (item.getName().equals(s))
				return item;
		}
		return null;
	}

	private Item getBasketItem(Basket b,String name) {
		
		for (Item item : b.getList()) {
			if(item.getName().equals(name))
				return item;
		}
		return null;
	}

	private void addToBasket(int qty, Basket b,Item sItem) {
		Item bItem;
		
		if((bItem=getBasketItem(b, sItem.getName()))==null) //not in basket -> new basket item
			b.addToBasket(new Item(sItem.getName(), qty, sItem.getPrice()));
		else
			bItem.setQty(bItem.getQty()+qty);	//already in basket -> add to basket item qty
		
		sItem.setQty(sItem.getQty() - qty); //deduct from store
	}
	
	private void chooseItem(Basket b){

		System.out.println("\nPlease choose from item 1 to " + Item.getList().size() + ": ");
		
		int choice;
		
		choice = inputChoice(1,Item.getList().size());
		
		Item sItem = Item.getList().get(choice - 1);
		
		System.out.println("You have chosen " + sItem.getName() + "!");

		System.out.println("\nPlease choose quantity of " + sItem.getName() + " (stock : " + sItem.getQty() + ")");
		
		choice = inputChoice(1,99); //Limit qty to 99

		//error checking - qty within stock
		if (choice > 0 && choice <= sItem.getQty()) {

			System.out.println(choice + " " + sItem.getName() + " selected");
			
			addToBasket(choice, b, sItem);

		} else if (choice > sItem.getQty())
			System.out.println("out of stock!");
		else
			System.out.println("no qty selected. Please reenter");

		mainMenu(b);
	}

	private void printBasketItem(Basket b) {

		System.out.println(dashN);
		System.out.println("\t\tItems in basket!");
		System.out.println(dash);
		System.out.println("No.\tItem\t\tQty\tPrice");
		System.out.println(dash);
		
		int i = 1;
		for (Item item : b.getList()) {
			System.out.println((i++) + ")\t" + item.getName() + "\t\t" + item.getQty() + "\t"
					+ df.format(item.getPrice() * item.getQty()));
		}
		
		System.out.println(dash);
		System.out.println("Total\t\t\t"+totalBasketQty(b)+"\t"+df.format(totalBasketPrice(b)));
		


	}

	private float totalBasketPrice(Basket b) {

		float total = 0;
		for (Item item : b.getList()) {
			total += (item.getPrice() * item.getQty());
		}
		return total;
	}
	private int totalBasketQty(Basket b) {

		int total = 0;
		for (Item item : b.getList()) {
			total += item.getQty();
		}
		return total;
	}

}
