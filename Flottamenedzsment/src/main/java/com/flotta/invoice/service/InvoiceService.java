package com.flotta.invoice.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.aspectj.weaver.NewParentTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.flotta.entity.invoice.DescriptionCategoryCoupler;
import com.flotta.entity.invoice.FeeItem;
import com.flotta.entity.invoice.Invoice;
import com.flotta.entity.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.entity.invoice.Participant;
import com.flotta.entity.invoice.RawFeeItem;
import com.flotta.entity.invoice.RawInvoice;
import com.flotta.entity.record.User;
import com.flotta.invoice.exception.FileUploadException;
import com.flotta.invoice.repository.InvoiceRepository;
import com.flotta.invoice.repository.ParticipantRepository;
import com.flotta.invoice.repository.RawInvoiceRepository;
import com.flotta.service.SubscriptionServiceOnlyInfo;

@Service
public class InvoiceService {

  private RawInvoiceRepository rawInvoiceRepository;

  private InvoiceRepository invoiceRepository;

  private BillTemplateService billTemplateService;

  private FeeItemService feeItemService;

  private SubscriptionServiceOnlyInfo subscriptionInfo;

  private InvoiceByUserAndPhoneNumberService invoiceByUserAndPhoneNumberService;

  private DescriptionCategoryCouplerServiceOnlyGet descriptionCategoryCouplerServiceOnlyGet;
  
  private ParticipantRepository participantRepository;

  @Autowired
  public void setRawInvoiceRepository(RawInvoiceRepository rawInvoiceRepository) {
    this.rawInvoiceRepository = rawInvoiceRepository;
  }

