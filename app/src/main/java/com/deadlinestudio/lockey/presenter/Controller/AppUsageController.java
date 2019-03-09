package com.deadlinestudio.lockey.presenter.Controller;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Pair;

import com.deadlinestudio.lockey.presenter.Activity.BaseActivity;
import com.deadlinestudio.lockey.presenter.Item.ItemApplock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AppUsageController extends BaseActivity {
    ArrayList<ItemApplock> Applist;
    List<Pair<ItemApplock, Long>> AppRank;

    public AppUsageController(Activity act){
        AppLockController alc = new AppLockController(act.getApplicationContext());
        this.Applist = alc.LoadAppList();
        this.AppRank = new LinkedList<>();
    }

    public AppUsageController(ArrayList<ItemApplock> Applist){
        this.Applist = Applist;
        this.AppRank = new LinkedList<>();
    }

    private static List sortByValue(final Map<String, UsageStats> stats){
        List<String> list = new ArrayList<>();
        list.addAll(stats.keySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1,Object o2){
                Object v1 = stats.get(o1);
                Object v2 = stats.get(o2);

                return ((Comparable)((UsageStats) v1).getTotalTimeInForeground()).compareTo(((UsageStats) v2).getTotalTimeInForeground());
            }
        });

        Collections.reverse(list);
        return list;
    }
    public void getAppUsageTime(Context context){
        UsageStatsManager usage = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        long start = calendar.getTimeInMillis();
        long end = System.currentTimeMillis();
        Map<String, UsageStats> stats = usage.queryAndAggregateUsageStats(start, end);
        // sort by value
        Iterator it = sortByValue(stats).iterator();
        if (stats != null)
        {
            int count = 0;
            while (it.hasNext()) {
                UsageStats usageStats = stats.get(it.next());
                if(count > 4)
                    break;
                for(ItemApplock al : Applist){
                    if(al.getAppPackage().equals(usageStats.getPackageName())) {
                        AppRank.add(Pair.create(al,usageStats.getTotalTimeInForeground()));
                        ++count;
                    }
                }
            }
        }
        else
            Log.d("isRooting stats is NULL","");
    }

    public List<Pair<ItemApplock, Long>> getAppRank(){
        return AppRank;
    }
}
