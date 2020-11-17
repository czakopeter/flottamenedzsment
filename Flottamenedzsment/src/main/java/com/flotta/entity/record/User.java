 package com.flotta.entity.record;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.entity.invoice.ChargeRatioByCategory;
import com.flotta.entity.switchTable.UserDev;
import com.flotta.entity.switchTable.UserSub;
import com.flotta.enums.UserStatusEnum;
import com.flotta.utility.Utility;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "users")
public class User extends BasicEntity {

	@Column( unique=true, nullable=false )
	private String email;
	
	@Column( nullable=false )
	private String password;
	
	private String fullName;
	
	private boolean enabled;
	
	private String activationKey;
	
  @OneToMany( mappedBy = "user" )
	private Set<UserSub> userSubs;
	
	@OneToMany( mappedBy = "user" )
  private Set<UserDev> userDevs;
	
	@ManyToMany( cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable( 
		name = "users_roles",
		joinColumns = {@JoinColumn(name="user_id")}, 
		inverseJoinColumns = {@JoinColumn(name="role_id")}  
	)
	private Set<Role> roles = new HashSet<Role>();
	
	private UserStatusEnum status;

	@ManyToMany
	private List<ChargeRatioByCategory> payDevs = new LinkedList<>();

  public User() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public Set<UserSub> getUserSubs() {
		return userSubs;
	}

	public void setUserSubs(Set<UserSub> userSubs) {
		this.userSubs = userSubs;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void addRoles(Role role) {
		if (this.roles == null) 
			this.roles = new HashSet<>();
		this.roles.add(role);
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", fullName=" + fullName
				+ ", enabled=" + enabled + ", subscriptions=" + Objects.toString(userSubs, "NULL") + ", roles=" + Objects.toString(roles, "NULL") + "]";
	}
	
	public boolean equalsByEmail(User other) {
		if(other == null) {
			return false;
		}
		return email.equals(other.getEmail());
	}
	
	public List<ChargeRatioByCategory> getPayDevs() {
    return payDevs;
  }

  public void setPayDevs(List<ChargeRatioByCategory> payDevs) {
    this.payDevs = payDevs;
  }
  
  public void addPayDevision(ChargeRatioByCategory payDevision) {
    this.payDevs.add(payDevision);
  }

  public boolean hasRole(String role) {
    for(Role r : roles) {
      if(r.getRole().equalsIgnoreCase(role)) {
        return true;
      }
    }
    return false;
  }
  
  public String getStatusName() {
    return status.toString();
  }
  
  public UserStatusEnum getStatus() {
    return status;
  }

  public void setStatus(UserStatusEnum status) {
    this.status = status;
  }
}