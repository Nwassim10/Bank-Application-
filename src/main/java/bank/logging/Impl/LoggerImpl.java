package bank.logging.Impl;

import bank.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoggerImpl implements Logger {

	public void log(String logstring) {
		java.util.logging.Logger.getLogger("BankLogger").info(logstring);
		System.out.println(logstring);
	}

}
