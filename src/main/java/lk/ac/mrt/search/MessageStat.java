package lk.ac.mrt.search;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by chamika on 11/22/16.
 */
public class MessageStat {

    private final char COMMA = ',';

    private String fileName;

    private int countReceived;
    private int countForwarded;
    private int countAnswered;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCountReceived(int countReceived) {
        this.countReceived = countReceived;
    }

    public void setCountForwarded(int countForwarded) {
        this.countForwarded = countForwarded;
    }

    public void setCountAnswered(int countAnswered) {
        this.countAnswered = countAnswered;
    }

    public void write() {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            append(sb, countReceived);
            append(sb, countForwarded);
            append(sb, countAnswered);
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
            System.out.println("Message Stats wrote to file");
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
        if (text != null) {
            sb.append(text.toString());
            sb.append(COMMA);
        }
    }
}
