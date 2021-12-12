package yfqing.cs339.pa;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckedFragment extends Fragment {

    private ListView checked_list;
    public CheckedFragment() {
        // Required empty public constructor
    }


    public static CheckedFragment newInstance() {
        CheckedFragment fragment = new CheckedFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private RecyclerView checkedRecycler;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_checked, container, false);
        checked_list = (ListView) rootView.findViewById(R.id.checked_list);

        if(GlobalClass.students!=null&&GlobalClass.students.getUnchecked()!=null){
//            Integer nums = GlobalClass.students.getUnchecked().size();
//            Log.e("number",nums.toString());
            ArrayList<String> array = new ArrayList<String>(GlobalClass.students.getChecked());
//            Log.e("student:",array.toString());
//            ArrayAdapter<String> mHistory = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,array);
            ArrayAdapter<String> mHistory = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,array);
            checked_list.setAdapter(mHistory);
            checked_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//            checked_list.setOnItemClickListener(
//                    new  AdapterView.OnItemClickListener(){
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                            Log.e("checked",String.valueOf(position));
//                            String name = (String) checked_list.getItemAtPosition(position);
//                            Log.e("[checked name]",name);
////                            GlobalClass.students.add_checked(name);
//                        }
//                    }
//            );
        }

        return rootView;
    }


}

