package assignment10.tester;

import assignment10.Contact;
import assignment10.ContactNotFoundException;
import assignment10.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tester {
    public static void main(String[] args) {
        Service service  = new Service();
        List<Contact>contact_list = new ArrayList<Contact>();
        try {
            service.readContactFromFile(contact_list, "Contacts.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1234567890);

        Contact contact1 = new Contact(1,"John Doe","jd@gmail.com", numbers);
        Contact contact2 = new Contact(2,"Mike Walker","mw@gmail.com",null);
        service.addContact(contact1, contact_list);
        service.addContact(contact2, contact_list);

        try {
            service.removeContact(contact2, contact_list);
        } catch (ContactNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Contact contact = service.searchContactByName("Mike", contact_list);
        } catch (ContactNotFoundException e) {
            e.printStackTrace();
        }

        List<Contact> list;
        try {
            list = service.SearchContactByNumber("1234567890", contact_list);
            for(Contact contact: list)
                System.out.println(contact.getContactName());
        } catch (ContactNotFoundException e) {
            e.printStackTrace();
        }
        service.sortContactByName(contact_list);
        for(Contact contact: contact_list)
            System.out.println(contact.getContactName());
    }
}
