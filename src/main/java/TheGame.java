import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.nio.file.*;

import javax.mail.*;
import javax.mail.internet.*;

public class TheGame
{
    private static String pass = "";

    // reads in the list of losers from a file, returns the LoserList object
    public static LoserList readList()
    {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File f = new File(s + "/loser_list.txt");
        LoserList readList = new LoserList();
        if (f.exists())
        {
            try
            {
                Scanner input = new Scanner(f);
                String s2;
                while (input.hasNext())
                {
                    String name = "";
                    String email = "";
                    LocalDate lastLost = LocalDate.now();
                    int frequency = -1;
                    s2 = input.next();

                    // ignoring irrelevant text, looking for beginning of next loser
                    while (input.hasNext() && !s2.equals("{"))
                        s2 = input.next();

                    // get name of loser
                    if (input.hasNext())
                    {
                        s2 = input.next();
                        name = s2;
                    }

                    // get email address of loser
                    while (!s2.equals("|") && input.hasNext())
                        s2 = input.next();
                    if (input.hasNext())
                    {
                        s2 = input.next();
                        email = s2;
                    }

                    // get last time loser was reminded to lose
                    while (!s2.equals("|") && input.hasNext())
                        s2 = input.next();
                    if (input.hasNext())
                    {
                        s2 = input.next();
                        lastLost = LocalDate.parse(s2);
                    }

                    // get reminder frequency
                    while (!s2.equals("|") && input.hasNext())
                        s2 = input.next();
                    if (input.hasNext())
                    {
                        s2 = input.next();
                        frequency = Integer.parseInt(s2);
                    }

                    // add the loser we read in to the list
                    if (frequency > -1)
                    {
                        readList.addLoser(new Loser(name, email, lastLost, frequency));
                    }
                }
                input.close();
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Could not find loser_list.txt file");
            }
        }
        return readList;
    }


    // writes list of losers to a file
    public static void writeList(LoserList losers)
    {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File f = new File(s + "/loser_list.txt");
        if (f.exists())
            f.delete();
        try
        {
            f.createNewFile();

            if (losers.howMany() > 0)
            {
                FileWriter fw = new FileWriter(f);
                BufferedWriter bw = new BufferedWriter(fw);

                // append string representing each loser to file
                for (int i = 0; i < losers.howMany(); i++)
                {
                    bw.append(losers.getLoser(i).toString() + "\n");
                }
                bw.close();
                fw.close();
            }
        }
        catch (IOException e)
        {
            System.out.println("Could not write list to file");
        }
    }


    // sends an email reminder to the input address, followed tutorial at
    // http://crunchify.com/java-mailapi-example-send-an-email-via-gmail-smtp/
    public static void sendEmail(String name, String email) throws MessagingException
    {
        Properties props = System.getProperties();
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully.");

        Session session = Session.getDefaultInstance(props, null);

        // writing email message
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        mimeMessage.setSubject("You Lost!");
        mimeMessage.setContent("Hello " + name + ",<br><br> You lost The " +
                                       "Game!<br><br>This is an automated " +
                                       "email sent from a good friend " +
                                       "reminding you about The Game.<br><br>" +
                                       "Best,<br>Mikey",
                               "text/html");

        System.out.println("getting session...");
        Transport transport = session.getTransport("smtp");

        System.out.println("connecting...");
        String userEmail = getEmailAddress();
        // get password from user
        if (pass.isEmpty())
        {
            Scanner passScanner = new Scanner(System.in);
            System.out.println("What is your email password (this will not be saved)?");
            pass = passScanner.next();
        }
        transport.connect("smtp.gmail.com", userEmail, pass);

        System.out.println("Sending e-mail...");
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
        System.out.println("e-mail sent.");
    }


