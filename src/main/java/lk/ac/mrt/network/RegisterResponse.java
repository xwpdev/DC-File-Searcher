package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class RegisterResponse extends Response {

    private int numberOfNodes;
    private String ip1;
    private int port1;
    private String ip2;
    private int port2;

    public RegisterResponse() {
        this.type = ResponseType.REGISTER;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public String getIp1() {
        return ip1;
    }

    public void setIp1(String ip1) {
        this.ip1 = ip1;
    }

    public int getPort1() {
        return port1;
    }

    public void setPort1(int port1) {
        this.port1 = port1;
    }

    public String getIp2() {
        return ip2;
    }

    public void setIp2(String ip2) {
        this.ip2 = ip2;
    }

    public int getPort2() {
        return port2;
    }

    public void setPort2(int port2) {
        this.port2 = port2;
    }

    @Override
    public String marshall() {
        return appendAll(type.code(), getNumberOfNodes(), getIp1(), getPort1(), getIp2(), getPort2());
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
		setNumberOfNodes(Integer.parseInt(splits[1]));
		//TODO: Can have multiple IPs from 0 to more. Use numberOfNodes to count IPs
        setIp1(splits[2]);
        setPort1(Integer.parseInt(splits[3]));
        setIp2(splits[4]);
        setPort2(Integer.parseInt(splits[5]));
    }
}
