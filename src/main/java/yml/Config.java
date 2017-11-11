package yml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.yaml.snakeyaml.Yaml;

import com.sjsu.rollbits.discovery.MyConstants;
import com.sjsu.rollbits.discovery.Node;


public class Config {
    public static void main(String[] args) throws IOException {
    	MyConstants constant=new MyConstants();
    	Configure con=new Configure();
    	Configuration configuration=new Configuration();
    	Connection conn=new Connection();
    	//configuration.configure().buildSessionFactory().getCurrentSession().ge
    	Map<String, Map<String, Node>> groupnodes =con.getGroup();
    	Map<String,Node> mapnodes=con.getNodes();
    	//Map<String,String> url =new HashMap<String,String>();
    	//private static final SessionFactory sessionFactory = buildSessionFactory();
    	String url=conn.getURL();
    	url = "url: " + url;
    	String port = "port: "+ constant.NODE_PORT;
    	
        Yaml yaml = new Yaml();
        File file = new File("config.yml");
        FileWriter writer = new FileWriter(file,true);
        
        yaml.dump("---------------------------",writer);
        yaml.dump(new Date(),writer);
        yaml.dump(url,writer);
        yaml.dump(port,writer);
        yaml.dump("Nodes", writer);
        yaml.dump(mapnodes,writer);
        yaml.dump("Groups",writer);
        yaml.dump(groupnodes,writer);
        
        writer.close();
        //System.out.println("end");	
        
    }
}