    // makes sure your email account is saved in a text file for future use
    private static void checkedEmailExists()
    {
        // email stored in file called me.txt
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();
        File emailFile = new File(path + "/me.txt");

        // if email address is not on file, prompt user for address
        if (!emailFile.exists())
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("We will send reminder emails to others to lose the game; what is your email address?");
            String emailAddress = scan.nextLine();

            // write address received from user to file
            try
            {
                emailFile.createNewFile();
                FileWriter fw = new FileWriter(emailFile);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.append(emailAddress);
                bw.close();
                fw.close();
            }
            catch (IOException e)
            {
                System.out.println("Could not write email address to file");
            }
        }
    }


    // changes email address of user
    private static void changeEmail(String email)
    {
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();
        File emailFile = new File(path + "/me.txt");
        // delete old email address file
        if (emailFile.exists())
            emailFile.delete();
        // and write new email address to file
        try
        {
            emailFile.createNewFile();
            FileWriter fw = new FileWriter(emailFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(email);
            bw.close();
            fw.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not write email address to file");
        }
    }


    // reads email address of user from file
    private static String getEmailAddress()
    {
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();
        File emailFile = new File(path + "/me.txt");
        String emailAddress = "";
        if (emailFile.exists())
        {
            // read email address from first line of file
            try
            {
                Scanner scanner = new Scanner(emailFile);
                emailAddress = scanner.nextLine();
                scanner.close();
            }
            catch (IOException e)
            {
                System.out.println("Could not read email address from file");
            }
        }
        return emailAddress;
    }


    // prints list of losers
    private static void printLosers(LoserList list)
    {
        for (int i = 0; i < list.howMany(); i++)
        {
            System.out.println(list.getLoser(i));
        }
    }


    public static void main(String[] args)
    {
        checkedEmailExists(); // make sure there is an email address on file
        LoserList readList = TheGame.readList(); // get list of losers from file
        printLosers(readList); // print list of losers

        // ask user if they want to make any changes before sending reminders
        boolean ctu = true;
        Scanner scan = new Scanner(System.in);
        while (ctu)
        {
            System.out.println("Before sending reminders, would you like to " +
                                       "add, remove, or modify a loser, " +
                                       "change your email address, view your " +
                                       "list of losers, or do nothing? " +
                                       "(a/r/m/e/v/n)?");
            String scanS = scan.next();

            // add a Loser
            if (scanS.equals("a") || scanS.equals("add"))
            {
                System.out.println("What is their name?");
                String name = scan.next();
                System.out.println("What is their email address?");
                String email = scan.next();
                System.out.println("How many days ago did you last remind them?");
                String lastLost = scan.next();
                System.out.println("How often do you want to remind them to lose? Every ___ days");
                String time = scan.next();
                readList.addLoser(new Loser(name, email, LocalDate.now().minusDays(Integer.parseInt(lastLost)), Integer.parseInt(time)));
                TheGame.writeList(readList);
            }
            // remove a Loser
            else if (scanS.equals("r") || scanS.equals("remove"))
            {
                System.out.println("Who would you like to remove?");
                String toRemove = scan.next();
                readList.removeLoser(toRemove);
                TheGame.writeList(readList);

            }
            // modify a Loser
            else if (scanS.equals("m") || scanS.equals("modify"))
            {
                System.out.println("Which loser would you like to modify?");
                String name = scan.next();
                if (readList.whereIsLoser(name) == -1)
                {
                    System.out.println("That person is not on your list of losers");
                }
                else
                {
                    Loser el = readList.getLoser(readList.whereIsLoser(name));

                    System.out.println("Would you like to change their name (y/n)?");
                    scanS = scan.next();
                    if (scanS.equals("y") || scanS.equals("yes"))
                    {
                        System.out.println("What is their new name?");
                        el.setName(scan.next());
                    }

                    System.out.println("Would you like to change their email address (y/n)?");
                    scanS = scan.next();
                    if (scanS.equals("y") || scanS.equals("yes"))
                    {
                        System.out.println("What is their new email address?");
                        el.setEmail(scan.next());
                    }

                    System.out.println("Did you remind " + name + " (y/n)?");
                    scanS = scan.next();
                    if (scanS.equals("y") || scanS.equals("yes"))
                    {
                        System.out.println("How many days has it been since you last reminded them?");
                        el.setLastLoss(LocalDate.now().minusDays(Integer.parseInt(scan.next())));
                    }

                    System.out.println("Would you like to change how often they lose (y/n)?");
                    scanS = scan.next();
                    if (scanS.equals("y") || scanS.equals("yes"))
                    {
                        System.out.println("They should lose every ___ days.");
                        el.setFrequency(Integer.parseInt(scan.next()));
                    }
                }
            }
            else if (scanS.equals("e") || scanS.equals("email"))
            {
                // change email address on file
                System.out.println("What is your email address?");
                scanS = scan.next();
                changeEmail(scanS);
            }
            else if (scanS.equals("v") || scanS.equals("view"))
            {
                printLosers(readList);
            }
            else if (scanS.equals("n") || scanS.equals("nothing"))
            {
                ctu = false;
            }
            else
            {
                System.out.println(scanS + " is not one of the choices, please try again.");
            }
        }

        // ask user if they want to send email reminder to each person that
        // needs reminding
        if (readList.anyToNotify())
        {
            for (Loser l : readList.whoToNotify())
            {
                System.out.println("Did you remind " + l.getName() + " yet? (y/n)");
                Scanner answer = new Scanner(System.in);
                String ans = answer.nextLine();
                if (ans.equals("y") || ans.equals("yes"))
                {
                    System.out.println("How many days ago did you remind them?");
                    ans = answer.nextLine();
                    l.setLastLoss(LocalDate.now().minusDays(Integer.parseInt(ans)));
                }
                else
                {
                    System.out.println("Would you like to send an email reminder to them now (y/n)?");
                    ans = answer.nextLine();
                    if (ans.equals("y") || ans.equals("yes"))
                    {
                        try
                        {
                            sendEmail(l.getName(), l.getEmail());
                            l.setLastLoss(LocalDate.now());
                        }
                        catch (MessagingException e)
                        {
                            System.out.println("failed to send email");
                        }
                    }
                }
            }
            TheGame.writeList(readList);
        }
        else
        {
            System.out.println("You are all up to date on your reminders!");
        }
        Runtime.getRuntime().halt(0); // end program
    }
}
