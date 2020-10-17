package com.flotta.invoice.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.flotta.entity.Subscription;
import com.flotta.entity.User;
import com.flotta.entity.viewEntity.InvoiceOfUserByNumber;
import com.flotta.entity.viewEntity.OneCategoryOfUserFinance;
import com.flotta.invoice.Category;
import com.flotta.invoice.ChargeRatioByCategory;
import com.flotta.invoice.DescriptionCategoryCoupler;
import com.flotta.invoice.FeeItem;
import com.flotta.invoice.Invoice;
import com.flotta.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.invoice.Participant;
import com.flotta.invoice.RawInvoice;
import com.flotta.invoice.exception.FileUploadException;

@Service
public class BillingService {

  private InvoiceService invoiceService;
  
  private CategoryService categoryService;
  
  private DescriptionCategoryCouplerService descriptionCategoryCouplerService;
  
  private ChargeRatioService chargeRatioService;

  @Autowired
  public void setInvoiceService(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }
  
  @Autowired
  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Autowired
  public void setBillPartitionTemplateService(DescriptionCategoryCouplerService descriptionCategoryCouplerService) {
    this.descriptionCategoryCouplerService = descriptionCategoryCouplerService;
  }
  
  @Autowired
  public void setChargeRatioService(ChargeRatioService chargeRatioService ) {
    this.chargeRatioService = chargeRatioService;
  }
  
  // beolvassa a számlát
  // valid akkor konvertál validFeeItem-re
  // newm valid új template készítése
  public boolean uploadInvoiceAndProcess(MultipartFile file) throws FileUploadException {
    invoiceService.uploadInvoice(file);
    return true;
  }

  List<FeeItem> getFeeItemsOfUser(User user) {
    return null;
  }

  List<FeeItem> getFeeItemsOfUser(User user, LocalDate begin, LocalDate end) {
    return null;
  }

  void connectValidFeeItemToCategory(FeeItem item, String category) {

  }

  public List<Invoice> findAllInvoice() {
    return invoiceService.findAll();
  }

  public Invoice findInvoicedByInvoiceNumber(String invoiceNumber) {
    return invoiceService.findByInvoiceNumber(invoiceNumber);
  }

  public List<Category> findAllCategory() {
    return categoryService.findAll();
  }

  public boolean addCategory(String category) {
    return categoryService.save(category);
  }

  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return  descriptionCategoryCouplerService.findAll();
  }
  
//  public List<FeeItem> findAllFeeItemByBillId(long id) {
//    return invoiceService.findAllFeeItemByInvoiceId(id);
//  }
  
  public Invoice findBillById(long id) {
    return invoiceService.findById(id);
  }

  public boolean billPartitionByTemplateId(long billId, long templateId) {
    //TODO missing bill or template problem throw exception
    Invoice invoice = invoiceService.findById(billId);
    if(invoice != null) {
      Map<Category, List<FeeItem> >  result =  descriptionCategoryCouplerService.partition(invoice, templateId);
      invoiceService.save(invoice);
      return result == null ?  false : true;
    }
    return false;
  }
  
  public List<String> getUnknownFeeDescToTemplate(long templateId) {
    return descriptionCategoryCouplerService.getMissingFeeItemDescription(templateId);
  }

  public void upgradeDescriptionCategoryCoupler(long id, List<String> descriptions, List<Long> categories) {
    descriptionCategoryCouplerService.upgradeDescriptionCategoryCoupler(id, descriptions, idListToCategoryList(categories));
  }
  
  private List<Category> idListToCategoryList(List<Long> catIds) {
    List<Category> result = new LinkedList<>();
    for(long id : catIds) {
      result.add(categoryService.findById(id));
    }
    return result;
  }

  public void save(Invoice invoice) {
    invoiceService.save(invoice);
  }

//  public List<OneCategoryOfUserFinance> getFinanceByUserId(long id) {
//    return invoiceService.getFinanceByUserId(id);
//  }

  public void save(List<FeeItem> fees) {
    invoiceService.save(fees);
  }

  public DescriptionCategoryCoupler findBillPartitionTemplateById(long id) {
    return descriptionCategoryCouplerService.findById(id);
  }

  public List<String> findAllBillDescription() {
    return descriptionCategoryCouplerService.findAllInvoiceDescription();
  }

  public boolean addPayDivision(ChargeRatioByCategory payDevision, List<Long> categories, List<Integer> ratios) {
    return chargeRatioService.addChargeRatio(payDevision, idListToCategoryList(categories), ratios);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return chargeRatioService.findAll();
  }

  public ChargeRatioByCategory findChargeRatioById(long id) {
    return chargeRatioService.findChargeRatioById(id);
  }

  public boolean editChargeRatio(long id, List<Long> categories, List<Integer> ratios) {
    return chargeRatioService.editChargeRatio(id, idListToCategoryList(categories), ratios);
  }

  public List<Category> getUnusedCategoryOfChargeRatio(long id) {
    List<Category> result = new LinkedList<>(categoryService.findAll());
    ChargeRatioByCategory crbc = chargeRatioService.findChargeRatioById(id);
    result.removeAll(crbc.getCategoryRatioMap().keySet());
    return result;
  }

  public InvoiceByUserAndPhoneNumber getPendingInvoiceOfUserById(User user, Long id) {
    return invoiceService.getPendingInvoiceOfUserById(user, id);
  }

  public List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfUser(User user) {
    return invoiceService.getPendingInvoicesOfUser(user);
  }

//  public boolean acceptInvoiceOfUserByNumber(User user, String number) {
//    return invoiceService.acceptInvoiceOfCurrentUserByNumber(user, number);
//  }

  public boolean acceptInvoicesOfUserByInvoiceNumbersAndSubscriptions(User user, List<Long> ids) {
    return invoiceService.acceptInvoicesOfUserByInvoiceNumbersAndSubscription(user, ids);
  }

  public void askForRevision(User user, long id, Map<String, String> map) {
    invoiceService.askForRevision(user, id, map);
  }

  public void resetInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.resetInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.deleteInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void acceptInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.acceptInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void modifyFeeItemGrossAmountRatio(long id, double userAmount, double compAmount) {
    invoiceService.modifyFeeItemGrossAmountRatio(id, userAmount, compAmount);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedInvoicesOfUser(User user) {
    return invoiceService.getAcceptedInvoicesOfUser(user);
  }

  public InvoiceByUserAndPhoneNumber getAcceptedInvoiceOfUserById(User user, long id) {
    return invoiceService.getAcceptedInvoiceOfUserById(user, id);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedByCompanyInvoicesOfUser(User user) {
    return invoiceService.getAcceptedByCompanyInvoicesOfUser(user);
  }

  public List<RawInvoice> findAllRawInvoice() {
    return invoiceService.findAllRawInvoice();
  }

  public List<Participant> findAllParticipant() {
    return invoiceService.findAllParticipant();
  }

  public Optional<Participant> findParticipantById(long id) {
    return invoiceService.findParticipantById(id);
  }
  
}
