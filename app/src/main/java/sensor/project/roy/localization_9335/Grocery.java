package sensor.project.roy.localization_9335;

import android.net.wifi.ScanResult;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

class Grocery{
    Grocery()
    {

    }

    static String fileIdle = "dataIdle";
    static String fileBusy = "dataBusy";
    static String fileEmpty = "dataEmpty";
    static String level1BaseMac[][] = null;
    static String level2BaseMac[][] = null;
    static String level3BaseMac[][] = {{"08:cc:68:b4:b8:40", "08:cc:68:b4:b8:41", "08:cc:68:b4:b8:42"},
            {"08:cc:68:b4:b8:4d", "08:cc:68:b4:b8:4e", "08:cc:68:b4:b8:4f"},
            {"08:cc:68:b4:ba:1d", "08:cc:68:b4:ba:1e", "08:cc:68:b4:ba:1f"},
            {"08:cc:68:b4:be:90", "08:cc:68:b4:be:91", "08:cc:68:b4:be:92"},
            {"08:cc:68:b4:c5:d0", "08:cc:68:b4:c5:d1", "08:cc:68:b4:c5:d2"},
            {"08:cc:68:b4:c5:dd", "08:cc:68:b4:c5:de", "08:cc:68:b4:c5:df"}};
    static Object level3RSSI[][] = {{"11", -84.1, -84.0, -88.1, -82.0, -79.0, -74.7},
                    {"12", -83.9, -84.1, -85.8, -74.9, -79.2, -74.2},
                    {"13", -83.6, -83.5, -76.7, -64.8, -82.9, -82.5},
                    {"21", -82.5, -79.1, -86.5, -70.5, -72.3, -67.8},
                    {"22", -81.4, -77.8, -85.0, -68.5, -75.7, -72.2},
                    {"23", -81.1, -77.2, -71.0, -57.9, -79.5, -78.2},
                    {"31", -71.9, -66.2, -81.0, -77.1, -63.6, -62.3},
                    {"32", -68.6, -66.0, -80.2, -74.6, -71.9, -69.0},
                    {"33", -75.0, -73.9, -68.7, -66.8, -77.7, -72.8},
                    {"41", -68.1, -69.1, -81.5, -79.4, -67.0, -61.5},
                    {"42", -60.7, -61.3, -73.8, -77.4, -70.8, -68.7},
                    {"43", -76.9, -76.1, -57.8, -69.7, -80.5, -75.8},
                    {"51", -74.2, -71.8, -74.1, -86.0, -76.7, -73.7},
                    {"52", -72.6, -73.0, -68.9, -83.3, -81.1, -79.5},
                    {"53", -80.8, -86.0, -67.3, -69.8, -84.6, -82.4},
                    {"61", -84.1, -80.8, -79.3, -85.8, -83.9, -82.2},
                    {"62", -79.4, -80.1, -77.2, -84.2, -83.2, -82.2},
                    {"63", -84.5, -86.5, -69.7, -72.1, -85.4, -86.0}};
    static Object level3Old[][] ={{"11", -84.14035087719299, -83.96666666666667, -88.09803921568627, -82.00000000000001, -79.03333333333332, -74.65},
            {"12", -83.875, -84.06666666666666, -85.77777777777777, -74.86666666666667, -79.21666666666667, -74.23333333333333},
            {"13", -83.61666666666665, -83.50833333333334, -76.74999999999999, -64.76666666666668, -82.91666666666669, -82.5},
            {"21", -82.50000000000001, -79.08333333333334, -86.53333333333333, -70.53333333333333, -72.3, -67.85},
            {"22", -81.39166666666665, -77.76666666666667, -84.96666666666665, -68.55, -75.65, -72.23333333333332},
            {"23", -81.09166666666667, -77.18333333333332, -71.05, -57.9, -79.48333333333333, -78.21666666666665},
            {"31", -71.93333333333334, -66.23333333333332, -81.03333333333333, -77.08333333333334, -63.633333333333326, -62.31666666666666},
            {"32", -68.6, -66.03333333333333, -80.2, -74.63333333333333, -71.88333333333333, -68.98333333333333},
            {"33", -75.03333333333333, -73.93333333333334, -68.71666666666667, -66.76666666666667, -77.70000000000002, -72.85},
            {"41", -68.13333333333333, -69.06666666666665, -81.53333333333335, -79.41666666666666, -67.03333333333332, -61.45000000000001},
            {"42", -60.716666666666676, -61.283333333333346, -73.78333333333333, -77.35000000000001, -70.78333333333333, -68.73333333333333},
            {"43", -76.91666666666666, -76.11666666666667, -57.76666666666667, -69.73333333333335, -80.46666666666667, -75.76666666666667},
            {"51", -74.16666666666666, -71.81666666666668, -74.06666666666668, -85.975, -76.66666666666666, -73.68333333333332},
            {"52", -72.6, -73.01666666666667, -68.93333333333334, -83.30000000000001, -81.1, -79.45000000000002},
            {"53", -80.83333333333334, -85.98333333333335, -67.26666666666667, -69.76666666666667, -84.56666666666668, -82.42592592592592},
            {"61", -84.08333333333333, -80.83333333333333, -79.26666666666667, -85.83333333333333, -83.91666666666667, -82.16666666666666},
            {"62", -79.36666666666665, -80.13333333333334, -77.16666666666667, -84.21428571428571, -83.24166666666665, -82.18333333333331},
            {"63", -84.475, -86.53333333333333, -69.71666666666667, -72.11666666666667, -85.375, -86.025}};
    static Object level3SetA[][] = {{"11", -84.08333333333333, -86.75, -73.74999999999999, -75.20833333333333, -84.66666666666667, -81.0},
            {"12", -81.62500000000001, -85.0, -81.70833333333333, -88.625, -85.91666666666667, -83.95833333333334},
            {"13", -78.29166666666666, -83.8125, -84.04166666666667, -87.23809523809526, -84.29166666666667, -84.16666666666667},
            {"21", -80.35416666666666, -84.0, -62.6875, -74.875, -83.89583333333334, -83.55208333333333},
            {"22", -68.75, -77.66666666666667, -68.04166666666667, -83.54166666666667, -81.375, -78.83333333333333},
            {"23", -78.29166666666667, -76.66666666666666, -73.62500000000001, -86.79166666666667, -78.375, -73.375},
            {"31", -77.50000000000001, -81.52380952380953, -63.83333333333333, -69.74999999999999, -78.99999999999999, -82.51851851851853},
            {"32", -61.41666666666667, -68.70833333333334, -74.58333333333333, -74.25, -71.45833333333333, -65.70833333333333},
            {"33", -67.125, -70.54166666666666, -80.20833333333333, -77.625, -70.83333333333334, -66.25},
            {"41", -74.58333333333334, -81.18518518518518, -70.20833333333333, -63.04166666666667, -73.70833333333334, -73.66666666666666},
            {"42", -66.25, -70.08333333333333, -80.75, -75.83333333333333, -69.625, -65.875},
            {"43", -68.33333333333333, -70.87878787878788, -81.79166666666667, -78.125, -63.458333333333336, -61.208333333333336},
            {"51", -80.41666666666667, -79.95833333333334, -74.24999999999999, -56.083333333333336, -79.0, -77.58333333333333},
            {"52", -80.29166666666667, -80.66666666666667, -82.25, -70.625, -74.66666666666667, -72.99999999999999},
            {"53", -81.72916666666667, -82.37500000000001, -86.44444444444446, -70.29166666666667, -71.04166666666667, -72.33333333333333},
            {"61", -85.27083333333333, -84.70370370370371, -78.75, -67.79166666666667, -84.08333333333334, -81.08333333333333},
            {"62", -83.22916666666666, -84.58333333333334, -81.16666666666666, -79.04166666666667, -76.83333333333333, -80.0},
            {"63", -83.77083333333334, -81.75, -86.86666666666666, -80.45833333333334, -80.24999999999999, -77.45833333333333 }};

