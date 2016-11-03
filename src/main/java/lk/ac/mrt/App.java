package lk.ac.mrt;

import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.routing.Node;
import lk.ac.mrt.routing.Router;
import lk.ac.mrt.routing.RoutingTable;

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

        printMenu();


        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice){
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
            default:

        }



    }

    private static void handleRegister(){
        Router router = new Router();
        router.register();
        //two random nodes join
        List<Node> nodeList = router.getRandomNodes(2);
        for (Node node:nodeList){
            router.join(node);
        }

    }

    private static void handleUnregister(){
        Router router = new Router();
        router.unregister();
    }

    private static void handleLeave(){
        Router router = new Router();
        router.leave();
    }

    private static void printRoutingTable(){
        Router router = new Router();
        System.out.println("======================Routing Table====================================");
        router.printRoutingTable();
    }

    private static void printMenu(){
        System.out.println("=======================================================================");
        System.out.println("1. Register");
        System.out.println("2. Unregister");
        System.out.println("3. Leave");
        System.out.println("4. Print Routing Table");
        System.out.println("=======================================================================");
        System.out.println("Choose the number of your choice or press 0 to exit menu");
        System.out.println("=======================================================================");

    }
}
