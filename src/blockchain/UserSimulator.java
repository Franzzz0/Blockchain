package blockchain;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UserSimulator extends Thread implements User{
    private final Blockchain BLOCKCHAIN = Blockchain.getBlockchain();
    private final String name;

    public UserSimulator(String name) {
        this.name = name;
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                break;
            }
            BLOCKCHAIN.submitTransaction(generateRandomTransaction());
        }
    }

    private Transaction generateRandomTransaction() {
        Random rnd = new Random();
        int rndNumber = rnd.nextInt(5) + 1;
        String receiver = switch (rndNumber) {
            case 1 -> "Buddy";
            case 2 -> "Dude";
            case 3 -> "Johnny";
            case 4 -> "Sticky Vicky";
            case 5 -> "miner" + rnd.nextInt(22);
            default -> throw new IllegalStateException("Unexpected value: " + rndNumber);
        };
        return new Transaction(name, receiver, (rnd.nextInt(50) + 1));
    }

    @Override
    public String getID() {
        return name;
    }
}
