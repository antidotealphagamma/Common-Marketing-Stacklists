/**
 * Created by nielskjer on 7/27/16.
 */

import java.util.*;
import java.io.*;


public class findData {

    /** Instance Variables **/
    private static ArrayList<String> companyList;
    private static ArrayList<String> emailMarketingTools;
    private static ArrayList<String> contentMarketingTools;
    private static ArrayList<String> socialMediaTools;
    private static ArrayList<String> analyticsTools;
    private static ArrayList<String> OTHER;
    private static TreeMap<Integer, ArrayList<String>> countData;
    private static HashMap<String, ArrayList<String>> companyTools;

    private static ArrayList<String> fullStackCompanies = new ArrayList<String>();
    private static ArrayList<String> partialStackCompanies = new ArrayList<String>();



    public void findData() {

        //Parse the file and take the data from comma separated values.
        FileTaker takeFile = new FileTaker();
        takeFile.run();

        //Grab data
        companyList = takeFile.get("list");
        emailMarketingTools = takeFile.get("email");
        contentMarketingTools = takeFile.get("content");
        socialMediaTools = takeFile.get("social");
        analyticsTools = takeFile.get("analytics");
        OTHER = takeFile.get("other");

        //Grab maps
        countData = takeFile.getCountData();
        companyTools = takeFile.getCompanyTools();

        //Initialize the counting TreeMap with empty ArrayLists
        int initTemp = 0;
        while (initTemp <= 20) {
            countData.put(initTemp, new ArrayList<String>());
            initTemp++;
        }

    }

    public void findMatch() {

        //Temporary ArrayList
        ArrayList<String> tempList = new ArrayList<String>();

        //Counters
        int emailCount, contentCount, socialCount, analyticsCount, totalCount;
        emailCount = 0; contentCount = 0; socialCount = 0; analyticsCount = 0; totalCount = 0;

        //Flags
        boolean email, content, social, analytics, isFullStack, isPartialStack;
        email = false; content = false; social = false;
        analytics = false; isFullStack = false; isPartialStack = false;

        //check if each company in the keyset matches with primary categories
        for (String company : companyTools.keySet()) {

            //Grab each list of tools a company is using
            tempList = companyTools.get(company);

            //Clean none flag
            if (tempList.contains("None")) tempList.remove("None");

            //Iterate through each email marketing tool
            for (int i = 0; i < emailMarketingTools.size(); i++) {

                //Check if each email marketing tool is in the list for each tool
                if (tempList.contains(emailMarketingTools.get(i))) {
                    emailCount++;
                    email = true;
                }
            }

            //Iterate through each content marketing tool
            for (int i = 0; i < contentMarketingTools.size(); i++) {

                //Check if each content marketing tool is in the stack of a company
                if (tempList.contains(contentMarketingTools.get(i))) {
                    contentCount++;
                    content = true;
                }
            }

            //Iterate through each social media marketing tool
            for (int i = 0; i < socialMediaTools.size(); i++) {

                //Check if each social media marketing tool is in the stack of a company
                if (tempList.contains(socialMediaTools.get(i))) {
                    socialCount++;
                    social = true;
                }
            }

            //Iterate through each analytics tool
            for (int i = 0; i < analyticsTools.size(); i++) {

                //Check if each analytics tool is in the stack of a company
                if (tempList.contains(analyticsTools.get(i))) {
                    analyticsCount++;
                    analytics = true;
                }
            }

            //Combine each count
            totalCount = emailCount + contentCount + socialCount + analyticsCount;

            //Check if a company has a full marketing stack
            if (email && content && social && analytics) {
                isFullStack = true;
            } else if (email || content || social || analytics) {
                isPartialStack = true;
            } else {
                isPartialStack = false; isFullStack = false;
            }

            //Add company to tallied TreeMap
            countData.get(totalCount).add(company);

            //If a company has a full marketing stack
            if (isFullStack) {
               fullStackCompanies.add(company);
            }

            //If a company has a partial marketing stack
            if (isPartialStack) {
                partialStackCompanies.add(company);
            }

            //Reset counter and boolean values;
            isPartialStack = false; isFullStack = false;
            email = false; social = false; content = false; analytics = false;
            emailCount = 0; socialCount = 0; contentCount = 0; analyticsCount = 0;
            totalCount = 0;
        }

        /** Debugging **/
        System.out.println("Full stack companies: ");
        System.out.println(fullStackCompanies);

        System.out.println("[][][][][][][][][][][]");
        System.out.println("Partial stack companies: ");
        System.out.println(partialStackCompanies);

        System.out.println("[][][][][][][][][][][][][]");
        System.out.println("Count Data: ");
        System.out.println(countData.toString());
    }

}
