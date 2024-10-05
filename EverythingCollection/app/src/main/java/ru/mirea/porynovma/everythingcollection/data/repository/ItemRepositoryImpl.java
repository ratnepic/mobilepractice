package ru.mirea.porynovma.everythingcollection.data.repository;

import ru.mirea.porynovma.everythingcollection.domain.models.Item;
import ru.mirea.porynovma.everythingcollection.domain.repository.ItemRepository;

public class ItemRepositoryImpl implements ItemRepository {
    @Override
    public Item getItem(int od) {
        return new Item(1);
    }

    @Override
    public Item[] getAllItems() {
        return new Item[0];
    }

    @Override
    public void saveItem(Item item) {

    }
}
