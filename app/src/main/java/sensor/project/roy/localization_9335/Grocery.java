package sensor.project.roy.localization_9335;

import android.net.wifi.ScanResult;

import java.util.ArrayList;

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


    public static float[] computeLocation(ArrayList<ScanResult> outerList){
        ArrayList<ScanResult> mediaList = new ArrayList<>();
        int floor;
        int[] counter = {0,0,0};
        for(int i = 0;i<outerList.size();i++){
            if((floor=Grocery.checkFloor(outerList.get(i).BSSID)) > 0){
                mediaList.add(outerList.get(i));
                counter[floor-1] += 1;
            }
        }
        System.out.println(counter[0]+" "+ counter[1]+" "+ counter[2]);
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
        ArrayList<Float> rssiList = getRealTimeList(mediaList);
        if(rssiList == null)    //not enough AP scanned
            return null;
        ArrayList<String> rankedCoor = euclidean(rssiList, level3RSSI);
        /*
        Deal with "rankedCoor", get topk coordinates as you wish
         */
        System.out.println(rankedCoor.toString());
        float[] result = {rankedCoor.get(0).charAt(0)-48, rankedCoor.get(0).charAt(1)-48, floor};
        return result;
    }
    public static ArrayList getRealTimeList(ArrayList<ScanResult> mediaList){   //return a list like [-67,-79.....]
        ArrayList<Float> rssiList = new ArrayList<>(6);
        rssiList.add((float) 1.0);      //noob way
        rssiList.add((float)1.0);
        rssiList.add((float)1.0);
        rssiList.add((float)1.0);
        rssiList.add((float)1.0);
        rssiList.add((float)1.0);
        /*for(int i = 0;i<rssiList.size();i++)    //initialize "rssiList" with (float)1
            rssiList.set(i, (float)1);*/
        //System.out.println(mediaList.toString());
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
        for(int i = 0;i<rssiList.size();i++){
            if(rssiList.get(i) == (float)1 ) {    //not enough AP scanned
                System.out.println("not enough AP "+rssiList.toString());
                return null;
            }
        }
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
    public static int checkFloor(String mac){       //return which floor this MAC belongs
        /*for(int i = 0;i<level3BaseMac.length;i++)
            for(int j = 0;j<level3BaseMac[i].length;j++){
                if(mac.equals(level3BaseMac[i][j]))
                    return 3;
            }
        for(int i = 0;i<level2BaseMac.length;i++)
            for(int j = 0;j<level2BaseMac[i].length;j++){
                if(mac.equals(level2BaseMac[i][j]))
                    return 2;
            }
        for(int i = 0;i<level1BaseMac.length;i++)
            for(int j = 0;j<level1BaseMac[i].length;j++){
                if(mac.equals(level1BaseMac[i][j]))
                    return 1;
            }*/
        if(checkAP(mac, level3BaseMac)>-1)
            return 3;
        if(checkAP(mac, level2BaseMac)>-1)
            return 2;
        if(checkAP(mac, level1BaseMac)>-1)
            return 1;
        return -1;
    }
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
    //get ranked coordinates here
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
            for(int j = 0;j<needSort.size();j++){
                Object[] temp;
                if((double)needSort.get(i)[1] > (double)needSort.get(j)[1]){
                    temp = needSort.get(j);
                    needSort.set(j, needSort.get(i));
                    needSort.set(i, temp);
                }
            }
    }
}