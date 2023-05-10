package blockchain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Blockchain {
    private static final Blockchain BLOCKCHAIN = new Blockchain();
    private static int blockchainSize;

    private final List<Block> chain;
    private final List<Transaction> awaitingTransactions;
    private int blocksCounter;
    private int leadingZeros;

    private Blockchain() {
        chain = new LinkedList<>();
        awaitingTransactions = new ArrayList<>();
        blocksCounter = 0;
        this.leadingZeros = 0;
    }

    public static Blockchain getBlockchain() {
        return BLOCKCHAIN;
    }

    public static void setBlockchainSize(int size) {
        blockchainSize = size;
    }

    public Block getLastBlock() {
        if (chain.size() > 0) return chain.get(chain.size() - 1);
        return null;
    }

    private String getLastHash() {
        if (chain.size() > 0) {
            Block lastBlock = chain.get(chain.size() - 1);
            return lastBlock.getHash();
        } else {
            return "0";
        }
    }

    public int getBlocksCounter() {
        return blocksCounter;
    }

    public int getLeadingZeros() {
        return leadingZeros;
    }

    public synchronized List<Transaction> getAwaitingTransactions() {
        if (awaitingTransactions.isEmpty()) return null;
        return awaitingTransactions;
    }

    public synchronized void offerNewBlock(Block newBlock) {
        if (blocksCounter == blockchainSize) return;
        if (Objects.equals(newBlock.getPreviousBlockHash(), getLastHash())) {
            if (leadingZeros > 0 && !newBlock.getHash().substring(0, leadingZeros).matches("0*")) {
                return;
            }
            chain.add(newBlock);
            blocksCounter++;
            awaitingTransactions.clear();
            System.out.println(newBlock);

            int generatingTime = newBlock.getGeneratingTime();
            int UPPER_GENERATING_TIME = 3;
            int LOWER_GENERATING_TIME = 1;

            if (generatingTime > UPPER_GENERATING_TIME && leadingZeros > 0) {
                leadingZeros--;
                System.out.println("N was decreased to " + leadingZeros);
            } else if (generatingTime < LOWER_GENERATING_TIME && leadingZeros < 3) {
                leadingZeros++;
                System.out.println("N was increased to " + leadingZeros);
            } else {
                System.out.println("N stays the same");
            }
            System.out.println();
        }
    }

    public synchronized void submitTransaction(Transaction transaction) {
        if (getBalance(transaction.sender()) < transaction.amount()) return;
        awaitingTransactions.add(transaction);
    }

    public synchronized int getBalance(String username) {
        int balance = 0;
        for (Block block : chain) {
            if (block.getTransactions() != null) {
                for (Transaction t : block.getTransactions()) {
                    if (Objects.equals(t.receiver(), username)) {
                        balance += t.amount();
                    } else if (Objects.equals(t.sender(), username)) {
                        balance -= t.amount();
                    }
                }
            }
            if (Objects.equals(block.getMinerID(), username)) balance += 100;
        }
        for (Transaction transaction : awaitingTransactions) {
            if (Objects.equals(transaction.receiver(), username)) {
                balance += transaction.amount();
            } else if (Objects.equals(transaction.sender(), username)) {
                 balance -= transaction.amount();
            }
        }
        return balance;
    }

    public boolean validateBlockchain() {
        if (!Objects.equals(chain.get(0).getPreviousBlockHash(), "0")) return false;
        for (int i = 1; i < chain.size(); i++) {
            if (!Objects.equals(chain.get(i).getPreviousBlockHash(), chain.get(i - 1).getHash())) {
                return false;
            }
        }
        return true;
    }
}
