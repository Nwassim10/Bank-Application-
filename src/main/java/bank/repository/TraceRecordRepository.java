package bank.repository;

import bank.events.TraceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceRecordRepository extends JpaRepository<TraceRecord, Long> {
}
