package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int blockChainSize = 15;
        Blockchain blockchain = Blockchain.getBlockchain();
        Blockchain.setBlockchainSize(blockChainSize);
        executor.submit(new UserSimulator("User"));
        for (int i = 0; i < 4; i++) {
            executor.submit(new Miner());
        }
        while (true) {
            if (blockchain.getBlocksCounter() == blockChainSize) {
                executor.shutdownNow();
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
        System.out.println("Blockchain is valid: " + blockchain.validateBlockchain());
    }
}
