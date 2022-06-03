//package acme.features.inventor.rustora;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import acme.entities.item.Item;
//import acme.entities.moneyExchange.MoneyExchange;
//import acme.entities.rustora.Rustora;
//import acme.features.authenticated.moneyExchange.AuthenticatedMoneyExchangePerformService;
//import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
//import acme.framework.components.models.Model;
//import acme.framework.controllers.Request;
//import acme.framework.datatypes.Money;
//import acme.framework.services.AbstractShowService;
//import acme.roles.Inventor;
//
//@Service
//public class InventorChimpumShowService implements AbstractShowService<Inventor, Rustora> {
//
//	@Autowired
//	protected InventorChimpumRepository inventorChimpumRepository;
//	@Autowired
//	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;
//
//	@Override
//	public boolean authorise(final Request<Rustora> request) {
//		assert request != null;
//
//		boolean res;
//		int chimpumId;
//		Item item;
//
//		chimpumId = request.getModel().getInteger("id");
//		item = this.inventorChimpumRepository.findItemByRustoraId(chimpumId);
//		res = item.getInventor().getId() == request.getPrincipal().getActiveRoleId();
//
//		return res;
//	}
//
//	@Override
//	public Rustora findOne(final Request<Rustora> request) {
//		assert request != null;
//		int chimpumId;
//		Rustora res;
//
//		chimpumId = request.getModel().getInteger("id");
//		res = this.inventorChimpumRepository.findRustoraById(chimpumId);
//
//		return res;
//	}
//
//	@Override
//	public void unbind(final Request<Rustora> request, final Rustora entity, final Model model) {
//		assert request != null;
//		assert entity != null;
//		assert model != null;
//		
//		final int itemId = this.inventorChimpumRepository.findItemByRustoraId(entity.getId()).getId();
//		model.setAttribute("itemId", itemId);
//
//		final Money newBudget = this.moneyExchangeChimpum(entity);
//		model.setAttribute("newBudget", newBudget);
//		
//		request.unbind(entity, model, "code", "title", "description", "budget", "creationMoment", "startDate", "endDate", "moreInfo");
//	}
//	
//	//Método auxiliar cambio de divisa
//
//	public Money moneyExchangeChimpum(final Rustora c) {
//
//		final String chimpumCurrency = c.getQuota().getCurrency();
//		
//		final AuthenticatedMoneyExchangePerformService moneyExchange = new AuthenticatedMoneyExchangePerformService();
//		final String systemCurrency = this.systemConfigRepository.findSystemConfiguration().getSystemCurrency();
//		final Double conversionAmount;
//
//		if(!systemCurrency.equals(chimpumCurrency)) {
//			MoneyExchange conversion;
//			conversion = moneyExchange.computeMoneyExchange(c.getQuota(), systemCurrency);
//			conversionAmount = conversion.getTarget().getAmount();	
//		}
//		else {
//			conversionAmount = c.getQuota().getAmount();
//		}
//
//		final Money newBudget = new Money();
//		newBudget.setAmount(conversionAmount);
//		newBudget.setCurrency(systemCurrency);
//				
//		return newBudget;
//	}
//
//}