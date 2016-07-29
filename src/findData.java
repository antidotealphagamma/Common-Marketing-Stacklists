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

    //Storage for stack count
    private static TreeMap<String, Integer> emailMarketingFullCount = new TreeMap<>();
    private static TreeMap<String, Integer> contentMarketingFullCount = new TreeMap<>();
    private static TreeMap<String, Integer> socialMediaFullCount = new TreeMap<>();
    private static TreeMap<String, Integer> analyticsFullCount = new TreeMap<>();

    //Partial Counts
    private static TreeMap<String, Integer> emailMarketingPartialCount = new TreeMap<>();
    private static TreeMap<String, Integer> contentMarketingPartialCount = new TreeMap<>();
    private static TreeMap<String, Integer> socialMediaPartialCount = new TreeMap<>();
    private static TreeMap<String, Integer> analyticsPartialCount = new TreeMap<>();



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
//        System.out.println("Full stack companies: ");
//        System.out.println(fullStackCompanies);
//
//        System.out.println("[][][][][][][][][][][]");
//        System.out.println("Partial stack companies: ");
//        System.out.println(partialStackCompanies);
//
//        System.out.println("[][][][][][][][][][][][][]");
//        System.out.println("Count Data: ");
//        System.out.println(countData.toString());
//
//        System.out.println("Total full companies" + fullStackCompanies.size());

        System.out.println(companyTools.toString());

    }

    //Alphabetical sort
    public ArrayList<String> mergeSort(ArrayList<String> fullList) {
        ArrayList<String> left = new ArrayList<String>();
        ArrayList<String> right = new ArrayList<String>();
        int middle;

        if (fullList.size() == 1) {
            return fullList;
        } else {
            middle = fullList.size()/2;
            //Left side copy
            for (int i = 0; i < middle; i++) {
                left.add(fullList.get(i));
            }

            //Right side copy
            for (int i = middle; i < fullList.size(); i++) {
                right.add(fullList.get(i));
            }

            //Sort the left and right halves of the ArrayList
            left = mergeSort(left);
            right = mergeSort(right);

            //Merge sorted halves
            merge(left, right, fullList);
        }

        return fullList;
    }

    //Helper method for recursive mergeSort
    private void merge(ArrayList<String> left, ArrayList<String> right, ArrayList<String> fullList) {
        int leftIndex = 0;
        int rightIndex = 0;
        int fullIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if ((left.get(leftIndex).compareTo(right.get(rightIndex))) < 0) {
                fullList.set(fullIndex, left.get(leftIndex));
                leftIndex++;
            } else {
                fullList.set(fullIndex, right.get(rightIndex));
                rightIndex++;
            }
            fullIndex++;
        }

        ArrayList<String> temp;
        int tempIndex;
        if (leftIndex >= left.size()) {
            //Left arraylist has no room
            temp = right;
            tempIndex = rightIndex;
        } else {
            //Right arrayList has no room
            temp = left;
            tempIndex = leftIndex;
        }

        //Copy the rest of the arraylist not used up
        for (int i = tempIndex; i < temp.size(); i++) {
            fullList.set(fullIndex, temp.get(i));
            fullIndex++;
        }
    }

    //Sort all ArrayLists in alphabetical order
    public void sortLists() {
        mergeSort(contentMarketingTools);
        mergeSort(emailMarketingTools);
        mergeSort(socialMediaTools);
        mergeSort(analyticsTools);
        mergeSort(partialStackCompanies);
        mergeSort(fullStackCompanies);

//        System.out.println(contentMarketingTools.toString());
//        System.out.println(emailMarketingTools.toString());
//        System.out.println(socialMediaTools.toString());
//        System.out.println(analyticsTools.toString());
//        System.out.println(partialStackCompanies.toString());
//        System.out.println(fullStackCompanies.toString());

    }

    //Stores all the found data into TreeMaps
//    public void storeCount() {
//
//        //Full Stack
//        for (int i = 0; i < fullStackCompanies.size(); i++) {
//            String company = fullStackCompanies.get(i);
//            if (companyTools.containsKey(company)) {
//                ArrayList<String> temp = companyTools.get(company);
//
//                //Iterate through each category
//
//            }
//        }
//
//        //Partial Stack
//    }

    //Calculates the most common full marketing stacklists
//    public void calculateFull() {
//

//    }

}
