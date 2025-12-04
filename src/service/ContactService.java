package service;

import dao.ContactDAO;
import model.Contact;
import java.util.List;

import javax.management.Query;

public class ContactService {
    private final ContactDAO contactDAO;

    public ContactService() {
        this.contactDAO = new ContactDAO();
    }

    public List<Contact> getAllContacts() {
        return contactDAO.getAllContacts();
    }

    public List<Contact> getAllSorted(String sortField, boolean ascending) {
        return contactDAO.getAllSorted(sortField, ascending);
    }

    public List<Contact> searchByFirstName(String query) {
        return contactDAO.searchByFirstName(query);
    }
    
    public List<Contact> searchByLastName(String query) {
        return contactDAO.searchByLastName(query);
    }
    public List<Contact> searchByPhoneContains(String digits) {
        return contactDAO.searchByPhoneContains(digits);
    }

    public void printContactsList(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\n CONTACTS LIST");
        System.out.printf("\n %-5s %-15s %-15s %-15s %-25s %-30s", "ID", "FIRST NAME", "LAST NAME", "PHONE", "EMAIL", "LINKEDIN URL");
        System.out.println("\n-----------------------------------------------------------------------------------------------");
        
        for (Contact contact : contacts) {
            int id = contact.getContactId();
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "-";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "-";
            String phone = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "-";
            String email = contact.getEmail() != null ? contact.getEmail() : "-";
            String url = contact.getLinkedinUrl() != null ? contact.getLinkedinUrl() : "-";

            System.out.printf(" %-5d %-15s %-15s %-15s %-25s %-30s", id, firstName, lastName, phone, email, url);

            System.out.println("\n Total " + contacts.size() + " contact(s) found.");
        }
    }
}
