package com.flotta.service.invoice;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.flotta.enums.Availability;
import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.Invoice;
import com.flotta.model.invoice.GroupedFeeItems;
import com.flotta.model.invoice.Participant;
import com.flotta.model.invoice.RawInvoice;
import com.flotta.model.registry.User;
import com.flotta.utility.BooleanWithMessages;

@Service
public class InvoiceManager {
  private InvoiceService invoiceService;

  private GroupedFeeItemsService groupedFeeItemsService;

  private FeeItemService feeItemService;

  private CategoryService categoryService;

  private ChargeRatioService chargeRatioService;

  private DescriptionCategoryCouplerService descriptionCategoryCouplerService;

  private ParticipantService participantService;

  @Autowired
  public InvoiceManager(InvoiceService invoiceService, GroupedFeeItemsService groupedFeeItemsService, FeeItemService feeItemService, CategoryService categoryService, ChargeRatioService chargeRatioService, DescriptionCategoryCouplerService descriptionCategoryCouplerService, ParticipantService participantService) {
    this.invoiceService = invoiceService;
    this.groupedFeeItemsService = groupedFeeItemsService;
    this.feeItemService = feeItemService;
    this.categoryService = categoryService;
    this.chargeRatioService = chargeRatioService;
    this.descriptionCategoryCouplerService = descriptionCategoryCouplerService;
    this.participantService = participantService;
  }
  
//-------- INVOICE SERVICE --------
  
  public BooleanWithMessages uploadInvoice(MultipartFile file) {
    return invoiceService.uploadInvoice(file);
  }
  
  public List<Invoice> findAllInvoice() {
    return invoiceService.findAll();
  }
  
  public Optional<Invoice> findInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceService.findByInvoiceNumber(invoiceNumber);
  }
  
  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.deleteInvoiceByInvoiceNumber(invoiceNumber);
  }
  
  public void acceptInvoiceByInvoiceNumberFromCompany(String invoiceNumber) {
    invoiceService.acceptInvoiceByInvoiceNumberFromCompany(invoiceNumber);
  }
  
//-------- RAW INVOICE SERVICE --------
  
  public List<RawInvoice> findAllRawInvoice() {
    return invoiceService.findAllRawInvoice();
  }
  
  public void deleteRawInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.deleteRawInvoiceByInvoiceNumber(invoiceNumber);
  }
  
  public List<String> findDescriptionsOfInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceService.findDescriptionsOfInvoiceByInvoiceNumber(invoiceNumber);
  }
  
  public void restartProcessingRawInvoiceBy(String invoiceNumber) {
    invoiceService.restartProcessingRawInvoiceByInvoiceNumber(invoiceNumber);
  }

//-------- INVOICE BY USER AND PHONE NUMBER SERVICE --------
  
  public Optional<GroupedFeeItems> findGroupedFeeItemsByUserAndId(User user, Long id) {
    return groupedFeeItemsService.findByUserAndId(user, id);
  }

  public void acceptGroupedFeeItemsOfUserByIdsFromUser(User user, List<Long> ids) {
    groupedFeeItemsService.acceptByUserAndIdsFromUser(user, ids);
  }

  public void askForRevision(User user, long id, Map<String, String> notes) {
    groupedFeeItemsService.askForRevision(user, id, notes);
  }
  
  public List<GroupedFeeItems> findAllGroupedFeeItemsByUser(User user) {
    return groupedFeeItemsService.findAllByUser(user);
  }
  
//-------- FEE ITEM SERVICE --------
  
  public void modifyFeeItemGrossAmountRatio(long id, double userAmount) {
    feeItemService.modifyGrossAmountRatio(id, userAmount);
  }
  
//-------- CATEGORY SERVICE --------
  
  public List<Category> findAllCategory() {
    return categoryService.findAll();
  }

  public Category createOrModifyCategory(long id, String name) {
      return categoryService.createOrModify(id, name);
  }
  
//-------- DESCRIPTION CATEGORY COUPLER SERVICE --------
  
  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return descriptionCategoryCouplerService.findAll();
  }

  public Optional<DescriptionCategoryCoupler> findDescriptionCategoryCoupler(long id) {
    return descriptionCategoryCouplerService.findById(id);
  }
  
  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCouplerByAvailability(Availability availability) {
    return descriptionCategoryCouplerService.findAllByAvailability(availability);
  }
  
  public BooleanWithMessages createDescriptionCategoryCoupler(DescriptionCategoryCoupler dcc) {
    return descriptionCategoryCouplerService.create(dcc);
  }
  
  public boolean updateDescriptionCategoryCoupler(DescriptionCategoryCoupler coupler, List<String> descriptions, List<Long> categoryIds) {
    List<Category> categories = categoryService.findAllByIds(categoryIds);
    return descriptionCategoryCouplerService.update(coupler, descriptions, categories);
  }
  
  
//-------- CHARGE RATIO BY CATEGORY SERVICE --------
  
  public BooleanWithMessages createChargeRatio(ChargeRatioByCategory chargeRatio) {
    return chargeRatioService.create(chargeRatio);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return chargeRatioService.findAll();
  }

  public Optional<ChargeRatioByCategory> findChargeRatioById(long id) {
    return chargeRatioService.findById(id);
  }
  
  public List<ChargeRatioByCategory> findAllChargeRatioByAvailability(Availability availability) {
    return chargeRatioService.findAllByAvailability(availability);
  }

  public boolean updateChargeRatio(ChargeRatioByCategory chargeRatio, List<Long> categoryIds, List<Integer> ratios) {
    List<Category> categories = categoryService.findAllByIds(categoryIds);
    return chargeRatioService.update(chargeRatio, categories, ratios);
  }

  public List<Category> findAllUnusedCategoryOfChargeRatio(long id) {
    List<Category> result = new LinkedList<>(categoryService.findAll());
    Optional<ChargeRatioByCategory> optional = chargeRatioService.findById(id);
    if (optional.isPresent()) {
      result.removeAll(optional.get().getCategoryRatioMap().keySet());
    }
    return result;
  }
  
//-------- PARTICIPANT SERVICE --------
  
  public List<Participant> findAllParticipant() {
    return participantService.findAll();
  }

  public Optional<Participant> findParticipantById(long id) {
    return participantService.findById(id);
  }

  public BooleanWithMessages createParticipant(Participant participant) {
      return participantService.create(participant);
  }
  
  public void updateParticipant(Participant participant) {
    participantService.update(participant);
  }

}
