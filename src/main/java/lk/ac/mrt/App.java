package lk.ac.mrt;

import lk.ac.mrt.comment.GossipInitiator;
import lk.ac.mrt.common.Constants;
import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.ErrorResponse;
import lk.ac.mrt.network.MessageHandler;
import lk.ac.mrt.network.Response;
import lk.ac.mrt.routing.HeatbeatChecker;
import lk.ac.mrt.routing.Node;
import lk.ac.mrt.routing.Router;
import lk.ac.mrt.routing.RoutingTable;
import lk.ac.mrt.search.SearchHandler;

import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Distributed File Searcher " );
        System.out.println(PropertyProvider.listProperties());
		//for initialization
		SearchHandler.getInstance();

        printMenu();


        Scanner scanner = new Scanner(System.in);

		int choice = 0;
		do{
			try
			{
				choice = scanner.nextInt();
				switch ( choice )
				{
					case 1:
						handleRegister();
						break;
					case 2:
						handleUnregister();
						break;
					case 3:
						handleLeave();
						break;
					case 4:
						printRoutingTable();
						break;
					case 5:
						handleSearch(scanner);
						break;
					case 6:
						handleUpdateHops(scanner);
						break;
					case 7:
						handleUpdateForward(scanner);
						break;
					case 8:
						recordStats();
						break;
					default:

				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}

		}while(choice != 0);
		System.out.println("Exit");
	}

	private static void handleUpdateHops(Scanner scanner){
		System.out.print("\nPrevious hop count is " + PropertyProvider.getProperty( Constants.CONFIG_MAX_HOP_COUNT ) + ". Enter new maximum hop count:");
		int count = scanner.nextInt();
		PropertyProvider.setProperty( Constants.CONFIG_MAX_HOP_COUNT,String.valueOf(count) );
	}

	private static void handleUpdateForward(Scanner scanner){
		System.out.print("\nPrevious forward count is " + PropertyProvider.getProperty(Constants.FORWARD_COUNT) + ". Enter new forward hop count:");
		int count = scanner.nextInt();
		PropertyProvider.setProperty( Constants.FORWARD_COUNT,String.valueOf(count) );
	}

	private static void handleSearch(Scanner scanner)
	{
		System.out.print("\nEnter keyword to search:");
		String keyword = scanner.next();
		boolean result = SearchHandler.getInstance().initiateSearch(keyword);
		if(!result){
			System.out.println("Unable to start search");
			return;
		}
		System.out.println("Waiting for results...");
//		System.out.println("Press 0 to stop the search");
//		while(scanner.nextInt() == 0);
	}

	private static void handleRegister(){
        Router router = Router.getInstance();
        boolean success =router.register();
        if(!success){
            return ;
        }

        //two random nodes join
        List<Node> nodeList = router.getRandomNodes( Integer.parseInt( PropertyProvider.getProperty( Constants.JOIN_COUNT ) ) );
        for (Node node:nodeList){
            router.join(node);
        }

        //Enable UDP listening for all messages
		MessageHandler.getInstance().startListening();
		HeatbeatChecker.getInstance().startChecking();

		// AHESH send UDP message
//		MessageHandler.getInstance().sendUDPMsg("10.10.10.132",8080,"TEST UDP MESSAGE");

		//Enable gossiping
		GossipInitiator.getInstance().startGossiping();

	}

    private static void handleUnregister(){
        Router router = Router.getInstance();
        boolean success = router.unregister();
        if(!success){
            return;
        }

		MessageHandler.getInstance().stopListening();
		HeatbeatChecker.getInstance().stopChecking();
    }

    private static void handleLeave(){
        Router router = Router.getInstance();
        router.leave();
		//TODO: add response from leave()
		//if success from router.leave();
		MessageHandler.getInstance().stopListening();
		HeatbeatChecker.getInstance().stopChecking();
    }

    private static void printRoutingTable(){
        Router router = Router.getInstance();
        System.out.println("======================Routing Table====================================");
        router.printRoutingTable();
    }

    private static void recordStats(){
		SearchHandler.getInstance().printAndResetCounts();
	}

    private static void printMenu(){
        System.out.println("\n\n\n=======================================================================");
        System.out.println("1. Register");
        System.out.println("2. Unregister");
        System.out.println("3. Leave");
        System.out.println("4. Print Routing Table");
        System.out.println("5. Search");
        System.out.println("6. Change hop count");
        System.out.println("7. Change forward count");
        System.out.println("8. Print and reset stats");
        System.out.println("0. Exit");
        System.out.println("=======================================================================");
        System.out.println("Choose the number of your choice or press 0 to exit menu");
        System.out.println("=======================================================================");

    }
}
