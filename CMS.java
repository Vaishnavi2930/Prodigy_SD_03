
import java.io.*;
import java.util.Scanner;

class Contact {
    private String fName, lName, address, email;
    private long phNo;

    public void createContact() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter first name: ");
        fName = scanner.next();
        System.out.print("Enter last name: ");
        lName = scanner.next();
        System.out.print("Enter phone number: ");
        phNo = scanner.nextLong();
        System.out.print("Enter address: ");
        address = scanner.next();
        System.out.print("Enter email: ");
        email = scanner.next();
    }

    public void showContact() {
        System.out.println("Name: " + fName + " " + lName);
        System.out.println("Phone: " + phNo);
        System.out.println("Address: " + address);
        System.out.println("Email: " + email);
    }

    public void writeOnFile() {
        char ch;
        try (DataOutputStream f1 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("CMS.dat", true)))) {
            do {
                createContact();
                f1.writeUTF(fName);
                f1.writeUTF(lName);
                f1.writeLong(phNo);
                f1.writeUTF(address);
                f1.writeUTF(email);
                System.out.print("Do you want to add another contact?(y/n)");
                ch = new Scanner(System.in).next().charAt(0);
            } while (ch == 'y');

            System.out.println("Contact Created...");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file: " + e.getMessage());
        }
    }

    public void readFromFile() {
        try (DataInputStream f2 = new DataInputStream(new BufferedInputStream(new FileInputStream("CMS.dat")))) {
            System.out.println("\nLIST OF CONTACTS\n");
            
            while (true) {
                try {
                    fName = f2.readUTF();
                    lName = f2.readUTF();
                    phNo = f2.readLong();
                    address = f2.readUTF();
                    email = f2.readUTF();
                    showContact();
                    System.out.println("\n");
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading from file: " + e.getMessage());
        }
    }

    public void searchOnFile() {
        try (DataInputStream f3 = new DataInputStream(new BufferedInputStream(new FileInputStream("CMS.dat")))) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter phone no.: ");
            long phone = scanner.nextLong();
            while (true) {
                try {
                    fName = f3.readUTF();
                    lName = f3.readUTF();
                    phNo = f3.readLong();
                    address = f3.readUTF();
                    email = f3.readUTF();
                    if (phNo == phone) {
                        showContact();
                        return;
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("\nNo record found");
        } catch (IOException e) {
            System.out.println("An error occurred while searching in file: " + e.getMessage());
        }
    }

    public void deleteFromFile() {
        try (DataInputStream f5 = new DataInputStream(new BufferedInputStream(new FileInputStream("CMS.dat")));
             DataOutputStream f4 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("temp.dat")))) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter phone no. to delete: ");
            long num = scanner.nextLong();
            while (true) {
                try {
                    fName = f5.readUTF();
                    lName = f5.readUTF();
                    phNo = f5.readLong();
                    address = f5.readUTF();
                    email = f5.readUTF();
                    if (phNo != num) {
                        f4.writeUTF(fName);
                        f4.writeUTF(lName);
                        f4.writeLong(phNo);
                        f4.writeUTF(address);
                        f4.writeUTF(email);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Contact Deleted...");
        } catch (IOException e) {
            System.out.println("An error occurred while deleting from file: " + e.getMessage());
        }
        File file = new File("CMS.dat");
        File tempFile = new File("temp.dat");
        if (!file.delete()) {
            System.out.println("Failed to delete the original file.");
        } else if (!tempFile.renameTo(file)) {
            System.out.println("Failed to rename the temp file.");
        }
    }

    public void editContact() {
        try (RandomAccessFile f6 = new RandomAccessFile("CMS.dat", "rw")) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Edit contact\n");
            System.out.print("Enter the phone number to be edited: ");
            long num = scanner.nextLong();
            while (true) {
                try {
                    fName = f6.readUTF();
                    lName = f6.readUTF();
                    phNo = f6.readLong();
                    address = f6.readUTF();
                    email = f6.readUTF();
                    if (phNo == num) {
                        createContact();
                        long pos = f6.getFilePointer() - (4 * 2 + 8); // subtracting size of 2 UTF strings and long
                        f6.seek(pos);
                        f6.writeUTF(fName);
                        f6.writeUTF(lName);
                        f6.writeLong(phNo);
                        f6.writeUTF(address);
                        f6.writeUTF(email);
                        System.out.println("Contact Updated...");
                        return;
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("No record found");
        } catch (IOException e) {
            System.out.println("An error occurred while editing contact: " + e.getMessage());
        }
    }
}

public class CMS{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\t\t\t * WELCOME TO CONTACT MANAGEMENT SYSTEM *\n Press Enter");
        scanner.nextLine();

        while (true) {
            Contact c1 = new Contact();
            int choice;

            System.out.println("\nCONTACT MANAGEMENT SYSTEM");
            System.out.println("\nMAIN MENU");
            System.out.println("[1] Add a new Contact");
            System.out.println("[2] List all Contacts");
            System.out.println("[3] Search for contact");
            System.out.println("[4] Delete a Contact");
            System.out.println("[5] Edit a Contact");
            System.out.println("[0] Exit");
            
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    c1.writeOnFile();
                    break;
                case 2:
                    c1.readFromFile();
                    break;
                case 3:
                    c1.searchOnFile();
                    break;
                case 4:
                    c1.deleteFromFile();
                    break;
                case 5:
                	c1.editContact();
                    break;
                case 0:
                    System.out.println("Program ended");
                    System.exit(0);
                    break;
                default:
                    continue;
            }

            int opt;
            System.out.println("\n\n..::Enter the Choice:\n[1] Main Menu\t\t[0] Exit\n");
            opt = scanner.nextInt();

            switch (opt) {
                case 0:
                    System.out.println("\n\n\n\t\t\tThank you for using CMS.\n\n");
                    System.exit(0);
                    break;
                default:
                    continue;
            }
        }
    }
}