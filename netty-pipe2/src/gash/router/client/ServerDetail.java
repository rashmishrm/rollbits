package gash.router.client;

public class ServerDetail {


    String host;
    int port;



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ServerDetail(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