    static Object level3SetB[][] = {{"11", -83.41666666666666, -84.91666666666666, -69.625, -75.41666666666666, -86.08333333333333, -85.87499999999999},
            {"12", -84.41666666666666, -83.70833333333334, -82.91666666666666, -82.44444444444444, -82.75000000000001, -81.5},
            {"13", -86.0, -86.0, -81.87499999999999, -91.0, -85.79166666666667, -81.5},
            {"21", -82.62499999999999, -83.33333333333333, -61.0, -73.25, -85.35416666666666, -83.08333333333333},
            {"22", -73.91666666666666, -76.2962962962963, -73.45833333333333, -82.83333333333333, -79.08333333333333, -82.25000000000001},
            {"23", -76.14285714285714, -76.61904761904762, -76.61904761904762, -86.0, -75.57142857142857, -76.14285714285714},
            {"31", -78.95833333333334, -79.73333333333333, -59.125, -72.54166666666666, -81.83333333333333, -81.16666666666667},
            {"32", -62.91666666666667, -70.54166666666666, -76.0, -75.375, -70.08333333333333, -68.66666666666667},
            {"33", -69.20833333333334, -70.875, -83.50000000000001, -77.04166666666667, -69.58333333333334, -68.875},
            {"41", -77.33333333333334, -79.3, -73.0, -66.25, -75.41666666666667, -74.79166666666666},
            {"42", -69.16666666666667, -73.04166666666667, -81.375, -79.04166666666666, -70.95833333333333, -67.75},
            {"43", -74.25, -75.29166666666669, -83.875, -83.58333333333334, -64.54166666666666, -57.708333333333336},
            {"51", -79.19047619047618, -78.60000000000001, -75.45833333333333, -59.24999999999999, -84.33333333333333, -80.41666666666667},
            {"52", -79.39583333333333, -79.41666666666667, -87.5, -70.66666666666666, -74.04166666666666, -75.16666666666667},
            {"53", -82.1875, -79.95833333333334, -88.62500000000001, -73.91666666666666, -70.25, -69.58333333333333},
            {"61", -84.5, -84.14285714285714, -82.33333333333333, -72.77083333333331, -82.5, -81.31372549019608},
            {"62", -86.0, -82.29166666666667, -86.04166666666667, -79.75, -76.91666666666667, -74.49999999999999},
            {"63", -85.0, -82.29166666666667, -85.41666666666667, -79.29166666666667, -79.83333333333333, -78.58333333333334}};

