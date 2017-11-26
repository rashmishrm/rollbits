# rollbits
Distributed Chat Server


## Follow the below steps to deploy the project
1. To start the application, you need to run MessageApp.java . 

2. This class does the following:  <br/> 
    2.1 Starts UDP Server .<br/>
    2.2 UDP Broadcasts its details .<br/>
    2.3 Starts Raft Context .<br/>
    2.4 Starts Server Request Queue .<br/>
    2.5 Starts the netty TCP Server . <br/>


3. The following configuration files has been used: <br/>
   3.1 routing.conf : Contains details regarding tcp port and routing resources . <br/>
   3.2 config.yml : Contains constants used project wide including node details like name, ip, group tag etc.  <br/>
   3.3 hibernate.cfg.xml : Contains mysql and hibernate related configurations . <br/>


##Code Walkthrough
1. UDP: com.sjsu.rollbits.discovery
2. Raft: com.sjsu.rollbits.raft
3. Sharding & Replication: com.sjsu.rollbits.sharding.hashing
4. DAO: com.sjsu.rollbits.dao.interfaces
5. InterCluster Services: com.sjsu.rollbits.intercluster.sync
6. External Client: com.sjsu.rollbits.client
