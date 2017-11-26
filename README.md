# rollbits
Distributed Chat Server


## Follow the below steps to deploy the project
1. To start the application, you need to run MessageApp.java . 
2. This class does the following:  
  A. Starts UDP Server . 
  B. UDP Broadcasts its details . 
  C. Starts Raft Context . 
  D. Starts Server Request Queue . 
  E. Starts the netty TCP Server . 
3. The following configuration files has been used:  
  A. routing.conf : Contains details regarding tcp port and routing resources . 
  B. config.yml : Contains constants used project wide including node details like name, ip, group tag etc.  
  C. hibernate.cfg.xml : Contains mysql and hibernate related configurations . 
  
##Code Walkthrough
1. UDP: com.sjsu.rollbits.discovery
2. Raft: com.sjsu.rollbits.raft
3. Sharding & Replication: com.sjsu.rollbits.sharding.hashing
4. DAO: com.sjsu.rollbits.dao.interfaces
5. InterCluster Services: com.sjsu.rollbits.intercluster.sync
6. External Client: com.sjsu.rollbits.client
