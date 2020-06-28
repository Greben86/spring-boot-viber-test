package viber.bot.services;

import viber.bot.dao.Receiver;

import java.util.List;

public interface ReceiverService {

    void addReceiver(Receiver receiver);

    void removeReceiver(String id);

    List<Receiver> getAllReceivers();
}
