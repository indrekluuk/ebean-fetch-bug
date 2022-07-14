package org.example.domain;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.Transaction;
import io.ebean.TxScope;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class EbeanFetchTwiceInsideTransactionBugTest {



  @Test
  public void testFetchSameItemTwiceInTransaction_withBeanCache() {
    Database server = DB.getDefault();

    Customer customer = new Customer();
    server.save(customer);

    Contact contact = new Contact();
    contact.setType("phone");
    contact.setCustomer(customer);
    server.save(contact);


    try (Transaction transaction = DB.beginTransaction(TxScope.required())) {
      {
        Customer requestedCustomer = server.find(Customer.class)
          .setDisableLazyLoading(true)
          //.fetch("contacts")
          .where()
          .eq("id", customer.getId())
          .findOne();
        assertEquals(0, requestedCustomer.getContacts().size());
      }

      // I don't know if this is a bug or expected behaviour.
      // Im loading the same item again but with different fetch,
      // but it just returns the result of the first request.
      {
        Customer requestedCustomer = server.find(Customer.class)
          .setDisableLazyLoading(true)
          .fetch("contacts")
          .where()
          .eq("id", customer.getId())
          .findOne();
        assertEquals(1, requestedCustomer.getContacts().size());
      }
    }
  }





  @Test
  public void testFetchSameItemTwiceInTransaction_withoutBeanCache() {
    Database server = DB.getDefault();

    Customer customer = new Customer();
    server.save(customer);

    Contact contact = new Contact();
    contact.setType("phone");
    contact.setCustomer(customer);
    server.save(contact);


    try (Transaction transaction = DB.beginTransaction(TxScope.required())) {
      {
        Customer requestedCustomer = server.find(Customer.class)
          .setLoadBeanCache(false)
          .setDisableLazyLoading(true)
          .fetch("contacts")
          .where()
          .eq("id", customer.getId())
          .findOne();
        assertEquals(1, requestedCustomer.getContacts().size());
      }

      // Ths definitely seems like a bug since it should not double the list like that.
      {
        Customer requestedCustomer = server.find(Customer.class)
          .setLoadBeanCache(false)
          .setDisableLazyLoading(true)
          .fetch("contacts")
          .where()
          .eq("id", customer.getId())
          .findOne();
        assertEquals(1, requestedCustomer.getContacts().size());
      }
    }
  }






}
