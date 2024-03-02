package bank.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AccountEventListener {

    @EventListener
    public void onEvent(AccountEvent event) {
        System.out.println("received event :" + event.getMessage());;
    }

}