    static Object level3SetC[][] = {{"11", -82.16666666666666, -83.375, -71.0, -75.29166666666666, -85.72916666666666, -84.75},
            {"12", -82.33333333333334, -83.79166666666667, -81.45833333333333, -86.0, -79.41666666666667, -82.87500000000001},
            {"13", -82.625, -82.62499999999999, -80.66666666666666, -86.66666666666667, -83.37499999999999, -82.625},
            {"21", -83.33333333333333, -80.83333333333334, -59.375, -73.16666666666666, -85.83333333333334, -82.41666666666667},
            {"22", -73.125, -76.16666666666666, -73.875, -81.375, -78.25, -84.33333333333334},
            {"23", -76.66666666666667, -76.41666666666666, -77.16666666666667, -82.40740740740739, -79.66666666666666, -76.625},
            {"31", -81.33333333333334, -79.90476190476191, -60.04166666666667, -67.91666666666666, -82.39583333333333, -77.91666666666667},
            {"32", -62.95833333333333, -67.33333333333334, -76.58333333333333, -75.41666666666667, -70.29166666666667, -70.25},
            {"33", -69.20833333333333, -72.41666666666667, -83.41666666666666, -79.20833333333334, -69.58333333333334, -66.875},
            {"41", -77.58333333333333, -76.69696969696969, -69.45833333333334, -61.333333333333336, -77.33333333333333, -76.20833333333334},
            {"42", -72.87499999999999, -75.66666666666667, -76.91666666666667, -76.91666666666667, -70.875, -66.33333333333334},
            {"43", -73.91666666666666, -75.5, -80.08333333333334, -81.5, -65.75000000000001, -61.12499999999999},
            {"51", -81.66666666666667, -77.66666666666667, -74.29166666666667, -56.74999999999999, -80.875, -77.125},
            {"52", -80.625, -81.0, -81.66666666666667, -72.04166666666667, -75.125, -73.04166666666667},
            {"53", -81.91666666666667, -79.125, -81.79166666666667, -71.375, -71.33333333333333, -69.41666666666667},
            {"61", -83.29166666666667, -86.16666666666667, -82.16666666666667, -63.20833333333333, -82.0625, -85.0},
            {"62", -84.97916666666666, -86.70833333333331, -89.0, -78.79166666666667, -77.12499999999999, -78.41666666666666},
            {"63", -85.36666666666666, -83.99999999999999, -87.77777777777779, -80.375, -80.20833333333333, -77.29166666666667}};

