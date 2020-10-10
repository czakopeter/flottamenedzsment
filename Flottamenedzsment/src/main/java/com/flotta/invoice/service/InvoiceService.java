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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.flotta.entity.User;
import com.flotta.invoice.CompanyData;
import com.flotta.invoice.DescriptionCategoryCoupler;
//import com.flotta.entity.viewEntity.OneCategoryOfUserFinance;
import com.flotta.invoice.FeeItem;
import com.flotta.invoice.Invoice;
import com.flotta.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.invoice.exception.FileUploadException;
import com.flotta.invoice.repository.InvoiceRepository;
import com.flotta.service.SubscriptionServiceOnlyInfo;

@Service
public class InvoiceService {

  private InvoiceRepository invoiceRepository;

  private BillTemplateService billTemplateService;

  private FeeItemService feeItemService;
  
  private SubscriptionServiceOnlyInfo subscriptionInfo;

  private InvoiceByUserAndPhoneNumberService invoiceByUserAndPhoneNumberService;
  
  private DescriptionCategoryCouplerServiceOnlyGet descriptionCategoryCouplerServiceOnlyGet;
  
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

  public Invoice uploadInvoice(MultipartFile file) throws FileUploadException {
    String xmlString = getXMLString(file);

    Invoice invoice = processInvoiceXmlString(xmlString);
    
    if(!invoice.isConsistent()) {
      throw new FileUploadException("Invoice is NOT consistant!");
    }
    
    if (invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber()) != null) {
      throw new FileUploadException("Already exists!");
    }
    
    return invoiceRepository.save(invoice);
  }

  private Invoice processInvoiceXmlString(String xml) throws FileUploadException {
    Element root = getTreeFromXMLString(xml);

    if (billTemplateService.invoiceTreeFormalCheck(root)) {
      Invoice invoice = parseToInvoice(root);
      invoice.setCategoryOfFees(descriptionCategoryCouplerServiceOnlyGet.findById(1));
      invoice.setAmountRatioOfFees();
      invoice.setXmlString(xml);
      return invoice;

    } else {
      throw new FileUploadException("Invalid structure");
    }
  }

  private Invoice parseToInvoice(Element root) throws FileUploadException {
    try {
      Invoice invoice = new Invoice();
      invoice.setBeginDate(LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
      invoice.setEndDate(LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
      invoice.setInvoiceNumber(getFirstTagValue(root, "InvNb"));
      invoice.setInvoiceNetAmount(Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')));
      invoice.setInvoiceTaxAmount(Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')));
      invoice.setInvoiceGrossAmount(Double.valueOf(getFirstTagValue(root, "InvTotalGrossA").replace(',', '.')));
      
      invoice.setCompanyData(parseToCompanyData((Element)root.getElementsByTagName("CompanyData").item(0)));
      
      NodeList nodes = root.getElementsByTagName("FeeItem");
      for (int i = 0; i < nodes.getLength(); i++) {
        Element feeItem = (Element) nodes.item(i);
        invoice.addFeeItem(
            subscriptionInfo.findByNumber(getFirstTagValue(feeItem, "ItemNr")),
            parseToFeeItem(feeItem));
      }
      return invoice;
    } catch (DateTimeParseException | NumberFormatException e) {
      throw new FileUploadException(e.toString());
    }
  }
  
  private FeeItem parseToFeeItem(Element feeItemElement) {
    return new FeeItem(
        getFirstTagValue(feeItemElement, "ItemNr"), 
        getFirstTagValue(feeItemElement, "Desc"), 
        LocalDate.parse(getFirstTagValue(feeItemElement, "Begin"),DateTimeFormatter.ofPattern("uuuu.MM.dd.")), 
        LocalDate.parse(getFirstTagValue(feeItemElement, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
        Double.valueOf(getFirstTagValue(feeItemElement, "NetA").replace(',', '.')),
        Double.valueOf(getFirstTagValue(feeItemElement, "TaxA").replace(',', '.')), 
        Double.valueOf(getFirstTagValue(feeItemElement, "TaxP").replace(',', '.').replace("%", "")),
        Double.valueOf(getFirstTagValue(feeItemElement, "GrossA").replace(',', '.')));
  }
  
  private CompanyData parseToCompanyData(Element root) {
    return new CompanyData(
        getFirstTagValue(root, "Name"),
        getFirstTagValue(root, "City"));
        
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
    Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);

    if (invoice != null) {
      try {
        Invoice reprocess = processInvoiceXmlString(invoice.getXmlString());
        for (FeeItem item : reprocess.getFeeItems()) {
          System.out.println(item);
        }
        invoiceRepository.delete(invoice);
        invoiceRepository.save(reprocess);
      } catch (FileUploadException e) {
        System.err.println(e);
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
    if(invoice != null) {
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

}
