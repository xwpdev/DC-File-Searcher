package lk.ac.mrt.search;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamika on 11/22/16.
 */
public class SearchStat {

    private final char COMMA = ',';

    private long searchInitTime;
    private String fileName;

    private int maxHopCount;
    private int maxForwardCount;
    private int receiveCount;
    private int forwardCount;
    private int resultCount;
    private int routingTableSize;
    private List<Long> responseTimes;
    private String messageHash;
    private boolean searchInit;

    public long getSearchInitTime() {
        return searchInitTime;
    }

    public void setSearchInitTime(long searchInitTime) {
        this.searchInitTime = searchInitTime;
    }

    public int getMaxHopCount() {
        return maxHopCount;
    }

    public void setMaxHopCount(int maxHopCount) {
        this.maxHopCount = maxHopCount;
    }

    public int getMaxForwardCount() {
        return maxForwardCount;
    }

    public void setMaxForwardCount(int maxForwardCount) {
        this.maxForwardCount = maxForwardCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public void addReceiveCount() {
        this.receiveCount += 1;
    }

    public int getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(int forwardCount) {
        this.forwardCount = forwardCount;
    }

    public void addForwardCount() {
        this.forwardCount += 1;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public void addResultCount() {
        this.resultCount += 1;
        long diff = System.currentTimeMillis() - this.searchInitTime;
        getResponseTimes().add(diff);
    }

    public int getRoutingTableSize() {
        return routingTableSize;
    }

    public void setRoutingTableSize(int routingTableSize) {
        this.routingTableSize = routingTableSize;
    }

    public List<Long> getResponseTimes() {
        if (responseTimes == null) {
            responseTimes = new ArrayList<Long>();
        }
        return responseTimes;
    }

    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void write() {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            append(sb, messageHash);
            append(sb, maxHopCount);
            append(sb, maxForwardCount);
            append(sb, routingTableSize);
            append(sb, receiveCount);
            append(sb, forwardCount);
            append(sb, resultCount);
            append(sb, responseTimes);
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
            System.out.println("Stats wrote to file");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                }
            }
        }

    }

    private void append(StringBuilder sb, String text) {
        sb.append(text);
        sb.append(COMMA);
    }

    private void append(StringBuilder sb, int text) {
        sb.append(text);
        sb.append(COMMA);
    }

    private void append(StringBuilder sb, Object text) {
        if(text != null) {
            sb.append(text.toString());
            sb.append(COMMA);
        }
    }

    public boolean isSearchInit() {
        return searchInit;
    }

    public void setSearchInit(boolean searchInit) {
        this.searchInit = searchInit;
    }
}
