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
@Table(name = "detail", 
	uniqueConstraints = { @UniqueConstraint(columnNames = { "todo", "list_id" }) })
public class Detail implements Serializable {
	
	private static final long serialVersionUID = 553382017804115529L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;
	
	@Column(name = "todo", nullable = false)
	private String todo;
	
	@Column(name = "done", nullable = false)
	private Boolean done;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "list_id", referencedColumnName = "id")
	private List list;

	public Detail() {
		super();
	}

	public Detail(String todo, List list) {
		this();
		this.todo = todo;
		this.done = Boolean.FALSE;
		this.list = list;
		this.list.getDetails().add(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public Boolean getDone() {
		if (done == null) {
			done = Boolean.FALSE;
		}

		return done;
	}

	public void setDone(Boolean done) {
		if (done == null) {
			done = Boolean.FALSE;
		}

		this.done = done;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	@Override
	public int hashCode() {
		final var prime = 31;
		var result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((todo == null) ? 0 : todo.hashCode());
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
		Detail other = (Detail) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (todo == null) {
			if (other.todo != null)
				return false;
		} else if (!todo.equalsIgnoreCase(other.todo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Detail [id=" + id + ", todo=" + todo + ", done=" + done + ", list=" + list.toString() + "]";
	}

}
