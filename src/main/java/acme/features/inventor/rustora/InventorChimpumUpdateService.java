//package acme.features.inventor.rustora;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import acme.entities.item.Item;
//import acme.entities.rustora.Rustora;
//import acme.entities.systemConfiguration.SystemConfiguration;
//import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
//import acme.framework.components.models.Model;
//import acme.framework.controllers.Errors;
//import acme.framework.controllers.Request;
//import acme.framework.services.AbstractUpdateService;
//import acme.roles.Inventor;
//import spamDetector.SpamDetector;
//
//@Service
//public class InventorChimpumUpdateService implements AbstractUpdateService<Inventor,Rustora>{
//	
//	@Autowired
//	protected InventorChimpumRepository inventorChimpumRepository;
//	@Autowired
//	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;
//
//	@Override
//	public boolean authorise(final Request<Rustora> request) {
//		assert request != null;
//		boolean res;
//		
//		Item item;
//		int chimpumId;
//
//		chimpumId = request.getModel().getInteger("id");
//		item = this.inventorChimpumRepository.findItemByChimpumId(chimpumId);
//		res = item.getInventor().getId() == request.getPrincipal().getActiveRoleId();
//		
//		return res;
//	}
//
//	@Override
//	public void bind(final Request<Rustora> request, final Rustora entity, final Errors errors) {
//		assert request != null;
//		assert entity != null;
//		assert errors != null;
//		
//		request.bind(entity, errors, "code", "title", "description","creationMoment", "budget", "startDate", "endDate", "moreInfo");
//	}
//
//	@Override
//	public void unbind(final Request<Rustora> request, final Rustora entity, final Model model) {
//		assert request != null;
//		assert entity != null;
//		assert model != null;
//		
//		request.unbind(entity, model, "code", "title", "description","creationMoment", "budget", "startDate", "endDate", "moreInfo");	
//	}
//
//	@Override
//	public Rustora findOne(final Request<Rustora> request) {
//		assert request != null;
//		
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
//
//        final SystemConfiguration systemConfig = this.systemConfigRepository.findSystemConfiguration();
//        final String StrongEN = systemConfig.getStrongSpamTermsEn();
//        final String StrongES = systemConfig.getStrongSpamTermsEs();
//        final String WeakEN = systemConfig.getWeakSpamTermsEn();
//        final String WeakES = systemConfig.getWeakSpamTermsEs();
//
//        final double StrongThreshold = systemConfig.getStrongThreshold();
//        final double WeakThreshold = systemConfig.getWeakThreshold();
//		
//        if(!errors.hasErrors("title")) {
//            final boolean res;
//            res = SpamDetector.spamDetector(entity.getTitle(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
//            errors.state(request, res, "title", "alert-message.form.spam");
//        }
//        
//        if(!errors.hasErrors("description")) {
//            final boolean res;
//            res = SpamDetector.spamDetector(entity.getDescription(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
//            errors.state(request, res, "description", "alert-message.form.spam");
//        }
//        
//        if(!errors.hasErrors("moreInfo")) {
//            final boolean res;
//            res = SpamDetector.spamDetector(entity.getMoreInfo(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
//            errors.state(request, res, "moreInfo", "alert-message.form.spam");
//        }
//		
//		if(!errors.hasErrors("budget")) {
//			final List<String> currencies = new ArrayList<>();
//			String currency;
//			Double amount;
//			
//			for(final String c: this.systemConfigRepository.acceptedCurrencies().split(",")) {
//				currencies.add(c.trim());
//			}
//			
//			currency = entity.getBudget().getCurrency();
//			amount = entity.getBudget().getAmount();
//			
//			errors.state(request, currencies.contains(currency) , "budget","inventor.chimpum.form.error.currency");
//			errors.state(request, amount>=0.00 , "budget","inventor.chimpum.form.error.negative-amount");
//		}
//		
//		if(entity.getStartDate()!=null) {
//			if(!errors.hasErrors("startDate")) {
//				Date startDate;
//				startDate = entity.getStartDate();
//
//				final long diff1 = startDate.getTime() - entity.getCreationMoment().getTime();
//				final TimeUnit time = TimeUnit.DAYS; 
//		        final long diff2 = time.convert(diff1, TimeUnit.MILLISECONDS);
//		        
//		        errors.state(request, diff2>=30 , "startDate","inventor.chimpum.form.error.startDate");
//			}
//			
//			if(!errors.hasErrors("endDate")) {
//				Date endDate;
//				endDate = entity.getEndDate();
//				
//				final long diff1 = endDate.getTime() - entity.getStartDate().getTime();
//			    final TimeUnit time = TimeUnit.DAYS; 
//			    final long diff2 = time.convert(diff1, TimeUnit.MILLISECONDS);
//			        
//			    errors.state(request, diff2>=7 , "endDate","inventor.chimpum.form.error.endDate");
//			}
//		}
//	}
//
//	@Override
//	public void update(final Request<Rustora> request, final Rustora entity) {
//		assert request != null;
//		assert entity != null;
//
//		this.inventorChimpumRepository.save(entity);
//	}
//	
//	
//	public String generateCode(final LocalDate creationMoment) {
//		String result = "";
//		
//		final Integer day1 = creationMoment.getDayOfMonth();
//		final Integer month1 = creationMoment.getMonthValue();
//		final Integer year1 = creationMoment.getYear();
//
//		final String year2 = year1.toString().substring(2, 4);
//		String month2= "";
//		String day2= "";
//		
//		if(day1.toString().length()==1) {
//			day2 = "0" + day1.toString();
//		}else {
//			day2 = day1.toString();
//		}
//		
//		if(month1.toString().length()==1) {
//			month2 = "0" + month1.toString();
//		}else{
//			month2 = month1.toString();
//		}
//					
//		result = year2 + "-" + month2 + "-" + day2;
//			
//		return result;
//	}
//}
