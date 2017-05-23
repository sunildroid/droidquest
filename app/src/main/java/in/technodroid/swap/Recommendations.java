package in.technodroid.swap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.technodroid.adapter.SectionedAdapterClass;
import in.technodroid.db.DBHelper;
import in.technodroid.model.AsyncResult;
import in.technodroid.model.UpdatesModel;
import in.technodroid.util.AppConstants;
import in.technodroid.util.DownloadWebpageTask;
import in.technodroid.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Updates.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Updates#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recommendations extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "Updates";
    private ListView lvTips;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<UpdatesModel> arUpdates;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Updates.
     */
    // TODO: Rename and change types and number of parameters
    public static Recommendations newInstance(String param1, String param2) {
        Recommendations fragment = new Recommendations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Recommendations() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    String[] url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the drawer_item for this fragment
        final android.support.v7.widget.RecyclerView v = (android.support.v7.widget.RecyclerView) inflater.inflate(R.layout.awareness, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        v.setLayoutManager(llm);

        // final File file = new File(Environment.getExternalStorageDirectory() + "/" + Util.appCompanyName + "/" + AppConstants.UPDATES_FILENAME);

        //Your RecyclerView.Adapter
        url = new String[]{
                "https://developer.android.com/training/index.html"
                ,"http://www.vogella.com/tutorials/android.html",
                "http://code.tutsplus.com/courses/getting-started-with-android"
                ,"http://www.androidhive.info/",
                "http://www.tutorialspoint.com/android/android_best_practices.htm",
                //Blogs
                "http://android-developers.blogspot.in/",
                "http://www.javatpoint.com/android-interview-questions",
                "http://skillgun.com/android/interview-questions-and-answers",
                "http://androidweekly.net/",
                "https://www.google.com/events/io"
               };

        String[] data = new String[]{
                "Official Android developer site"
                ,"Vogella.com - Android development tutorials for all",
                "tutsplus.com - Good for beginners "
                ,"Androidhive.info- Nice Android examples",
                "Tutorialspoint - Best Android practices",
                //Android resources
                "Official Android developer blogs",
                "Android Interview Questions for beginners",
                "Skillgun.com - Interview Q&A for All ",
                "Subscribe Weekly updates on androidweekly.net",
                "Google IO Videos from experts"
                };
     SimpleAdapter mAdapter = new SimpleAdapter(getActivity(),data);
        //This is the code to provide a sectioned list
        List<SectionedAdapterClass.Section> sections =
                new ArrayList<SectionedAdapterClass.Section>();

        //Sections
        sections.add(new SectionedAdapterClass.Section(0,"Best Tutorials"));
        sections.add(new SectionedAdapterClass.Section(5,"Other Android resources"));
        //sections.add(new SectionedAdapterClass.Section(10,"Best Books"));


        //Add your adapter to the sectionAdapter
        SectionedAdapterClass.Section[] dummy = new SectionedAdapterClass.Section[sections.size()];
        SectionedAdapterClass mSectionedAdapter = new
                SectionedAdapterClass(getActivity(),R.layout.section,R.id.section_text,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        v.setAdapter(mSectionedAdapter);

        return v;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

        private final Context mContext;
        private List<String> mData;

        public void add(String s,int position) {
            position = position == -1 ? getItemCount()  : position;
            mData.add(position,s);
            notifyItemInserted(position);
        }

        public void remove(int position){
            if (position < getItemCount()  ) {
                mData.remove(position);
                notifyItemRemoved(position);
            }
        }

        public  class SimpleViewHolder extends RecyclerView.ViewHolder {
            public final TextView title;

            public SimpleViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.simple_text);
            }
        }

        public SimpleAdapter(Context context, String[] data) {
            mContext = context;
            if (data != null)
                mData = new ArrayList<String>(Arrays.asList(data));
            else mData = new ArrayList<String>();
        }

        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_recommendations, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, final int position) {
           String mystring=mData.get(position);
            SpannableString content = new SpannableString(mystring);
            content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
            //yourtextview.setText(content);
            holder.title.setText(content);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mContext,"Position ="+position,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url[position]));
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

}
