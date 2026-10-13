package model;

import java.time.Instant;

public final class Transaction
{
    private String type;
    private long amount;
    private Instant timestamp;
    private long newBalance;
    //
    public Transaction(String type, long amount, Instant timestamp, long newBalance)
    {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.newBalance = newBalance;
    }
    //
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public long getAmount() {
        return amount;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public long getNewBalance() {
        return newBalance;
    }
    public void setNewBalance(long newBalance) {
        this.newBalance = newBalance;
    }
    //
    @Override
    public String toString() {
        return """
                Type: %s
                Amount: %d
                Timestamp: %s
                Updated Balance: %d """.formatted(type, amount, timestamp, newBalance);
    }
}
