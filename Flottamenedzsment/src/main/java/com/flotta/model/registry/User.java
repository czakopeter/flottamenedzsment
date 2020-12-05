 package com.flotta.model.registry;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.enums.UserStatus;
import com.flotta.model.BasicEntity;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.switchTable.UserDev;
import com.flotta.model.switchTable.UserSub;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "users")
public class User extends BasicEntity {

	@Column(unique = true)
	private String email;
	
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
	
	private UserStatus status;
	
	@ManyToOne
	private ChargeRatioByCategory chargeRatio;
	
	public static final Comparator<User> BY_NAME = 
	    Comparator.comparing(User::getFullName, String.CASE_INSENSITIVE_ORDER)
	    .thenComparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);

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
	
	public Set<UserDev> getUserDevs() {
    return userDevs;
  }

  public void setUserDevs(Set<UserDev> userDevs) {
    this.userDevs = userDevs;
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
	
	public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public ChargeRatioByCategory getChargeRatio() {
    return chargeRatio;
  }

  public void setChargeRatio(ChargeRatioByCategory chargeRatio) {
    this.chargeRatio = chargeRatio;
  }

	public void addRole(Role role) {
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
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    return true;
  }
  
}