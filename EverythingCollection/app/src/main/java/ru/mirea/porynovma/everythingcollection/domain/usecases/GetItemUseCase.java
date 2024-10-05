package ru.mirea.porynovma.everythingcollection.domain.usecases;

import ru.mirea.porynovma.everythingcollection.domain.models.Item;
import ru.mirea.porynovma.everythingcollection.domain.repository.ItemRepository;

public class GetItemUseCase {
    private ItemRepository repo;
    public GetItemUseCase(ItemRepository repo) {
        this.repo = repo;
    }
    public Item execute(int id) {
        return this.repo.getItem(id);
    }
}
