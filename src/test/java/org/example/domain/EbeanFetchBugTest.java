package org.example.domain;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.FetchConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EbeanFetchBugTest {



  @Test
  public void testSubItemListFetch_ofQuery() {
    Database server = DB.getDefault();

    Item itemA = new Item();
    server.save(itemA);
    Item itemB = new Item();
    server.save(itemB);

    for (int i=0; i<2; i++) {
      Item subItem = new Item();
      subItem.setItemGroup(itemA);
      server.save(subItem);
    }

    for (int i=0; i<3; i++) {
      Item subItem = new Item();
      subItem.setItemGroup(itemB);
      server.save(subItem);
    }

    Customer customer = new Customer();
    customer.setItemA(itemA);
    customer.setItemB(itemB);
    server.save(customer);

    // This is OK
    {
      Customer requestedCustomer = server.find(Customer.class)
        .setDisableLazyLoading(true)
        .fetch("itemA.subItems", FetchConfig.ofQuery())
        .fetch("itemB.subItems", FetchConfig.ofQuery())
        .where()
        .eq("id", customer.getId())
        .findOne();
      assertEquals(2, requestedCustomer.getItemA().getSubItems().size());
      assertEquals(3, requestedCustomer.getItemB().getSubItems().size());
    }
  }



  @Test
  public void testSubItemListFetch_itemAFirst() {
    Database server = DB.getDefault();


    Item itemA = new Item();
    server.save(itemA);
    Item itemB = new Item();
    server.save(itemB);

    for (int i=0; i<2; i++) {
      Item subItem = new Item();
      subItem.setItemGroup(itemA);
      server.save(subItem);
    }

    for (int i=0; i<3; i++) {
      Item subItem = new Item();
      subItem.setItemGroup(itemB);
      server.save(subItem);
    }

    Customer customer = new Customer();
    customer.setItemA(itemA);
    customer.setItemB(itemB);
    server.save(customer);


    // This fails because requestedCustomer.getItemB().getSubItems() is not loaded
    {
      Customer requestedCustomer = server.find(Customer.class)
        .setDisableLazyLoading(true)
        .fetch("itemA.subItems")
        .fetch("itemB.subItems")
        .where()
        .eq("id", customer.getId())
        .findOne();
      assertEquals(2, requestedCustomer.getItemA().getSubItems().size());
      assertEquals(3, requestedCustomer.getItemB().getSubItems().size());
    }
  }



  @Test
  public void testSubItemListFetch_itemBFirst() {
    Database server = DB.getDefault();


    Item itemA = new Item();
    server.save(itemA);
    Item itemB = new Item();
    server.save(itemB);

    for (int i=0; i<2; i++) {
      Item subItem = new Item();
      subItem.setItemGroup(itemA);
      server.save(subItem);
    }

    for (int i=0; i<3; i++) {
      Item subItem = new Item();
      subItem.setItemGroup(itemB);
      server.save(subItem);
    }

    Customer customer = new Customer();
    customer.setItemA(itemA);
    customer.setItemB(itemB);
    server.save(customer);

    // This fails because requestedCustomer.getItemA().getSubItems() is not loaded
    {
      Customer requestedCustomer = server.find(Customer.class)
        .setDisableLazyLoading(true)
        .fetch("itemB.subItems")
        .fetch("itemA.subItems")
        .where()
        .eq("id", customer.getId())
        .findOne();
      assertEquals(2, requestedCustomer.getItemA().getSubItems().size());
      assertEquals(3, requestedCustomer.getItemB().getSubItems().size());
    }
  }



}
