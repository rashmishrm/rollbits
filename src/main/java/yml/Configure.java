package yml;

import static java.lang.String.format;

import java.util.Map;

import com.sjsu.rollbits.discovery.Node;
import com.sjsu.rollbits.discovery.ClusterDirectory;

public final class Configure { 
	 ClusterDirectory cd=new ClusterDirectory();
    private Connection connection;
    private Map<String, Node> nodes=cd.getNodeMap();
    private Map<String, Map<String,Node>> groups=cd.getGroupMap();
   
   
    public Connection getConnection() {
        return connection;
    }
 
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
 
       public Map< String, Node > getNodes() {
       return nodes;
    }
 
    public void setGroups(Map< String, Map<String,Node>> groups) {
        this.groups=groups;
    }
    
    public Map< String, Map<String,Node>> getGroup() {
        return groups;
    }
 
    public void setNodes(Map< String, Node > nodes) {
        this.nodes=nodes;
    }

 
    
    @Override
    public String toString() {
        return new StringBuilder()
            .append( format( "Nodes: %s\n", nodes ) )
            .append( format( "Groups: %s\n", groups ) )
            .toString();
    }
}