    /**
     * entrance method
     * @param outerList
     * @param indicator specify which data set to use
     * @return
     */
    public static String[] computeLocation(ArrayList<ScanResult> outerList, int indicator){
        ArrayList<ScanResult> mediaList = new ArrayList<>();
        ArrayList<String> rankedCoor;
        int floor;
        int[] counter = {0,0,0};
        for(int i = 0;i<outerList.size();i++){
            if((floor=Grocery.checkFloor(outerList.get(i).BSSID)) > 0){ //limit macAddress in baseMacSet
                mediaList.add(outerList.get(i));
                counter[floor-1] += 1;
            }
        }
        floor = counter[2] > counter[1]? 3:2;       //determine which floor has more MAC
        floor = counter[floor-1] > counter[0]? floor:1;
        ScanResult temp;
        for(int i = 0;i<mediaList.size()-1;i++){    //sort the list on MAC
            for(int j = i+1;j<mediaList.size();j++){
                if(compareScanResult(mediaList.get(i), mediaList.get(j))>0){  // i > j
                    temp = mediaList.get(i);
                    mediaList.set(i,mediaList.get(j));
                    mediaList.set(j,temp);
                }
            }
        }
        //ArrayList<Float> rssiList = getRealTimeList(mediaList);
        ArrayList<Float> rssiList = getRealTimeList1(mediaList);
        int count = 0;
        for(int i = 0;i<rssiList.size();i++){
            if(rssiList.get(i) == (float)0 ) {    //not enough AP scanned
                count ++;
            }
        }
        /*if(rssiList == null)    //not enough AP scanned
            return null;*/
        System.out.println(rssiList);
        System.out.println("count "+count);
        if(count>2)
            return null;
        /*
        Might read data from file to replace the pre-written data
         */
        if(indicator == 1)
            rankedCoor = euclidean(rssiList, level3RSSI);
        else
            rankedCoor = euclidean(rssiList, level3SetA, level3SetB, level3SetC);
        /*
        Deal with "rankedCoor", get top-k coordinates as you wish
         */
        System.out.println(rankedCoor.toString());
        /*String[] result = {rankedCoor.get(0), rankedCoor.get(1), rankedCoor.get(2),
                String.valueOf(floor), String.valueOf(6-count)};*/
        String[] result = {rankedCoor.get(0).substring(0,1),rankedCoor.get(0).substring(1,2),String.valueOf(floor)};
        //System.out.println("rankedCoor --> "+result);
        return result;
    }

    /**
     * given “mediaList” containing sorted and processed ScanResults, this method calculates
     * the average RSSI for every selected AP (3 mac addresses).
     * @param mediaList
     * @return
     */
    public static ArrayList getRealTimeList(ArrayList<ScanResult> mediaList){   //return a list like [-67,-79.....]
        ArrayList<Float> rssiList = new ArrayList<>(6);
        rssiList.add((float) 0.0);      //noob way of initialize a 6-length list
        rssiList.add((float)0.0);
        rssiList.add((float)0.0);
        rssiList.add((float)0.0);
        rssiList.add((float)0.0);
        rssiList.add((float) 0.0);
        for(int a = 0; a<mediaList.size(); a++)
            System.out.println(mediaList.get(a).BSSID+"\t"+mediaList.get(a).level);
        for(int i = 0;i<mediaList.size();  ){   //fill "rssiList" with average rssi
            int loc = getSameAPinListLocation(mediaList, i);
            int count=0;
            int totalRSSI=0;
            if(loc > 0){
                for(int j = i; j<=i+loc; j++){
                    count++;
                    totalRSSI += mediaList.get(j).level;
                }
                int APIndex = checkAP(mediaList.get(i).BSSID, level3BaseMac);
                //System.out.println("APIndex "+APIndex+" "+rssiList.size());
                rssiList.set(APIndex, totalRSSI/(float)count);
                i += loc+1;
            }
            else{
                int APIndex = checkAP(mediaList.get(i).BSSID, level3BaseMac);
                rssiList.set(APIndex, (float)mediaList.get(i).level);
                i++;
            }
        }
        System.out.println("old realTimeList --> " + rssiList.toString());
        return rssiList;
    }

