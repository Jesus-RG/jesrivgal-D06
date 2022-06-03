//package acme.features.inventor.rustora;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import acme.entities.item.Item;
//import acme.entities.rustora.Rustora;
//import acme.features.inventor.item.InventorItemRepository;
//import acme.framework.components.models.Model;
//import acme.framework.controllers.Errors;
//import acme.framework.controllers.Request;
//import acme.framework.services.AbstractDeleteService;
//import acme.roles.Inventor;
//
//@Service
//public class InventorChimpumDeleteService  implements AbstractDeleteService<Inventor,Rustora>  {
//
//	@Autowired
//	protected InventorChimpumRepository inventorChimpumRepository;
//	@Autowired
//	protected InventorItemRepository inventorItemRepository;
//	
//	@Override
//	public boolean authorise(final Request<Rustora> request) {
//		assert request != null;
//
//		return true;
//	}
//
//	@Override
//	public void bind(final Request<Rustora> request, final Rustora entity, final Errors errors) {
//		request.bind(entity, errors, "code", "title", "description", "budget", "creationMoment", "startDate", "endDate", "moreInfo");
//	}
//
//	@Override
//	public void unbind(final Request<Rustora> request, final Rustora entity, final Model model) {
//		request.unbind(entity, model, "code", "title", "description", "budget", "creationMoment", "startDate", "endDate", "moreInfo");
//	}
//
//	@Override
//	public Rustora findOne(final Request<Rustora> request) {
//		assert request != null;
//		Rustora res;
//		int chimpumId;
//
//		chimpumId = request.getModel().getInteger("id");
//		res = this.inventorChimpumRepository.findChimpumById(chimpumId);
//
//		return res;
//	}
//
//	@Override
//	public void validate(final Request<Rustora> request, final Rustora entity, final Errors errors) {
//		assert request != null;
//		assert entity != null;
//		assert errors != null;
//	}
//
//	@Override
//	public void delete(final Request<Rustora> request, final Rustora entity) {
//		assert request != null;
//		assert entity != null;
//		Item item;
//		
//		item = this.inventorChimpumRepository.findItemByChimpumId(entity.getId());
//		item.setChimpum(null);
//		this.inventorItemRepository.save(item);
//		
//		this.inventorChimpumRepository.delete(entity);
//	}
//}
