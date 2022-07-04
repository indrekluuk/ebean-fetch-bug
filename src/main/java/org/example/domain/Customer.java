package org.example.domain;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class Customer extends Model {

  @Id
  Long id;

  String name;

  String notes;

  @ManyToOne
  @JoinColumn(name = "F_ITEM_A_ID")
  private Item itemA;

  @ManyToOne
  @JoinColumn(name = "F_ITEM_B_ID")
  private Item itemB;


  @Version
  Long version;

  public Customer(String name) {
    this.name = name;
  }

  public Customer() {
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

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Item getItemA() {
    return itemA;
  }

  public void setItemA(Item itemA) {
    this.itemA = itemA;
  }

  public Item getItemB() {
    return itemB;
  }

  public void setItemB(Item itemB) {
    this.itemB = itemB;
  }
}
