package com.flotta.model.invoice;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.model.BasicEntity;

@Entity
@Table(name = "nodes")
public class MyNode extends BasicEntity {

  private long templateId;
  
  private String name;
  
  @ManyToOne
  private MyNode parent;
  
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<MyNode> child = new LinkedList<MyNode>();

  public MyNode(MyNode parent, String name) {
    this.templateId = parent.getTemplateId();
    this.parent = parent;
    this.name = name;
  }

  public MyNode() {}


  public long getTemplateId() {
    return templateId;
  }

  public void setTemplateId(long templateId) {
    this.templateId = templateId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public MyNode getParent() {
    return parent;
  }

  public void setParent(MyNode parent) {
    this.parent = parent;
  }

  public List<MyNode> getChild() {
    return child;
  }

  public void setChild(List<MyNode> child) {
    this.child = child;
  }
  
  public void show() {
    if(child.isEmpty()) {
      System.out.println("name = " + name);
    } else {
      System.out.println("name = " + name + "(OPEN)");
      for(MyNode n : child) {
        n.show();
      }
      System.out.println("name = " + name + "(CLOSE)");
    }
  }
  
  public MyNode appendChild(String name) {
    for(MyNode c : this.child) {
      if(c.getName().equalsIgnoreCase(name)) {
        return c;
      }
    }
    MyNode n = new MyNode(this, name);
    child.add(n);
    return n;
  }

  public static MyNode createRoot(int templateId, String name) {
    MyNode root = new MyNode();
    root.setTemplateId(templateId);
    root.setParent(null);
    root.setName(name);
    return root;
  }
  
  public boolean equalsName(String name) {
    return this.name.equalsIgnoreCase(name);
  }
}
