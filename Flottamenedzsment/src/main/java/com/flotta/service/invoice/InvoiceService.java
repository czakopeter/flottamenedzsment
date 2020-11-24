package com.flotta.service.invoice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.flotta.entity.invoice.Category;
import com.flotta.entity.invoice.ChargeRatioByCategory;
import com.flotta.entity.invoice.DescriptionCategoryCoupler;
import com.flotta.entity.invoice.FeeItem;
import com.flotta.entity.invoice.Invoice;
import com.flotta.entity.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.entity.invoice.Participant;
import com.flotta.entity.invoice.RawFeeItem;
import com.flotta.entity.invoice.RawInvoice;
import com.flotta.entity.record.Subscription;
import com.flotta.entity.record.User;
import com.flotta.exception.invoice.FileUploadException;
import com.flotta.repository.invoice.InvoiceRepository;
import com.flotta.repository.invoice.ParticipantRepository;
import com.flotta.repository.invoice.RawInvoiceRepository;
import com.flotta.service.record.SubscriptionServiceOnlyInfo;

/**
 * @author CzP
 *
 */
@Service
public class InvoiceService {

  private RawInvoiceRepository rawInvoiceRepository;

  private InvoiceRepository invoiceRepository;

  private InvoiceByUserAndPhoneNumberService invoiceByUserAndPhoneNumberService;

  private FeeItemService feeItemService;

  private InvoiceTemplateService invoiceTemplateService;

  private SubscriptionServiceOnlyInfo subscriptionInfo;

  private CategoryService categoryService;

  private ChargeRatioService chargeRatioService;

  private DescriptionCategoryCouplerService descriptionCategoryCouplerService;

  private ParticipantRepository participantRepository;

  @Autowired
  public InvoiceService(RawInvoiceRepository rawInvoiceRepository, InvoiceRepository invoiceRepository, InvoiceByUserAndPhoneNumberService invoiceByUserAndPhoneNumberService, FeeItemService feeItemService, InvoiceTemplateService invoiceTemplateService, SubscriptionServiceOnlyInfo subscriptionInfo, CategoryService categoryService, ChargeRatioService chargeRatioService, DescriptionCategoryCouplerService descriptionCategoryCouplerService, ParticipantRepository participantRepository) {
    this.rawInvoiceRepository = rawInvoiceRepository;
    this.invoiceRepository = invoiceRepository;
    this.invoiceByUserAndPhoneNumberService = invoiceByUserAndPhoneNumberService;
    this.feeItemService = feeItemService;
    this.invoiceTemplateService = invoiceTemplateService;
    this.subscriptionInfo = subscriptionInfo;
    this.categoryService = categoryService;
    this.chargeRatioService = chargeRatioService;
    this.descriptionCategoryCouplerService = descriptionCategoryCouplerService;
    this.participantRepository = participantRepository;
  }

