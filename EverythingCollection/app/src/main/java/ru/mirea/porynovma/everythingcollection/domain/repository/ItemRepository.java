package ru.mirea.porynovma.everythingcollection.domain.repository;

import ru.mirea.porynovma.everythingcollection.domain.models.Item;

public interface ItemRepository {
    public Item getItem(int id);
    public Item[] getAllItems();
    public void saveItem(Item item);
    public Item createItem();
}
