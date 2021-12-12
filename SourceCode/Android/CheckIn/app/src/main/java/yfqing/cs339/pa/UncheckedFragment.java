package yfqing.cs339.pa;

import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.FractionRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UncheckedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UncheckedFragment extends Fragment {

    private ListView unchecked_list;
    public UncheckedFragment() {
        // Required empty public constructor
    }


    public static UncheckedFragment newInstance(String param1, String param2) {
        UncheckedFragment fragment = new UncheckedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_unchecked, container, false);
        unchecked_list = (ListView) rootView.findViewById(R.id.unchecked_list);

        if(GlobalClass.students!=null&&GlobalClass.students.getUnchecked()!=null){
            Integer nums = GlobalClass.students.getUnchecked().size();
//            Log.e("number",nums.toString());
            ArrayList<String> array = new ArrayList<String>(GlobalClass.students.getUnchecked());
//            Log.e("student:",array.toString());
            ArrayAdapter<String> mHistory = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,array);
            unchecked_list.setAdapter(mHistory);
            unchecked_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            unchecked_list.setOnItemClickListener(
                    new  AdapterView.OnItemClickListener(){
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.e("checked",String.valueOf(position));
                            String name = (String) unchecked_list.getItemAtPosition(position);
                            Log.e("Unchecked name",name);
                            GlobalClass.students.add_checked_from_teacher(name);
                            ((TeacherActivity)getActivity()).update_tabs();
                        }
                    }
            );
        }
        return rootView;
    }

}
