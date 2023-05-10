package blockchain;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Miner extends Thread implements User{
    private final Blockchain blockchain = Blockchain.getBlockchain();

    @Override
    public void run() {
        while (!isInterrupted()) {
            Block lastBlock = blockchain.getLastBlock();
            int leadingZeros = blockchain.getLeadingZeros();

            String lastHash = lastBlock == null ? "0" : lastBlock.getHash();
            int blockID = blockchain.getBlocksCounter() + 1;
            String hash;
            String minerID = getID();
            int magic;
            int genTime;

            long start = System.currentTimeMillis();
            Random rnd = new Random();
            while (true) {
                if (isInterrupted()) return;
                magic = rnd.nextInt(999999999);
                hash = StringUtil.applySha256(lastHash + blockID + magic);
                if (hash.substring(0, leadingZeros).matches("0*")) {
                    genTime = (int) ((System.currentTimeMillis() - start) / 1000);
                    break;
                }
            }
            List<Transaction> transactions;
            while (true) {
                transactions = blockchain.getAwaitingTransactions();
                if (transactions != null || lastBlock == null) break;
                if (isInterrupted()) return;
                try {
                    trySubmitTransaction();
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    return;
                }
            }
            Block block = new Block(lastHash, blockID, transactions, hash, minerID, magic, genTime);
            blockchain.offerNewBlock(block);
            trySubmitTransaction();
        }

    }

    private void trySubmitTransaction() {
        Random rnd = new Random();
        if (blockchain.getBalance(getID()) >= 100) {
            int randomNumber = rnd.nextInt(22) + 1;
            String receiver = randomNumber % 2 == 0 ? "miner" + randomNumber : "User";
            Transaction transaction = new Transaction(getID(), receiver, rnd.nextInt(100));
            blockchain.submitTransaction(transaction);
        }
    }

    @Override
    public String getID() {
        return "miner" + currentThread().getId();
    }
}
