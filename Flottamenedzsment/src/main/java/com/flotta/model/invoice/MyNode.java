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
  private List<MyNode> children = new LinkedList<MyNode>();

  public MyNode(MyNode parent, String name) {
    this.templateId = parent.getTemplateId();
    this.parent = parent;
    this.name = name;
  }

  public MyNode() {}
  
  public MyNode(String name) {
    this.name = name;
  }

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

  public List<MyNode> getChildren() {
    return children;
  }

  public void setChildren(List<MyNode> children) {
    this.children = children;
  }
  
  public void show() {
    if(children.isEmpty()) {
      System.out.println("name = " + name);
    } else {
      System.out.println("name = " + name + "(OPEN)");
      for(MyNode n : children) {
        n.show();
      }
      System.out.println("name = " + name + "(CLOSE)");
    }
  }
  
  public MyNode appendChild(String name) {
    for(MyNode c : this.children) {
      if(c.getName().equalsIgnoreCase(name)) {
        return c;
      }
    }
    MyNode n = new MyNode(this, name);
    children.add(n);
    return n;
  }

  public boolean equalsByName(String name) {
    return this.name.equalsIgnoreCase(name);
  }
}
