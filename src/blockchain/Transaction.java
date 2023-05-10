package blockchain;

public record Transaction(String sender, String receiver, int amount) {

    @Override
    public String toString() {
        return sender + " sent " + amount + " VC to " + receiver;
    }
}