  public void uploadInvoice(MultipartFile file) throws FileUploadException {
    String xmlString = getXMLString(file);

    RawInvoice rawInvoice = parseXmlStringToRawInvoice(xmlString);

    if (invoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()).isPresent() || rawInvoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()).isPresent()) {
      throw new FileUploadException("Already has invoice with this invoice number: " + rawInvoice.getInvoiceNumber());
    } else {
      processRawInvoice(rawInvoice);
    }
  }

  private boolean processRawInvoice(RawInvoice rawInvoice) {
    if (check(rawInvoice)) {
      Invoice invoice = parseRawInvoiceToInvoice(rawInvoice);
      invoiceRepository.save(invoice);
      return true;
    } else {
      rawInvoiceRepository.save(rawInvoice);
      return false;
    }
  }

  private RawInvoice parseXmlStringToRawInvoice(String xml) throws FileUploadException {
    Element root = getTreeFromXMLString(xml);

    if (invoiceTemplateService.invoiceTreeFormalCheck(root)) {
      RawInvoice rawInvoice = new RawInvoice();
      try {
        rawInvoice.setBeginDate(LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
        rawInvoice.setEndDate(LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
        rawInvoice.setInvoiceNumber(getFirstTagValue(root, "InvNb"));
        rawInvoice.setInvoiceNetAmount(Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')));
        rawInvoice.setInvoiceTaxAmount(Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')));
        rawInvoice.setInvoiceGrossAmount(Double.valueOf(getFirstTagValue(root, "InvTotalGrossA").replace(',', '.')));

        Element customerData = (Element) root.getElementsByTagName("CustomerData").item(0);
        rawInvoice.setCustomerName(getFirstTagValue(customerData, "Name"));
        rawInvoice.setCustomerAddress(getFirstTagValue(customerData, "City"));

        Element companyData = (Element) root.getElementsByTagName("CompanyData").item(0);
        rawInvoice.setCompanyName(getFirstTagValue(companyData, "Name"));
        rawInvoice.setCompanyAddress(getFirstTagValue(companyData, "City"));

        NodeList feeItemNodes = root.getElementsByTagName("FeeItem");
        for (int i = 0; i < feeItemNodes.getLength(); i++) {
          Element feeItemElement = (Element) feeItemNodes.item(i);
          rawInvoice.addFeeItem(parseToFeeItem(feeItemElement));
        }
      } catch (DateTimeParseException | NumberFormatException e) {
        throw new FileUploadException(e.getMessage());
      }

      return rawInvoice;
    } else {
      throw new FileUploadException("Invalid structure");
    }
  }

  // TODO a különböző hibák visszajelzése, először csak szöveges hibajegyzék,
  // később megoldás ajánlása plusz átirányítás (hiányzó előfizetés -> új
  // felvétele a számmal, hiányzó cég -> felvétele névvel címmel...)
  private boolean check(RawInvoice rawInvoice) {
    Optional<Participant> optionalParticipant = participantRepository.findByName(rawInvoice.getCompanyName());
    if (optionalParticipant.isPresent()) {
      DescriptionCategoryCoupler dcc = optionalParticipant.get().getDescriptionCategoryCoupler();
      for (RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
        if (dcc.getCategoryByDescription(rawFeeItem.getDescription()) == null) {
          rawInvoice.addProblem("Unknown description: " + rawFeeItem.getDescription());
        }
      }
    } else {
      rawInvoice.addProblem("Unknown company, name: " + rawInvoice.getCompanyName());
    }
    Set<User> users = new HashSet<>();
    for (RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
      Optional<Subscription> optionalSubscription = subscriptionInfo.findByNumber(rawFeeItem.getSubscription());
      if (!optionalSubscription.isPresent()) {
        rawInvoice.addProblem("Unknown phone number: " + rawFeeItem.getSubscription());
      } else {
        users.addAll(optionalSubscription.get().getUsersBetween(rawFeeItem.getBeginDate(), rawFeeItem.getEndDate()));
      }
    }
    for(User user : users) {
      if(user.getChargeRatio() == null) {
        rawInvoice.addProblem(user.getEmail() + " don't have charge ratio");
      }
    }
    return !rawInvoice.hasProblem();
  }

  private Invoice parseRawInvoiceToInvoice(RawInvoice rawInvoice) {
    Invoice invoice = new Invoice();

    invoice.setInvoiceNumber(rawInvoice.getInvoiceNumber());
    invoice.setBeginDate(rawInvoice.getBeginDate());
    invoice.setEndDate(rawInvoice.getEndDate());
    invoice.setInvoiceNetAmount(rawInvoice.getInvoiceNetAmount());
    invoice.setInvoiceTaxAmount(rawInvoice.getInvoiceTaxAmount());
    invoice.setInvoiceGrossAmount(rawInvoice.getInvoiceGrossAmount());
    invoice.setCompany(participantRepository.findByName(rawInvoice.getCompanyName()).get());

    for (RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
      FeeItem feeItem = parseRawFeeItemToFeeItem(rawFeeItem);
      Subscription subscription = subscriptionInfo.findByNumber(feeItem.getSubscription()).get();
      invoice.processAndAddFeeItem(subscription, feeItem);
    }
    return invoice;
  }

  private FeeItem parseRawFeeItemToFeeItem(RawFeeItem rawFeeItem) {
    FeeItem feeItem = new FeeItem();
    feeItem.setBeginDate(rawFeeItem.getBeginDate());
    feeItem.setEndDate(rawFeeItem.getEndDate());
    feeItem.setNetAmount(rawFeeItem.getNetAmount());
    feeItem.setDescription(rawFeeItem.getDescription());
    feeItem.setSubscription(rawFeeItem.getSubscription());
    feeItem.setTaxAmount(rawFeeItem.getTaxAmount());
    feeItem.setTaxPercentage(rawFeeItem.getTaxPercentage());
    feeItem.setTotalGrossAmount(rawFeeItem.getGrossAmount());
    return feeItem;
  }
  
  private RawFeeItem parseToFeeItem(Element feeItemElement) {
    return new RawFeeItem(getFirstTagValue(feeItemElement, "ItemNr"), getFirstTagValue(feeItemElement, "Desc"), LocalDate.parse(getFirstTagValue(feeItemElement, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")), LocalDate.parse(getFirstTagValue(feeItemElement, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")), Double.valueOf(getFirstTagValue(feeItemElement, "NetA").replace(',', '.')), Double.valueOf(getFirstTagValue(feeItemElement, "TaxA").replace(',', '.')), Double.valueOf(getFirstTagValue(feeItemElement, "TaxP").replace(',', '.').replace("%", "")), Double.valueOf(getFirstTagValue(feeItemElement, "GrossA").replace(',', '.')));
  }

  private String getFirstTagValue(Element root, String tagname) {
    return root.getElementsByTagName(tagname).item(0).getFirstChild().getNodeValue();
  }

  private Element getTreeFromXMLString(String xmlString) throws FileUploadException {
    // TODO convert to utf-8
    try {
      DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));

      doc.getDocumentElement().normalize();

      return doc.getDocumentElement();
    } catch (ParserConfigurationException e) {
      throw new FileUploadException("ParserConfigurationException");
    } catch (IOException e) {
      throw new FileUploadException("IOException");
    } catch (SAXException e) {
      throw new FileUploadException("SAXException\n" + e);
    }
  }

  private String getXMLString(MultipartFile file) throws FileUploadException {
    if (file.isEmpty()) {
      throw new FileUploadException("Empty file");
    }

    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }

      return sb.toString().replaceAll(">\\s+<", "><");
    } catch (IOException e) {
      throw new FileUploadException("IOException");
    }
  }

  public List<Invoice> findAllInvoice() {
    return invoiceRepository.findAll();
  }

  public Optional<Invoice> findInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceRepository.findByInvoiceNumber(invoiceNumber);
  }

  public Invoice findById(long id) {
    return invoiceRepository.findById(id).orElse(null);
  }

  public void save(List<FeeItem> fees) {
    feeItemService.save(fees);
  }

  public List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberService.getPendingInvoicesOfUser(user);
