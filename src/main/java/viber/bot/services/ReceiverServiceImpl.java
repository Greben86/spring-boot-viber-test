package viber.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import viber.bot.dao.Receiver;
import viber.bot.dao.ReceiverRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReceiverServiceImpl implements ReceiverService {

    @Autowired
    private ReceiverRepository receiverRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addReceiver(Receiver receiver) {
        receiverRepository.save(receiver);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void removeReceiver(String id) {
        receiverRepository.deleteById(id);
    }

    @Override
    public List<Receiver> getAllReceivers() {
        List<Receiver> result = new ArrayList<>();
        receiverRepository.findAll().forEach(result::add);
        return result;
    }
}
