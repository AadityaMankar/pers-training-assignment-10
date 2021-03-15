package assignment10;

import java.io.*;
import java.sql.*;
import java.util.*;

import assignment10.util.JDBCUtility;

class SortContactByName implements Comparator<Contact> {
    @Override
    public int compare(Contact o1, Contact o2) {
        return Integer.compare(o1.getContactName().compareTo(o2.getContactName()), 0);
    }
}

public class Service {
    public void addContact(Contact contact,List<Contact>contacts){
        contacts.add(contact);
    }

    public void removeContact(Contact contact, List<Contact> contacts) throws ContactNotFoundException {
        if(contacts.contains(contact)) {
            contacts.remove(contact);
        }else {
            throw new ContactNotFoundException("This contact is not available");
        }
    }

    public Contact searchContactByName(String name, List<Contact> contact) throws ContactNotFoundException {
        for(Contact c: contact)
            if(c.getContactName().equals(name))
                return c;

        throw new ContactNotFoundException("Contact with this name is not available");
    }

    public List<Contact> SearchContactByNumber(String number, List<Contact> contact) throws ContactNotFoundException{
        Set<Contact> contactSet = new HashSet<Contact>();

        for(Contact c: contact) {
            List<String> cont = c.getContactNumber();
            for(String num:cont)
                if(num.contains(number))
                    contactSet.add(c);
        }

        if(contactSet.size() == 0)
            throw new ContactNotFoundException("This number does not match with any of the contacts");

        return new ArrayList<>(contactSet);
    }

    public void addContactNumber(int contactID,String contactNo, List<Contact> contacts) {
        for(Contact contact: contacts) {
            if(contact.getContactId() == contactID) {
                if(!contact.getContactNumber().contains(contactNo)) {
                    contact.getContactNumber().add(contactNo);
                }
            }
        }
    }

    public void sortContactByName(List<Contact> contacts) {
        contacts.sort(new SortContactByName());
        for(Contact c: contacts) {
            System.out.println(c.getContactName());
        }
    }

    public void readContactFromFile(List<Contact> contacts, String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] arr = line.split(",");
            int id = Integer.parseInt(arr[0]);
            String name = arr[1];
            String email = arr[2];

            String[] temp = arr[3].split(";");
            List<String> list = new ArrayList<String>(Arrays.asList(temp));

            Contact contact = new Contact(id,name,email,list);
            contacts.add(contact);
        }
    }
    public void serializeContactDetails(List<Contact> contacts,String filename) {
        try{
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(contacts);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> deserializeContact(String filename){
        ArrayList<Contact> list = null;

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert objectInputStream != null;
            list = (ArrayList<Contact>) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        try {
            objectInputStream.close();
            assert fileInputStream != null;
            fileInputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;

    }

    Set<Contact> populateContactFromDb() throws SQLException {
        Set<Contact> contactSet =new HashSet<Contact>();

        Connection connection = JDBCUtility.getConnection();

        try {
            Statement statement = connection.createStatement();
            String q = "Select * from contact";
            ResultSet rs = statement.executeQuery(q);

            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                String arr = rs.getString(4);
                List<String> contList = new ArrayList<String>();
                if(arr != null) {
                    String[] temp = arr.split(",");
                    Collections.addAll(contList, temp);
                }
                contactSet.add(new Contact(id,name,email,contList));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtility.closeConnection();
        }
        return contactSet;
    }
    boolean addContacts(List<Contact> existingContact,Set<Contact>newContacts) {
        return existingContact.addAll(newContacts);
    }
}
