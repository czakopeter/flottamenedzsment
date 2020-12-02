package com.flotta.service.invoice;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.flotta.exception.invoice.FileUploadException;
import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.Invoice;
import com.flotta.model.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.model.invoice.Participant;
import com.flotta.model.invoice.RawInvoice;
import com.flotta.model.registry.User;

@Service
public class InvoiceManager {
  private InvoiceService invoiceService;

  private InvoiceByUserAndPhoneNumberService invoiceByUserAndPhoneNumberService;

  private FeeItemService feeItemService;

  private CategoryService categoryService;

  private ChargeRatioService chargeRatioService;

  private DescriptionCategoryCouplerService descriptionCategoryCouplerService;

  private ParticipantService participantService;

  @Autowired
  public InvoiceManager(InvoiceService invoiceService, InvoiceByUserAndPhoneNumberService invoiceByUserAndPhoneNumberService, FeeItemService feeItemService, CategoryService categoryService, ChargeRatioService chargeRatioService, DescriptionCategoryCouplerService descriptionCategoryCouplerService, ParticipantService participantService) {
    this.invoiceService = invoiceService;
    this.invoiceByUserAndPhoneNumberService = invoiceByUserAndPhoneNumberService;
    this.feeItemService = feeItemService;
    this.categoryService = categoryService;
    this.chargeRatioService = chargeRatioService;
    this.descriptionCategoryCouplerService = descriptionCategoryCouplerService;
    this.participantService = participantService;
  }
  
//-------- INVOICE SERVICE --------
  
  public void uploadInvoice(MultipartFile file) throws FileUploadException {
    invoiceService.uploadInvoice(file);
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
  
  public List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberService.getPendingInvoicesOfUser(user);
  }

  public Optional<InvoiceByUserAndPhoneNumber> findInvoiceOfUserById(User user, Long id) {
    return invoiceByUserAndPhoneNumberService.findInvoiceOfUserById(user, id);
  }

  public void acceptInvoicesOfUserByUserAndEmailAndInvoiceIds(User user, List<Long> ids) {
    invoiceByUserAndPhoneNumberService.acceptInvoicesOfUserByUserAndIds(user, ids);
  }

  public void askRevisionOfInvoiceByUser(User user, long id, Map<String, String> map) {
    invoiceByUserAndPhoneNumberService.askRevisionOfInvoiceByUser(user, id, map);
  }
  
  public List<InvoiceByUserAndPhoneNumber> findInvoicesOfUserByEmail(User user) {
    return invoiceByUserAndPhoneNumberService.findInvoicesOfUserByEmail(user);
  }
  
//-------- FEE ITEM SERVICE --------
  
  public void modifyFeeItemGrossAmountRatio(long id, double userAmount, double compAmount) {
    feeItemService.modifyGrossAmountRatio(id, userAmount, compAmount);
  }
  
//-------- CATEGORY SERVICE --------
  
  public List<Category> findAllCategory() {
    return categoryService.findAll();
  }

  public Category createOrModifyCategory(long id, String name) {
      return categoryService.createOrModifyCategory(id, name);
  }
  
//-------- DESCRIPTION CATEGORY COUPLER SERVICE --------
  
  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return descriptionCategoryCouplerService.findAll();
  }

  public void updateDescriptionCategoryCoupler(long id, List<String> descriptions, List<Long> categoryIds, boolean available) {
    List<Category> categories = categoryService.findAllCategoryByIds(categoryIds);
    descriptionCategoryCouplerService.updateDescriptionCategoryCoupler(id, descriptions, categories, available);
  }
  
  public Optional<DescriptionCategoryCoupler> findDescriptionCategoryCoupler(long id) {
    return descriptionCategoryCouplerService.findById(id);
  }
  
  public boolean createDescriptionCategoryCoupler(DescriptionCategoryCoupler dcc) {
    return descriptionCategoryCouplerService.createDescriptionCategoryCoupler(dcc);
  }
  
  
//-------- CHARGE RATIO BY CATEGORY SERVICE --------
  
  public boolean createChargeRatio(ChargeRatioByCategory chargeRatio) {
    return chargeRatioService.addChargeRatio(chargeRatio);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return chargeRatioService.findAll();
  }

  public Optional<ChargeRatioByCategory> findChargeRatioById(long id) {
    return chargeRatioService.findChargeRatioById(id);
  }

  public boolean updateChargeRatio(long id, List<Long> categoryIds, List<Integer> ratios) {
    List<Category> categories = categoryService.findAllCategoryByIds(categoryIds);
    return chargeRatioService.updateChargeRatio(id, categories, ratios);
  }

  public List<Category> findAllUnusedCategoryOfChargeRatio(long id) {
    List<Category> result = new LinkedList<>(categoryService.findAll());
    Optional<ChargeRatioByCategory> optional = chargeRatioService.findChargeRatioById(id);
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

  public boolean createParticipant(Participant participant, long descriptionCategoryCouplerId) {
      return participantService.create(participant, descriptionCategoryCouplerService.findById(descriptionCategoryCouplerId));
  }
  
  public void updateParticipant(Participant participant) {
    participantService.update(participant);
  }

}