    /**
     * given “mediaList” containing sorted and processed ScanResults, this method calculates
     * the average RSSI for every selected AP (3 mac addresses).
     * @param mediaList
     * @return
     */
    private static ArrayList getRealTimeList1(ArrayList<ScanResult> mediaList){
        ArrayList<Float> rssiList = new ArrayList<Float>(Arrays.asList((float)0.0, (float)0.0, (float)0.0,
                (float)0.0, (float)0.0, (float)0.0));
        ArrayList<Integer> countList = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

        int APIndex, tempCount;
        float tempRSSI;
        for(int i = 0;i<mediaList.size(); i++) {   //fill "rssiList" with average rssi
            APIndex = checkAP(mediaList.get(i).BSSID, level3BaseMac);
            tempRSSI = rssiList.get(APIndex);
            tempRSSI += mediaList.get(i).level;
            rssiList.set(APIndex, tempRSSI);
            tempCount = countList.get(APIndex);
            tempCount += 1;
            countList.set(APIndex, tempCount);
        }
        for(int j = 0; j<rssiList.size(); j++){
            tempRSSI = rssiList.get(j);
            tempCount = countList.get(j);
            if(tempCount == 0)
                continue;
            rssiList.set(j, tempRSSI / (float) tempCount);
        }
        System.out.println("new realTimeList --> " + rssiList.toString());

        return rssiList;
    }

