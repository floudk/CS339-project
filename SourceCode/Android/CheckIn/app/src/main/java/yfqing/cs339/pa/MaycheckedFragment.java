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

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaycheckedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaycheckedFragment extends Fragment {

    private ListView maychecked_list;


    public MaycheckedFragment() {
        // Required empty public constructor
    }


    public static MaycheckedFragment newInstance(String param1, String param2) {
        MaycheckedFragment fragment = new MaycheckedFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maychecked, container, false);
        maychecked_list = (ListView) rootView.findViewById(R.id.maychecked_list);

        if(GlobalClass.students!=null&&GlobalClass.students.getSuspect()!=null){
            Integer nums = GlobalClass.students.getUnchecked().size();
//            Log.e("number",nums.toString());
            ArrayList<String> array = new ArrayList<String>(GlobalClass.students.getSuspect());
//            Log.e("student:",array.toString());
            ArrayAdapter<String> mHistory = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,array);
            maychecked_list.setAdapter(mHistory);
            maychecked_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            maychecked_list.setOnItemClickListener(
                    new  AdapterView.OnItemClickListener(){
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.e("checked",String.valueOf(position));
                            String name = (String) maychecked_list.getItemAtPosition(position);
                            Log.e("Maychecked name",name);
                            GlobalClass.students.add_checked_from_teacher(name);
                            ((TeacherActivity)getActivity()).update_tabs();
                        }
                    }
            );
        }
        return rootView;
    }
}