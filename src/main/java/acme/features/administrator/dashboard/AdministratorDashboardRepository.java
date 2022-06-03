package acme.features.administrator.dashboard;

import java.util.Collection;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.patronage.Status;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	//Components
	@Query("SELECT count(c) FROM Item c WHERE c.itemType = acme.entities.item.ItemType.COMPONENT")
	Integer totalNumberOfComponents();
	
	@Query("SELECT i.technology, avg(i.retailPrice.amount) FROM Item i WHERE i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.COMPONENT GROUP BY i.technology")
	Collection<Tuple> averageRetailPriceOfComponentsByTechnologyAndCurrency(String currency);
	
	@Query("SELECT i.technology, stddev(i.retailPrice.amount) FROM Item i WHERE i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.COMPONENT GROUP BY i.technology")
	Collection<Tuple> deviationRetailPriceOfComponentsByTechnologyAndCurrency(String currency);
	
	@Query("SELECT i.technology, min(i.retailPrice.amount) FROM Item i WHERE i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.COMPONENT GROUP BY i.technology")
	Collection<Tuple> minimumRetailPriceOfComponentsByTechnologyAndCurrency(String currency);

	@Query("SELECT i.technology, max(i.retailPrice.amount) FROM Item i WHERE i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.COMPONENT GROUP BY i.technology")
	Collection<Tuple> maximumRetailPriceOfComponentsByTechnologyAndCurrency(String currency);
	
	//Tools
	@Query("SELECT count(c) FROM Item c WHERE c.itemType = acme.entities.item.ItemType.TOOL")
	Integer totalNumberOfTools();
	
	@Query("SELECT avg(i.retailPrice.amount) FROM Item i WHERE (i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.TOOL)")
	Double averageRetailPriceOfToolsByCurrency(String currency);
	
	@Query("SELECT stddev(i.retailPrice.amount) FROM Item i WHERE i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.TOOL")
	Double deviationRetailPriceOfToolsByCurrency(String currency);

	@Query("SELECT min(i.retailPrice.amount) FROM Item i WHERE i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.TOOL ")
	Double minimumRetailPriceOfToolsByCurrency(String currency);

	@Query("SELECT max(i.retailPrice.amount) FROM Item i WHERE i.retailPrice.currency = :currency AND i.itemType = acme.entities.item.ItemType.TOOL")
	Double maximumRetailPriceOfToolsByCurrency(String currency);

	//Patronages
	@Query("SELECT count(p) FROM Patronage p WHERE p.status = :status")
	Integer totalNumberOfPatronagesByStatus(Status status);

	@Query("SELECT p.budget.currency, avg(p.budget.amount) FROM Patronage p WHERE p.status = :status GROUP BY p.budget.currency")
	Collection<Tuple> averageBudgetPatronagesByStatus(Status status);
	
	@Query("SELECT p.budget.currency, stddev(p.budget.amount) FROM Patronage p WHERE p.status = :status GROUP BY p.budget.currency")
	Collection<Tuple> deviationBudgetPatronagesByStatus(Status status);

	@Query("SELECT p.budget.currency, min(p.budget.amount) FROM Patronage p WHERE p.status = :status GROUP BY p.budget.currency")
	Collection<Tuple> minimumBudgetPatronagesByStatus(Status status);

	@Query("SELECT p.budget.currency, max(p.budget.amount) FROM Patronage p WHERE p.status = :status GROUP BY p.budget.currency")
	Collection<Tuple> maximumBudgetPatronagesByStatus(Status status);
	
	//Rustora
	@Query("select 1.0 * count(a) / (select count(b) FROM Item b) FROM Rustora a")
	Double ratioOfToolsWithRustora();
		
	@Query("select avg(r.quota.amount) FROM Rustora r WHERE r.quota.currency = :currency")
	Double averageQuotaOfRustoraByCurrency(String currency);
		
	@Query("select stddev(r.quota.amount) FROM Rustora r WHERE r.quota.currency = :currency")
	Double deviationQuotaOfRustoraByCurrency(String currency);
		
	@Query("select min(r.quota.amount) FROM Rustora r WHERE r.quota.currency = :currency")
	Double minimumQuotaOfRustoraByCurrency(String currency);
		
	@Query("select max(r.quota.amount) FROM Rustora r WHERE r.quota.currency = :currency")
	Double maximumQuotaOfRustoraByCurrency(String currency);
}