    public static int getSameAPinListLocation(ArrayList<ScanResult> originList, int index){
        /*if(index == originList.size()-1)    //index is the last
            return -1;*/
        int ahead = index;
        while(ahead != originList.size()-1 && compareScanResult(originList.get(ahead), originList.get(ahead + 1)) == -1){
            ahead ++;
            if(ahead-index == 2)    //prevent the continuous MAC address
                break;
        }
        return ahead - index;
    }
    /*
    method not finished
    */
    public static void makeDB(String fileName){
        ArrayList<String> totalLine = new ArrayList<>();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/9335");
        dir.mkdir();
        File file = new File(dir, fileName);
        try{
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = br.readLine()) != null) {
                totalLine.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i =0; i<totalLine.size();i++){
            String[] timeline = totalLine.get(i).split(" ");
            String coorX = timeline[0];
            String coorY = timeline[1];
        }
    }

    /**
     * check which mac address sets this given “mac” is in, return 1 or 2 or 3
     * corresponding to the floors if exists, or -1 if not.
     * @param mac
     * @return
     */
    public static int checkFloor(String mac){       //return which floor this MAC belongs
        if(checkAP(mac, level3BaseMac)>-1)
            return 3;
        if(checkAP(mac, level2BaseMac)>-1)
            return 2;
        if(checkAP(mac, level1BaseMac)>-1)
            return 1;
        return -1;
    }

    /**
     * check if the given “mac” exists in this “basemac” set or not,
     * return the index in the set if exists, or -1 if not.
     * @param mac
     * @param basemac
     * @return
     */
    public static int checkAP(String mac, String[][] basemac){
        if(basemac == null)
            return -1;
        for(int i = 0;i<basemac.length;i++)
            for(int j = 0;j<basemac[i].length;j++)
                if(mac.equals(basemac[i][j]))
                    return i;
        return -1;
    }

    public static int compareScanResult(ScanResult a, ScanResult b){
        String[] aa = a.BSSID.split(":");
        String[] bb = b.BSSID.split(":");
        String aaa = aa[0] + aa[1] + aa[2] + aa[3] + aa[4] + aa[5];
        String bbb = bb[0] + bb[1] + bb[2] + bb[3] + bb[4] + bb[5];
        long anum = Long.parseLong(aaa, 16);
        long bnum = Long.parseLong(bbb, 16);
        return (int)(anum-bnum);
    }
    public static int findBaseAP(String macAddr){      //index AP in level#Mac
        for(int i=0;i<level3BaseMac.length;i++)
            //print(macAddr, baseMac[i])
            for(int j=0;j<level3BaseMac[i].length;j++)
                if(level3BaseMac[i][j].equals(macAddr))
                    return i;
        return -1;
    }
    /**
    get ranked coordinates here, with 1 RSSIdataSet
     */
    public static ArrayList<String> euclidean(ArrayList<Float> realtimeList, Object[][] databaseList){
        double total=0;
        ArrayList<Object[]> mixedRankList = new ArrayList<>();
        for(int row = 0;row<databaseList.length;row++) {    //build array of {{'63',-78},...{'62',-89}}
            total = 0;
            for (int j = 0; j < realtimeList.size(); j++) {
                total += Math.pow(((double)realtimeList.get(j) - (double)databaseList[row][j + 1]), 2);
            }
            mixedRankList.add(new Object[]{databaseList[row][0], Math.sqrt(total)});
        }
        sortArray(mixedRankList);
        //System.out.println("mixList -- " + mixedRankList.toString());
        ArrayList<String> rankedCoordinate = new ArrayList<>();
        for(int i = 0;i<mixedRankList.size();i++)       //append sorted coordinate
            rankedCoordinate.add((String)mixedRankList.get(i)[0]);
        getOpposite(rankedCoordinate);
        System.out.println(rankedCoordinate.toString());
        return rankedCoordinate;
    }
    private static void getOpposite(ArrayList<String> orderList){
        String temp;
        for(int i = 0;i<orderList.size();i++){
            temp = orderList.get(i);
            orderList.set(i, (7-Integer.parseInt(temp.substring(0,1)))+""+(4-Integer.parseInt(temp.substring(1,2))));
        }
    }

    /**
     * get ranked coordinates here, with parameters of 3 RSSIdataSets
     * @param realtimeList
     * @param dataAList
     * @param dataBList
     * @param dataCList
     * @return
     */
    public static ArrayList<String> euclidean(ArrayList<Float> realtimeList, Object[][] dataAList,
                                              Object[][] dataBList, Object[][] dataCList){
        double total=0;
        double mean;
        ArrayList<Object[]> mixedRankList = new ArrayList<>();
        for(int row = 0;row<dataAList.length;row++) {    //build array of {{'63',-78},...{'62',-89}}
            total = 0;
            for (int j = 0; j < realtimeList.size(); j++) {
                if(realtimeList.get(j) == (float)0)     //AP not present
                    continue;
                mean = ((double)dataAList[row][j+1]+(double)dataBList[row][j+1]+(double)dataCList[row][j+1])/3;
                total += Math.pow(((double)realtimeList.get(j) - mean), 2);
            }
            mixedRankList.add(new Object[]{dataAList[row][0], Math.sqrt(total)});
        }
        sortArray(mixedRankList);
        /*System.out.println("mixList -- ");
        for(int index = 0; index<mixedRankList.size();index++)
            System.out.print(mixedRankList.get(index)[0]+"..."+mixedRankList.get(index)[1]+" ");
        System.out.println();*/
        ArrayList<String> rankedCoordinate = new ArrayList<>();
        for(int i = 0;i<mixedRankList.size();i++)       //append sorted coordinate
            rankedCoordinate.add((String)mixedRankList.get(i)[0]);
        return rankedCoordinate;
    }

    //get ranked coordinates here
    public static ArrayList<String> cosine(ArrayList<Float> realtimeList, Object[][] databaseList){
        float fenzi = 0;
        float fenmu1 = 0;
        float fenmu2 = 0;
        ArrayList<Object[]> mixedRankList = new ArrayList<>();
        for(int row = 0;row<databaseList.length;row++) {    //build array of {{'63',-78},...{'62',-89}}
            fenzi = 0;
            fenmu1 = 0;
            fenmu2 = 0;
            for (int j = 0; j < realtimeList.size(); j++) {
                fenzi += realtimeList.get(j) * (float)databaseList[row][j+1];
                fenmu1 += Math.pow(realtimeList.get(j), 2);
                fenmu2 += Math.pow((float)databaseList[row][j+1], 2);
            }
            mixedRankList.add(new Object[]{databaseList[row][0], fenzi/Math.sqrt(fenmu1*fenmu2)});
        }
        sortArray(mixedRankList);
        ArrayList<String> rankedCoordinate = new ArrayList<>();
        for(int i = 0;i<mixedRankList.size();i++)       //append sorted coordinate
            rankedCoordinate.add((String)mixedRankList.get(i)[0]);
        return rankedCoordinate;
    }

    public static void sortArray(ArrayList<Object[]> needSort){
        for(int i = 0;i<needSort.size()-1;i++)     //sort array
            for(int j = i+1;j<needSort.size();j++){
                Object[] temp;
                if((double)needSort.get(i)[1] > (double)needSort.get(j)[1]){
                    temp = needSort.get(j);
                    needSort.set(j, needSort.get(i));
                    needSort.set(i, temp);
                }
            }
    }
}