//    return feeItemService.getPendingInvoicesOfCurrentUser(user);
  }

  public InvoiceByUserAndPhoneNumber getPendingInvoiceOfUserById(User user, Long id) {
    return invoiceByUserAndPhoneNumberService.getPendingInvoiceOfUserById(user, id);
  }

  public boolean acceptInvoicesOfUserByInvoiceNumbersAndSubscription(User user, List<Long> ids) {
    return invoiceByUserAndPhoneNumberService.acceptInvoicesOfUserByInvoiceNumbersAndSubscription(user, ids);
  }

  public void askForRevision(User user, long id, Map<String, String> map) {
    invoiceByUserAndPhoneNumberService.askForRevision(user, id, map);
  }

  public void restartProcessingInvoiceBy(String invoiceNumber) {
    Optional<RawInvoice> optional = rawInvoiceRepository.findByInvoiceNumber(invoiceNumber);

    if (optional.isPresent()) {
      RawInvoice rawInvoice = optional.get();
      rawInvoice.clearProblem();
      if (processRawInvoice(rawInvoice)) {
        rawInvoiceRepository.delete(rawInvoice);
      }
    }
  }

  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<Invoice> invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    if (invoice.isPresent()) {
      invoiceRepository.delete(invoice.get());
    }
  }

  public void acceptInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<Invoice> invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    if (invoice.isPresent()) {
      invoice.get().setAcceptedByCompany();
      invoiceRepository.save(invoice.get());
    }
  }

  public void modifyFeeItemGrossAmountRatio(long id, double userAmount, double compAmount) {
    FeeItem feeItem = feeItemService.getById(id);
    feeItem.setUserGrossAmount(userAmount);
    feeItem.setCompanyGrossAmount(compAmount);
    feeItem.getInvoiceByUserAndPhoneNumber().updateAmountsByFeeItems();
    feeItemService.save(feeItem);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberService.getAcceptedInvoicesOfUser(user);
  }

  public InvoiceByUserAndPhoneNumber getAcceptedInvoiceOfUserById(User user, long id) {
    return invoiceByUserAndPhoneNumberService.getAcceptedInvoiceOfUserById(user, id);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedByCompanyInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberService.getAcceptedByCompanyInvoicesOfUser(user);
  }

  public List<RawInvoice> findAllRawInvoice() {
    return rawInvoiceRepository.findAll();
  }

  public List<Participant> findAllParticipant() {
    return participantRepository.findAll();
  }

  public Optional<Participant> findParticipantById(long id) {
    return participantRepository.findById(id);
  }

  public boolean addParticipant(Participant participant, long descriptionCategoryCouplerId) {
    DescriptionCategoryCoupler dcc = descriptionCategoryCouplerService.findById(descriptionCategoryCouplerId);
    Optional<Participant> result = participantRepository.findByName(participant.getName());
    if (!result.isPresent()) {
      participant.setDescriptionCategoryCoupler(dcc);
      participantRepository.save(participant);
    }
    return !result.isPresent();
  }

  public List<String> findDescriptionsOfInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<Invoice> optionalInvoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    Optional<RawInvoice> optionalRawInvoice = rawInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    if (optionalInvoice.isPresent()) {
      return optionalInvoice.get().getAllDescription();
    }
    if(optionalRawInvoice.isPresent()) {
      return optionalRawInvoice.get().getAllDescription();
    }
    return new LinkedList<>();
  }

  public List<Category> findAllCategory() {
    return categoryService.findAll();
  }

  public Category addOfMofifyCategory(long id, String name) {
    return categoryService.addOfModifyCategory(id, name);
  }

  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return descriptionCategoryCouplerService.findAll();
  }

  public void updateDescriptionCategoryCoupler(long id, List<String> descriptions, List<Long> categories, boolean available) {
    descriptionCategoryCouplerService.updateDescriptionCategoryCoupler(id, descriptions, idListToCategoryList(categories), available);
  }

  private List<Category> idListToCategoryList(List<Long> catIds) {
    List<Category> result = new LinkedList<>();
    for (long id : catIds) {
      result.add(categoryService.findById(id).get());
    }
    return result;
  }

  public DescriptionCategoryCoupler findDescriptionCategoryCoupler(long id) {
    return descriptionCategoryCouplerService.findById(id);
  }

  public List<String> findAllBillDescription() {
    return descriptionCategoryCouplerService.findAllInvoiceDescription();
  }

  public boolean addChargeRatio(ChargeRatioByCategory chargeRatio) {
    return chargeRatioService.addChargeRatio(chargeRatio);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return chargeRatioService.findAll();
  }

  public Optional<ChargeRatioByCategory> findChargeRatioById(long id) {
    return chargeRatioService.findChargeRatioById(id);
  }

  public boolean editChargeRatio(long id, List<Long> categories, List<Integer> ratios) {
    return chargeRatioService.editChargeRatio(id, idListToCategoryList(categories), ratios);
  }

  public List<Category> getUnusedCategoryOfChargeRatio(long id) {
    List<Category> result = new LinkedList<>(categoryService.findAll());
    Optional<ChargeRatioByCategory> optional = chargeRatioService.findChargeRatioById(id);
    if (optional.isPresent()) {
      result.removeAll(optional.get().getCategoryRatioMap().keySet());
    }
    return result;
  }

  public boolean addDescriptionCategoryCoupler(DescriptionCategoryCoupler dcc) {
    return descriptionCategoryCouplerService.descriptionCategoryCoupler(dcc);
  }

  
  public void deleteRawInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<RawInvoice> optional = rawInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    if(optional.isPresent()) {
      rawInvoiceRepository.delete(optional.get());
    }
  }
}