  @Autowired
  public void setInvoiceRepository(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @Autowired
  public void setBillTemplateService(BillTemplateService billTemplateService) {
    this.billTemplateService = billTemplateService;
    billTemplateService.createBasicTemplate();
  }

  @Autowired
  public void setFeeItemService(FeeItemService feeItemService) {
    this.feeItemService = feeItemService;
  }

  @Autowired
  public void setSubscriptionInfo(SubscriptionServiceOnlyInfo subscriptionInfo) {
    this.subscriptionInfo = subscriptionInfo;
  }

  @Autowired
  public void setInvoiceByUserAndPhoneNumberService(InvoiceByUserAndPhoneNumberService invoiceByUserAndPhoneNumberService) {
    this.invoiceByUserAndPhoneNumberService = invoiceByUserAndPhoneNumberService;
  }

  @Autowired
  public void setDescriptionCategoryCouplerServiceOnlyGet(DescriptionCategoryCouplerServiceOnlyGet descriptionCategoryCouplerServiceOnlyGet) {
    this.descriptionCategoryCouplerServiceOnlyGet = descriptionCategoryCouplerServiceOnlyGet;
  }

  @Autowired
  public void setParticipantRepository(ParticipantRepository participantRepository) {
    this.participantRepository = participantRepository;
  }

  public void uploadInvoice(MultipartFile file) throws FileUploadException {
    String xmlString = getXMLString(file);

    RawInvoice rawInvoice = parseXmlStringToRawInvoice(xmlString);
    
    if(invoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()) != null || rawInvoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()) != null) {
      throw new FileUploadException("Already has invoice with this invoice number: " + rawInvoice.getInvoiceNumber());
    } else  {
      processRawInvoice(rawInvoice);
    }
  }
  
  private boolean processRawInvoice(RawInvoice rawInvoice) {
    if(check(rawInvoice)) {
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
    
    if (billTemplateService.invoiceTreeFormalCheck(root)) {
      RawInvoice rawInvoice = new RawInvoice();
      try {
        rawInvoice.setBeginDate(LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
        rawInvoice.setEndDate(LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
        rawInvoice.setInvoiceNumber(getFirstTagValue(root, "InvNb"));
        rawInvoice.setInvoiceNetAmount(Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')));
        rawInvoice.setInvoiceTaxAmount(Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')));
        rawInvoice.setInvoiceGrossAmount(Double.valueOf(getFirstTagValue(root, "InvTotalGrossA").replace(',', '.')));
        
        Element customerData = (Element)root.getElementsByTagName("CustomerData").item(0);
        rawInvoice.setCustomerName(getFirstTagValue(customerData, "Name"));
        rawInvoice.setCustomerAddress(getFirstTagValue(customerData, "City"));
        
        Element companyData = (Element)root.getElementsByTagName("CompanyData").item(0);
        rawInvoice.setCompanyName(getFirstTagValue(companyData, "Name"));
        rawInvoice.setCompanyAddress(getFirstTagValue(companyData, "City"));
        
        NodeList feeItemNodes = root.getElementsByTagName("FeeItem");
        for (int i = 0; i < feeItemNodes.getLength(); i++) {
          Element feeItemElement = (Element) feeItemNodes.item(i);
          rawInvoice.addFeeItem(parseToFeeItem(feeItemElement));
        }
      } catch (DateTimeParseException| NumberFormatException e) {
        throw new FileUploadException(e.getMessage());
      }
      
      return rawInvoice;
    } else {
      throw new FileUploadException("Invalid structure");
    }
  }

  //TODO a különböző hibák visszajelzése, először csak szöveges hibajegyzék, később megoldás ajánlása plusz átirányítás (hiányzó előfizetés -> új felvétele a számmal, hiányzó cég -> felvétele névvel címmel...)
  private boolean check(RawInvoice rawInvoice) {
    Optional<Participant> optionalParticipant = participantRepository.findByName(rawInvoice.getCompanyName());
    if(optionalParticipant.isPresent()) {
      DescriptionCategoryCoupler dcc = optionalParticipant.get().getDescriptionCategoryCoupler();
      for(RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
        if(dcc.getCategoryByDescription(rawFeeItem.getDescription()) == null) {
          rawInvoice.addProblem("Unknown description: " + rawFeeItem.getDescription());
        }
      }
    } else {
      rawInvoice.addProblem("Unknown company, name: " + rawInvoice.getCompanyName());
    }
    for(RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
      if(subscriptionInfo.findByNumber(rawFeeItem.getSubscription()) == null) {
        rawInvoice.addProblem("Unknown phone number: " + rawFeeItem.getSubscription());
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
    
    for(RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
      FeeItem feeItem = parseRawFeeItemToFeeItem(rawFeeItem);
      invoice.addFeeItem(subscriptionInfo.findByNumber(rawFeeItem.getSubscription()), feeItem);
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
//  private Invoice processInvoiceXmlString(String xml) throws FileUploadException {
//    Element root = getTreeFromXMLString(xml);
//
//    if (billTemplateService.invoiceTreeFormalCheck(root)) {
//      Invoice invoice = parseToInvoice(root);
//      invoice.setCategoryOfFees(descriptionCategoryCouplerServiceOnlyGet.findById(1));
//      invoice.setAmountRatioOfFees();
//      invoice.setXmlString(xml);
//      return invoice;
//
//    } else {
//      throw new FileUploadException("Invalid structure");
//    }
//  }

//  private Invoice parseToInvoice(Element root) throws FileUploadException {
//    try {
//      Invoice invoice = new Invoice();
//      invoice.setBeginDate(LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
//      invoice.setEndDate(LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
//      invoice.setInvoiceNumber(getFirstTagValue(root, "InvNb"));
//      invoice.setInvoiceNetAmount(Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')));
//      invoice.setInvoiceTaxAmount(Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')));
//      invoice.setInvoiceGrossAmount(Double.valueOf(getFirstTagValue(root, "InvTotalGrossA").replace(',', '.')));
//      
//      invoice.setCompanyData(parseToParticipant((Element)root.getElementsByTagName("CompanyData").item(0)));
//      
//      NodeList nodes = root.getElementsByTagName("FeeItem");
//        for (int i = 0; i < nodes.getLength(); i++) {
//          Element feeItem = (Element) nodes.item(i);
//          invoice.addFeeItem(
//            subscriptionInfo.findByNumber(getFirstTagValue(feeItem, "ItemNr")),
//            parseToFeeItem(feeItem));
//        }
//      return invoice;
//    } catch (DateTimeParseException | NumberFormatException e) {
//      throw new FileUploadException(e.toString());
//    }
//  }

  private RawFeeItem parseToFeeItem(Element feeItemElement) {
    return new RawFeeItem(
        getFirstTagValue(feeItemElement, "ItemNr"), 
        getFirstTagValue(feeItemElement, "Desc"), 
        LocalDate.parse(getFirstTagValue(feeItemElement, "Begin"),
            DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
        LocalDate.parse(getFirstTagValue(feeItemElement, "End"),
            DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
        Double.valueOf(getFirstTagValue(feeItemElement, "NetA").replace(',', '.')), 
        Double.valueOf(getFirstTagValue(feeItemElement, "TaxA").replace(',', '.')),
        Double.valueOf(getFirstTagValue(feeItemElement, "TaxP").replace(',', '.').replace("%", "")),
        Double.valueOf(getFirstTagValue(feeItemElement, "GrossA").replace(',', '.')));
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

  public List<Invoice> findAll() {
    return invoiceRepository.findAll();
  }

  public Invoice findByInvoiceNumber(String invoiceNumber) {
    return invoiceRepository.findByInvoiceNumber(invoiceNumber);
  }

  public Invoice findById(long id) {
    return invoiceRepository.findById(id).orElse(null);
  }

//  public void save(Invoice invoice) {
//    invoiceRepository.save(invoice);
//  }

//  public List<OneCategoryOfUserFinance> getFinanceByUserId(long id) {
//    return feeItemService.getFinanceByUserId(id);
//  }

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

//  public boolean acceptInvoiceOfCurrentUserByNumber(User user, String number) {
//    return feeItemService.acceptInvoiceOfCurrentUserByNumber(user, number);
//  }

  public boolean acceptInvoicesOfUserByInvoiceNumbersAndSubscription(User user, List<Long> ids) {
    return invoiceByUserAndPhoneNumberService.acceptInvoicesOfUserByInvoiceNumbersAndSubscription(user, ids);
  }

  public void askForRevision(User user, long id, Map<String, String> map) {
    invoiceByUserAndPhoneNumberService.askForRevision(user, id, map);
  }

  public void resetInvoiceByInvoiceNumber(String invoiceNumber) {
    RawInvoice rawInvoice = rawInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    
    if(rawInvoice != null) {
      rawInvoice.clearProblem();
      if(processRawInvoice(rawInvoice)) {
        rawInvoiceRepository.delete(rawInvoice);
      }
    }
  }

  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    if (invoice.canDelete()) {
      invoiceRepository.delete(invoice);
    }
  }

  public void acceptInvoiceByInvoiceNumber(String invoiceNumber) {
    Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    if (invoice != null) {
      invoice.setAcceptedByCompany();
      invoiceRepository.save(invoice);
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
    DescriptionCategoryCoupler dcc = descriptionCategoryCouplerServiceOnlyGet.findById(descriptionCategoryCouplerId);
    Optional<Participant> result = participantRepository.findByName(participant.getName());
    if(!result.isPresent()) {
      participant.setDescriptionCategoryCoupler(dcc);
      participantRepository.save(participant);
    }
    return !result.isPresent();
  }

  public List<String> findDescriptionsOfInvoiceById(long id) {
    Optional<Invoice> optional = invoiceRepository.findById(id);
    if(optional.isPresent()) {
      return optional.get().getAllDescription();
    }
    return new LinkedList<>();
  }
}
