package lk.ac.mrt.routing;


import java.util.ArrayList;
import java.util.List;

/**
 * Stores the routing table data
 */
public class RoutingTable {

    private ArrayList<Node> routingTable = new ArrayList<>(  );

    public boolean addNode(Node node){

		boolean exist = false;
		for ( Node nd : routingTable )
		{
			if(nd.getIp().equals( node.getIp() ) && nd.getPort() == node.getPort()){
				exist = true;
				break;
			}
		}

		if(!exist)
		{
			routingTable.add( node );
			return true;
		}
		return false;
    }

    public void deleteNode(Node node) {
		int index = -1;
		for ( int i = 0; i < routingTable.size(); i++ )
		{
			Node nd = routingTable.get( i );
			if ( nd.getIp().equals( node.getIp() ) && nd.getPort() == node.getPort() )
			{
				index = i;
				break;
			}
		}
		if ( index >= 0 )
		{
			routingTable.remove( index );
		}
	}

    public void clearData(){routingTable.clear();}

    public int getSize(){
        return routingTable.size();
    }

    public Node getNode(int index){
        return routingTable.get(index);
    }


}
