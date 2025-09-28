package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Item,Long> {
    @Query("select it from Item as it where (upper(it.description) like upper(?1) or upper(it.name) like upper(?1)) and it.available")
    List<Item> findByText(String textSearch);
    List<Item> findByOwner(Long ownerId);
}