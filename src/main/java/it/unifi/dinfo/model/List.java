package it.unifi.dinfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "list", 
	uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "user_id" }) })
public class List implements Serializable {
	
	private static final long serialVersionUID = -4475012228137651744L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
	private User user;
	
	public List() {
		super();
	}
	
	public List(String name, User user) {
		this();
		this.name = name;
		this.user = user;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final var prime = 31;
		var result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		List other = (List) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equalsIgnoreCase(other.name))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "List [id=" + id + ", name=" + name + ", user=" + user.toString() + "]";
	}
	
}
