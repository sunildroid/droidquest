package in.technodroid.swap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.technodroid.db.DBHelper;
import in.technodroid.model.AsyncResult;
import in.technodroid.model.UpdatesModel;
import in.technodroid.util.AppConstants;
import in.technodroid.util.DownloadWebpageTask;
import in.technodroid.util.ImageLoadTask;
import in.technodroid.util.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Updates.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Updates#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Updates extends Fragment {
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
    TipAdapter tipAdapter;

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
    public static Updates newInstance(String param1, String param2) {
        Updates fragment = new Updates();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Updates() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the drawer_item for this fragment
        final android.support.v7.widget.RecyclerView v = (android.support.v7.widget.RecyclerView) inflater.inflate(R.layout.awareness, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        v.setLayoutManager(llm);

       // final File file = new File(Environment.getExternalStorageDirectory() + "/" + Util.appCompanyName + "/" + AppConstants.UPDATES_FILENAME);
        if(DBHelper.getInstance(getActivity()).getUpdatesCount() >0){ //Database is already downloaded
            //Read file
            try {
                tipAdapter = new TipAdapter(DBHelper.getInstance(getActivity()).getAllUpdates());
                v.setAdapter(tipAdapter);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Error" + e.getMessage());
            }
        }
        else { //IF  is not downloaded download file
            //Download spreadsheet
            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(List<?> object) {
                    try {
                        tipAdapter = new TipAdapter(object);
                        v.setAdapter(tipAdapter);
                    } catch (Exception e) {
                        Log.i(TAG ,"Error"+e.getMessage());
                        e.printStackTrace();
                    }
                }
            },getActivity()).execute(AppConstants.TIPS_URL, AppConstants.UPDATES_FILENAME);
        }

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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.home_screen, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                new DownloadWebpageTask(new AsyncResult() {
                    @Override
                    public void onResult(List<?> object) {
                        try {
                            //v.setAdapter(new TipAdapter(object));
                            tipAdapter.arTips=object;
                            tipAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.i(TAG ,"Error"+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },getActivity()).execute(AppConstants.TIPS_URL, AppConstants.UPDATES_FILENAME);

                return true;
            // case R.id.fragment_menu_item:
            // Do Fragment menu item stuff here
            //  return true;
            default:
                break;
        }

        return false;
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

    public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder>{

        public  class ViewHolder extends RecyclerView.ViewHolder {
            CardView v;
            TextView tvTitle;
            TextView tvDesc;
            ImageView imgDetail;
            ImageView imgShare;
            ImageView imglike;
            ImageView imgDelete;

            ViewHolder(View itemView) {
                super(itemView);
                v = (CardView)itemView.findViewById(R.id.llMain);
                tvDesc = (TextView) v.findViewById(R.id.tvDesc);
                tvTitle= (TextView) v.findViewById(R.id.tvTitle);
                imgDetail= (ImageView) v.findViewById(R.id.imgDetail);
                imgDelete=(ImageView)v.findViewById(R.id.imgDel);
                imglike=(ImageView)v.findViewById(R.id.imgLike);
                imgShare=(ImageView)v.findViewById(R.id.imgShare);
            }
        }

        public List<?> arTips;
        public TipAdapter(List<?> objects) {
            this.arTips = objects;
        }


        @Override
        public TipAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.awareness_item, viewGroup, false);
            ViewHolder pvh = new ViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(TipAdapter.ViewHolder viewHolder,final int i) {
            final UpdatesModel updatesModel= (UpdatesModel) arTips.get(arTips.size()-i-1);
            viewHolder.tvTitle.setText(updatesModel.getTitle());
            viewHolder.tvDesc.setText(updatesModel.getDesc());
            if(updatesModel.getImgPath()!=null){
                viewHolder.imgDetail.setVisibility(View.VISIBLE);
                new ImageLoadTask(updatesModel.getImgPath(),viewHolder.imgDetail).execute();
             //   viewHolder.imgDetail.setImageResource(R.drawable.tip);
            }
            viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.putExtra(Intent.EXTRA_SUBJECT, updatesModel.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT,updatesModel.getDesc() + "\n ##From DroidQuest##");
                    startActivity(Intent.createChooser(intent, "How do you want to share?"));
                }
            });

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return this.arTips.size();
        }
    }

}
