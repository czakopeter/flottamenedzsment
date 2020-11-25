package com.flotta.service.invoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.flotta.model.invoice.MyNode;
import com.flotta.repository.invoice.InvoiceTemplateRepository;

@Service
public class InvoiceTemplateService {

  //TODO function to add new template or modify
  
  private InvoiceTemplateRepository invoiceTemplateRepository;

  @Autowired
  public void setBillTemplateRepository(InvoiceTemplateRepository invoiceTemplateRepository) {
    this.invoiceTemplateRepository = invoiceTemplateRepository;
    createBasicTemplate();
  }

  private void createBasicTemplate() {

    MyNode root = MyNode.createRoot(1, "Account");

    MyNode feeItems = root.appendChild("FeeItems");
    MyNode feeItem = feeItems.appendChild("FeeItem");

    feeItem.appendChild("ItemNr");
    feeItem.appendChild("Desc");
    feeItem.appendChild("Begin");
    feeItem.appendChild("End");
    feeItem.appendChild("NetA");
    feeItem.appendChild("TaxP");
    feeItem.appendChild("TaxA");
    feeItem.appendChild("GrossA");

    MyNode customerData = root.appendChild("CustomerData");
    customerData.appendChild("Name");
    customerData.appendChild("City");

    MyNode companyData = root.appendChild("CompanyData");
    companyData.appendChild("Name");
    companyData.appendChild("City");

    MyNode invoiceData = root.appendChild("InvoiceData");
    invoiceData.appendChild("Begin");
    invoiceData.appendChild("End");
    invoiceData.appendChild("InvNb");
    invoiceData.appendChild("InvTotalNetA");
    invoiceData.appendChild("InvTotalTaxA");
    invoiceData.appendChild("InvTotalGrossA");

    invoiceTemplateRepository.save(root);
  }
  
  public List<MyNode> findAllRoot() {
    return invoiceTemplateRepository.findAllByParentIsNull();
  }

  public List<MyNode> findAllRootByName(String name) {
    return invoiceTemplateRepository.findAllByParentIsNullAndName(name);
  }

  public boolean invoiceTreeFormalCheck(Element root) {
    List<MyNode> templates = findAllRootByName(root.getNodeName());
    if (templates.isEmpty()) {
      return false;
    }
    
    for(MyNode template : templates) {
      if (equals(root.getChildNodes(), template.getChild())) {
        return true;
      }
    }

    return false;
  }
  
  //kijavítani, nem működik jól
  private boolean equals(NodeList nodes, List<MyNode> templates) {
    System.out.println("\ntemplate: " + templates.size());
    print(templates);
    System.out.println("invoice: " + length(nodes));
    print(nodes);
    
    if(templates.size() > length(nodes)) {
      return false;
    }
    
    Map<String, Boolean> appear = new HashMap<>();
    for(MyNode node : templates) {
      appear.put(node.getName(), false);
    }
    
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        int index = indexOf(templates, node.getNodeName());
        if(index == -1 || !equals(node.getChildNodes(), templates.get(index).getChild())) {
          return false;
        }
        appear.put(templates.get(index).getName(), true);
      }
    }
    for(boolean appeared : appear.values()) {
      if(appeared == false) {
        return false;
      }
    }
    return true;
  }
  
  private int indexOf(List<MyNode> nodes, String searched) {
    for(int i = 0; i < nodes.size(); i++) {
      MyNode node = nodes.get(i);
      if(node.equalsName(searched)) {
        return i;
      }
    }
    return -1;
  }
  
  private void print(NodeList list) {
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        System.out.print(node.getNodeName() + ", ");
      }
    }
    System.out.println();
  }
  
  private void print(List<MyNode> list) {
    for (int i = 0; i < list.size(); i++) {
      MyNode node = list.get(i);
      System.out.print(node.getName() + ", ");
    }
    System.out.println();
  }
  
  private int length(NodeList nodes) {
    int count = 0;
    for (int i = 0; i < nodes.getLength(); i++) {
      Node tempNode = nodes.item(i);
      if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
        count++;
      }
    }
    return count;
  }
}
