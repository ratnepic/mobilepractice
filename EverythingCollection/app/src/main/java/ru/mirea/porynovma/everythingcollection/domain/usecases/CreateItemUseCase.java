package ru.mirea.porynovma.everythingcollection.domain.usecases;

import ru.mirea.porynovma.everythingcollection.domain.models.Item;
import ru.mirea.porynovma.everythingcollection.domain.repository.ItemRepository;

public class CreateItemUseCase {
    private ItemRepository repo;
    public CreateItemUseCase(ItemRepository repo) {
        this.repo = repo;
    }
    public Item execute() {
        return this.repo.createItem();
    }
}
