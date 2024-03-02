package bank.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TraceRecord {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime dateTime = LocalDateTime.now();
    private Long accountNumber;
    private String operation;
    private Double amount;

    public TraceRecord(Long accountNumber, String operation, Double amount) {
        this.accountNumber = accountNumber;
        this.operation = operation;
        this.amount = amount;
    }
}
