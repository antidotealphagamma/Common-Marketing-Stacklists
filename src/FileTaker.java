/**
 * Created by nielskjer on 7/27/16.
 */

import java.util.*;
import java.io.*;


public class FileTaker {

    /** Instance Variables **/
    private static ArrayList<String> companyList = new ArrayList<String>();
    private static ArrayList<String> emailMarketing = new ArrayList<String>();
    private static ArrayList<String> contentMarketing = new ArrayList<String>();
    private static ArrayList<String> socialMedia = new ArrayList<String>();
    private static ArrayList<String> analytics = new ArrayList<String>();
    private static ArrayList<String> cleanMarket = new ArrayList<>();

    private static final ArrayList<String> OTHER = new ArrayList<String>();

    //Create count mapping with tools
    private static TreeMap<Integer, ArrayList<String>> countData = new TreeMap<Integer, ArrayList<String>>();

    //Create user list mapping with tools
    private static HashMap<String, ArrayList<String>> companyTools = new HashMap<String, ArrayList<String>>();


    public void run() {

        //Declare file directory
        String csvFile = "/Users/nielskjer/Downloads/stacklist.csv";

        //Initialize reader objects
        BufferedReader br1 = null;
        BufferedReader br2 = null;

        //Establish line separators
        String line = "";
        String splitVal = ",";

        try {
            //Create buffered reader objects
            br1 = new BufferedReader(new FileReader(csvFile));
            br2 = new BufferedReader(new FileReader(csvFile));

            while ((line = br1.readLine()) != null) {
                String[] inLine = line.split(splitVal);
//                String category = inLine[6];
//                String company = inLine[1];
//                String tool = inLine[5];
//                String primaryCategory = inLine[0];
                String category = inLine[0];
                String company = inLine[1];
                String tool = inLine[5];


                //Add an empty arraylist for each company being read from file
                companyTools.put(company, new ArrayList<String>());

                //Check each tool's category and add to respective ArrayList
                //Set counter
                int duplicates = 0;

                //Grab all companies and store in an ArrayList
                companyList.add(company);
                //System.out.println(companyList.toString());
                if (category.equals("Email Marketing")) {
                    //Check each item in the list for duplicates in the read data
                    for (int i = 0; i < emailMarketing.size(); i++) {
                        //Increment duplicate values if match is found
                        if (emailMarketing.contains("Google Analytics")) emailMarketing.remove("Google Analytics");
                        if (emailMarketing.get(i).equals(tool)) duplicates++;
                    }
                    if (duplicates == 0) emailMarketing.add(tool);
                    //Reset duplicate counter to 0
                    duplicates = 0;

                } else if (category.equals("Content Marketing")) {
                    for (int i = 0; i < contentMarketing.size(); i++) {
                        if (contentMarketing.contains("Google Analytics")) contentMarketing.remove("Google Analytics");
                        if (contentMarketing.get(i).equals(tool)) duplicates++;
                    }
                    if (duplicates == 0) contentMarketing.add(tool);
                    duplicates = 0;

                } else if (category.equals("Social Media Management")) {
                    for (int i = 0; i < socialMedia.size(); i++) {
                        if (socialMedia.contains("Google Analytics")) socialMedia.remove("Google Analytics");
                        if (socialMedia.get(i).equals(tool)) duplicates++;
                    }
                    if (duplicates == 0) socialMedia.add(tool);
                    duplicates = 0;

                } else if (category.equals("BI / Analytics")) {
                    for (int i = 0; i < analytics.size(); i++) {
                        //System.out.println(analytics.get(i));
                        if (analytics.contains("Google Analytics")) analytics.remove("Google Analytics");
                        if (analytics.get(i).equals(tool)) duplicates++;
                    }
                    if (duplicates == 0) analytics.add(tool);
                    duplicates = 0;
                } else {
                    OTHER.add(tool);
                }
            }


            while ((line = br2.readLine()) != null) {
                String[] inLine = line.split(splitVal);
//                String company = inLine[1];
//                String tool = inLine[5];
                String category = inLine[0];
                String company = inLine[1];
                String tool = inLine[5];
                //Map marketing tools to companies
                //for (int i = 0; i < inLine.length; i++) { System.out.println(inLine[0]+inLine[1]+inLine[2]+inLine[3]+inLine[4]+inLine[5]+inLine[6]); }
                if (companyTools.keySet().contains(company)) {
                    if (emailMarketing.contains(tool)) {
                        if (!companyTools.get(company).contains(tool) && !tool.equals("") &&!tool.equals("Slack")) companyTools.get(company).add(tool);
                    }
                    if (contentMarketing.contains(tool)) {
                        if (!companyTools.get(company).contains(tool) && !tool.equals("") && !tool.equals("Slack")) companyTools.get(company).add(tool);
                    }
                    if (socialMedia.contains(tool)) {
                        if (!companyTools.get(company).contains(tool) && !tool.equals("") && !tool.equals("Slack")) companyTools.get(company).add(tool);
                    }
                    if (analytics.contains((tool))) {
                        if (!companyTools.get(company).contains(tool) && !tool.equals("") && !tool.equals("Google Analytics") && !tool.equals("Slack")) companyTools.get(company).add(tool);
                    }
                }
            }
            System.out.println(companyTools.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br1 != null) {
                try {
                    br1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /** User output/Debugging **/

        System.out.println(companyTools.toString());
        System.out.println("------");
        System.out.println("Email marketing tools: ");
        System.out.println(emailMarketing);
        System.out.println("-------");
        System.out.println("Content marketing tools: ");
        System.out.println(contentMarketing);
        System.out.println("-------");
        System.out.println("Social media marketing tools: ");
        System.out.println(socialMedia);
        System.out.println("------");
        System.out.println("BI / Analytics tools: ");
        System.out.println(analytics);
    }

    /** Helper Methods **/

    /** Access data with the get method.
     *
     *  Args: takes an email marketing category or returns the irrelevant list.
     */
    public ArrayList<String> get(String category) {
        if (category.equals("email")) {
            return emailMarketing;
        } else if (category.equals("content")) {
            return contentMarketing;
        } else if (category.equals("social")) {
            return socialMedia;
        } else if (category.equals("analytics")) {
            return analytics;
        } else if (category.equals("list")) {
            return companyList;
        } else {
            //System.out.println("Invalid Category: Please try again.");
            return OTHER;
        }
    };


    public TreeMap<Integer, ArrayList<String>> getCountData() {
        return countData;
    };

    public HashMap<String, ArrayList<String>> getCompanyTools() {
        return companyTools;
    };



    /**       **/
    /** Saved Space **/

}
