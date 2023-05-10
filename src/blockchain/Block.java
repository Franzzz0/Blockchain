package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    private final String previousBlockHash;
    private final int id;
    private final List<Transaction> transactions;
    private final long timestamp;
    private final String hash;
    private final String minerID;

    private final int magic;
    private final int generatingTime;

    public Block(String previousBlockHash, int id, List<Transaction> transactions,
                 String hash, String minerID, int magic, int generatingTime) {
        this.previousBlockHash = previousBlockHash;
        this.id = id;
        this.transactions = transactions == null ? null : new ArrayList<>(transactions);
        this.timestamp = new Date().getTime();
        this.hash = hash;
        this.minerID = minerID;

        this.magic = magic;
        this.generatingTime = generatingTime;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getHash() {
        return hash;
    }

    public String getMinerID() {
        return minerID;
    }

    public int getGeneratingTime() {
        return generatingTime;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Block:" +
                "\nCreated by: " + minerID +
                "\n" + minerID + " gets 100 VC" +
                "\nId: " + id +
                "\nTimestamp: " + timestamp +
                "\nMagic number: " + magic +
                "\nHash of the previous block: \n" + previousBlockHash +
                "\nHash of the block: \n" + hash +
                "\nBlock data: " + getBlockData() +
                "\nBlock was generating for " + generatingTime + " seconds";
    }

    private String getBlockData() {
        if (transactions == null) return "no transactions";
        StringBuilder builder = new StringBuilder();
        for (Transaction transaction : transactions) {
            builder.append("\n").append(transaction.toString());
        }
        return builder.toString();
    }
}
