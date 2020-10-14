package com.flotta.invoice.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

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

import com.flotta.entity.User;
import com.flotta.invoice.Participant;
import com.flotta.invoice.RawFeeItem;
import com.flotta.invoice.DescriptionCategoryCoupler;
//import com.flotta.entity.viewEntity.OneCategoryOfUserFinance;
import com.flotta.invoice.FeeItem;
import com.flotta.invoice.Invoice;
import com.flotta.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.invoice.RawInvoice;
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
    
    if(check(rawInvoice)) {
      Invoice invoice = parseRawInvoiceToInvoice(rawInvoice);
      invoiceRepository.save(invoice);
    } else {
      rawInvoiceRepository.save(rawInvoice);
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

  private boolean check(RawInvoice rawInvoice) {
    if(invoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()) != null || rawInvoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()) != null) {
      return false;
    }
    if(participantRepository.findByName(rawInvoice.getCompanyName()) == null) {
      participantRepository.save(new Participant(rawInvoice.getCompanyName(), rawInvoice.getCompanyAddress()));
    }
    return true;
  }
  
  private Invoice parseRawInvoiceToInvoice(RawInvoice rawInvoice) {
    Invoice invoice = new Invoice();
    
    invoice.setInvoiceNumber(rawInvoice.getInvoiceNumber());
    invoice.setBeginDate(rawInvoice.getBeginDate());
    invoice.setEndDate(rawInvoice.getEndDate());
    invoice.setInvoiceNetAmount(rawInvoice.getInvoiceNetAmount());
    invoice.setInvoiceTaxAmount(rawInvoice.getInvoiceTaxAmount());
    invoice.setInvoiceGrossAmount(rawInvoice.getInvoiceGrossAmount());
    invoice.setCompany(participantRepository.findByName(rawInvoice.getCompanyName()));
    
    for(RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
      FeeItem feeItem = parseRawFeeItemToFeeItem(rawFeeItem);
      invoice.addFeeItem(subscriptionInfo.findByNumber(rawFeeItem.getSubscription()),feeItem);
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

//  private Participant parseToParticipant(Element root) {
//    return new Participant(
//        getFirstTagValue(root, "Name"),
//        getFirstTagValue(root, "City"));
//        
//  }

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

//  public List<FeeItem> findAllFeeItemByInvoiceId(long id) {
//    List<FeeItem> result = new LinkedList<>();
//    Invoice invoice = invoiceRepository.findById(id).orElse(null);
//    if (invoice != null) {
//      for (InvoiceByUserAndPhoneNumber part : invoice.getInvoicePart()) {
//        result.addAll(part.getFees());
//      }
//    }
//    return result;
//  }

  public void save(Invoice invoice) {
    invoiceRepository.save(invoice);
  }

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
//    Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
//
//    if (invoice != null) {
//      try {
//        Invoice reprocess = processInvoiceXmlString(invoice.getXmlString());
//        for (FeeItem item : reprocess.getFeeItems()) {
//          System.out.println(item);
//        }
//        invoiceRepository.delete(invoice);
//        invoiceRepository.save(reprocess);
//      } catch (FileUploadException e) {
//        System.err.println(e);
//      }
//    }
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

  public List<InvoiceByUserAndPhoneNumber> getInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberService.getInvoicesOfUser(user);
  }

  public List<RawInvoice> findAllRawInvoice() {
    return rawInvoiceRepository.findAll();
  }
}
