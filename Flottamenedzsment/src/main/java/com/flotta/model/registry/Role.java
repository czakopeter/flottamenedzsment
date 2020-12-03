package com.flotta.model.registry;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.flotta.model.BasicEntity;

@Entity
@Table(name = "roles")
public class Role extends BasicEntity {

  @Column(unique = true, nullable = false)
	private String role;
	
	@ManyToMany( mappedBy = "roles")
	private Set<User> users = new HashSet<User>();
	
	public Role() {}
	
	public Role(String role){
		this.role=role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + "]";
	}

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Role other = (Role) obj;
    if (role == null) {
      if (other.role != null)
        return false;
    } else if (!role.equalsIgnoreCase(other.role))
      return false;
    return true;
  }
	
}
