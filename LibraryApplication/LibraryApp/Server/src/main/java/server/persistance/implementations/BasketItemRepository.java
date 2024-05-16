package server.persistance.implementations;

import org.springframework.stereotype.Repository;
import server.model.BasketItem;
import server.persistance.interfaces.IBasketItemRepository;

@Repository
public class BasketItemRepository implements IBasketItemRepository {
    @Override
    public BasketItem add(BasketItem obj) {
        return null;
    }

    @Override
    public BasketItem remove(Long aLong) {
        return null;
    }

    @Override
    public BasketItem update(BasketItem obj) {
        return null;
    }

    @Override
    public BasketItem get(Long aLong) {
        return null;
    }

    @Override
    public Iterable<BasketItem> getAll() {
        return null;
    }
}
