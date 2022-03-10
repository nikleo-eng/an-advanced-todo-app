package it.unifi.dinfo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "log", 
	uniqueConstraints = { @UniqueConstraint(columnNames = { "in", "user_id" }) })
public class Log implements Serializable {
	
	private static final long serialVersionUID = 6867311688404352939L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"in\"", nullable = false)
	private Date in;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"out\"", nullable = true)
	private Date out;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
	private User user;
	
	public Log() {
		super();
	}

	public Log(Date in, User user) {
		super();
		this.in = in;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getIn() {
		return in;
	}

	public void setIn(Date in) {
		this.in = in;
	}

	public Date getOut() {
		return out;
	}

	public void setOut(Date out) {
		this.out = out;
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
		result = prime * result + ((in == null) ? 0 : in.hashCode());
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
		Log other = (Log) obj;
		if (in == null) {
			if (other.in != null)
				return false;
		} else if (!in.equals(other.in))
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
		return "Log [id=" + id + ", in=" + in.toString() + ", user=" + user.toString() + "]";
	}
	
}
