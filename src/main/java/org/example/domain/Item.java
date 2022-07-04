package org.example.domain;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Item extends Model {

  @Id
  private Long itemId;

  @ManyToOne
  @JoinColumn(name = "F_ITEM_GROUP_ID")
  private Item itemGroup;

  @OneToMany(mappedBy = "itemGroup")
  private List<Item> subItems;

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public Item getItemGroup() {
    return itemGroup;
  }

  public void setItemGroup(Item itemGroup) {
    this.itemGroup = itemGroup;
  }

  public List<Item> getSubItems() {
    return subItems;
  }

  public void setSubItems(List<Item> subItems) {
    this.subItems = subItems;
  }
}
