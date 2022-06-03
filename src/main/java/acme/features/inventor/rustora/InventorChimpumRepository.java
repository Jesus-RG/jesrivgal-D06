package acme.features.inventor.rustora;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.item.Item;
import acme.entities.rustora.Rustora;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface InventorChimpumRepository extends AbstractRepository {

	@Query("select i.rustora from Item i where i.inventor.id =:id")
	Collection<Rustora> findRustorasByInventorId(int id);
	
	@Query("select r from Rustora r where r.id = :id")
	Rustora findRustoraById(int id);
	
	@Query("SELECT i FROM Item i WHERE i.rustora.id =:id")
	Item findItemByRustoraId(int id);
	
	@Query("SELECT r FROM Rustora r WHERE r.code =:code")
	Rustora findRustoraByCode(String code);

}