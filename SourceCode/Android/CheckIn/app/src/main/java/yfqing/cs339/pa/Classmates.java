package yfqing.cs339.pa;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//假定传入


public class Classmates {

    private Set<String> unchecked;
    private Set<String> checked;
    private Set<String> suspect;

    public void initial(String names){

        //输入：初始列表（暂时认为是以空格作为分割）
        unchecked= new HashSet<>();
        checked = new HashSet<>();
        suspect = new HashSet<>();

        String[] tmp=names.split(" ");

        for(int i=0;i< tmp.length;++i){
//            Log.e("name",tmp[i]);
            unchecked.add(tmp[i]);
        }
        //将所有同学加入到unchecked中
    }
    public void update_list(String rec){

        Log.e("output",rec);
        if(Objects.equals(rec, "#")){
            Log.e("is #","yes");
            return;
        }

        String[] tmp=rec.split(" ");
        for(int i=0;i< tmp.length;++i){
            Log.e("unit",tmp[i]);
            String[] name_status = tmp[i].split("_");
            add_checked(name_status[0],name_status[1].substring(0,name_status[1].length()));
        }
    }

    public void add_checked(String name,String status){
        if(unchecked.contains(name)){
            unchecked.remove(name);
        }
        Log.e("status",status);
        if(Objects.equals(status, "0")){
            Log.e("case","suspect");
            if(!suspect.contains(name)){
                suspect.add(name);
            }
        }else if(Objects.equals(status, "1")){
            if(!checked.contains(name)){
                checked.add(name);
            }
        }
    }
    public void add_checked_from_teacher(String name){
        if(unchecked.contains(name)){
            unchecked.remove(name);
        }
        if(suspect.contains(name)){
            suspect.remove(name);
        }

        if(!checked.contains(name)){
            checked.add(name);
        }
    }
    public Set<String> getUnchecked(){
        return unchecked;
    }
    public Set<String> getChecked(){
        return checked;
    }
    public Set<String> getSuspect(){
        return suspect;
    }
}
