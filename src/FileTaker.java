/**
 * Created by nielskjer on 7/27/16.
 */

import java.lang.reflect.Array;
import java.util.*;


public class FindData {

    /** Instance Variables **/
    private static ArrayList<String> companyList;
    private static ArrayList<String> emailMarketingTools;
    private static ArrayList<String> contentMarketingTools;
    private static ArrayList<String> socialMediaTools;
    private static ArrayList<String> analyticsTools;
    private static ArrayList<String> OTHER;
    private static TreeMap<Integer, ArrayList<String>> countData;
    private static HashMap<String, ArrayList<String>> companyTools;

    private static ArrayList<String> fullStackCompanies = new ArrayList<>();
    private static ArrayList<String> partialStackCompanies = new ArrayList<>();

    //Storage for probability mapping
    private static Map<Float, ArrayList<String>> likelihoodMap = new TreeMap<>();
    private static Map<Integer, ArrayList<String>> mapFinal = new TreeMap<>();


    //Alpha
    private static Map<ArrayList<String>, Integer> map = new HashMap<>();
    private static int totalCount = 0;




    public void FindData() {

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
        while (initTemp <= 100) {
            countData.put(initTemp, new ArrayList<String>());
            initTemp++;
        }
    }

    public void findMatch() {

        //Temporary ArrayList
        List<String> tempList = new ArrayList<>();

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
            //totalCount = 0;

        }





        /** Debugging **/
        System.out.println(totalCount);
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

        //System.out.println(companyTools.toString());

    }

    //Alphabetical sort via MergeSort algorithm
    public static ArrayList<String> mergeSort(ArrayList<String> fullList) {
        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();
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
    private static void merge(ArrayList<String> left, ArrayList<String> right, ArrayList<String> fullList) {
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
            //Left arrayList has no room
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
        mergeSort(companyList);
    }


    public void countLengths() {
        int temp = 0;
        int num = 5;
        for (Integer val : countData.keySet()) {
            ArrayList<String> outList = countData.get(val);
            System.out.println("Count: " + val + " is = " + outList.size());
            System.out.println("~~~~~~~~~~~~~~~~~");

            if (val >= num) temp += val;
        }
        System.out.println("Total of " + num + " lists is " + temp);
    }

    public ArrayList<String> getMatchingElements(ArrayList<String> a, ArrayList<String> b) {
        ArrayList<String> retVal = new ArrayList<>();
        if (a.size() >= b.size()) {
            for (int i=0; i < a.size(); i++) {
                for (int j=0; j < b.size(); j++) {
                    if (a.get(i).contains(b.get(j))) retVal.add(a.get(i));
                }
            }
        } else {
            for (int i=0; i < b.size(); i++) {
                for (int j=0; j < a.size(); j++) {
                    if (b.get(i).contains(a.get(j))) retVal.add(b.get(i));
                }
            }
        }
        return retVal;
    };



    public double tallyTotal(Map<Integer, ArrayList<String>> input) {
        double outVal = 0;
        for (Integer temp : input.keySet()) {
            outVal += temp;
        }
        return outVal;
    }

    public void getMostCommon () {
        //int n = 5;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter n filter: ");
        int n = in.nextInt();
        boolean hasElements;
        for (String company : companyTools.keySet()) {
            ArrayList<String> input = companyTools.get(company);
            Collections.sort(companyTools.get(company));
            //Iterate over each list entry in the HashMap of company tools.
            for (Map.Entry<String, ArrayList<String>> listEntry : companyTools.entrySet()) {
                ArrayList<String> tempList = listEntry.getValue();
                ArrayList<String> temp = getMatchingElements(input,tempList);
                hasElements = (temp != null);
                if (map.containsKey(temp)) {
                    if (hasElements && temp.size() >= n) {
                        map.put(temp, map.get(temp) + 1);
                    }
                    totalCount++;
                } else {
                    if (hasElements && input.size() >= n) {
                        map.put(temp, 1);
                    }
                }
            }
        }
    }

    public void filter() {
        System.out.println("Enter the minimum amount of tools to search for their most common stacklists: ");
        Scanner in = new Scanner(System.in);
        int temp = in.nextInt();


        //Ordered frequency of stacklist subsets
        for (ArrayList<String> stack : map.keySet()) {
             mapFinal.put(map.get(stack), stack);
        }

        Iterator<Map.Entry<Integer, ArrayList<String>>> iterator = mapFinal.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ArrayList<String>> entry = iterator.next();
            if (temp > entry.getValue().size()) iterator.remove();

        }

        double divisor = tallyTotal(mapFinal);
        //double divisor = (double)totalCount;
        System.out.println("Union: " + divisor);

        //System.out.println(mapFinal.toString());
        for (Integer k : mapFinal.keySet()) {
            //divisor = divisor(float);
            double tempVal = k/divisor;
            tempVal *= 100;

            System.out.println(mapFinal.get(k));
            System.out.printf(" ========================================================>       Likelihood is %.6f \n", tempVal);
        }

        System.out.println(mapFinal);


    }

//    public String toString() {
//        return null;
//    }

}
