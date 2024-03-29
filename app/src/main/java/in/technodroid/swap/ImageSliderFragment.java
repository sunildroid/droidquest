package in.technodroid.swap;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import in.technodroid.adapter.ImageSlideAdapter;
import in.technodroid.model.Product;
import in.technodroid.util.CheckNetworkConnection;
import in.technodroid.util.CirclePageIndicator;
import in.technodroid.util.PageIndicator;

public class ImageSliderFragment extends Fragment {

    public static final String ARG_ITEM_ID = "home_fragment";

    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

    // UI References
    private ViewPager mViewPager;
    TextView imgNameTxt;
    PageIndicator mIndicator;

    AlertDialog alertDialog;

    List<Product> products;
    //RequestImgTask task;
    boolean stopSliding = false;
    String message;

    private Runnable animateViewPager;
    private Handler handler;

  //  String url = "http://192.168.3.119:8080/products.json";
    FragmentActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViewById(view);

        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (products != null && products.size() != 0) {
                            stopSliding = false;
                            runnable(products.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });

        return view;
    }

    private void findViewById(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        imgNameTxt = (TextView) view.findViewById(R.id.img_name);
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }


    @Override
    public void onResume() {
        if (products == null) {
            sendRequest();
        } else {
            mViewPager.setAdapter(new ImageSlideAdapter(activity, products,
                    ImageSliderFragment.this));

            mIndicator.setViewPager(mViewPager);
            imgNameTxt.setText(""
                    + ((Product) products.get(mViewPager.getCurrentItem()))
                    .getName());
            runnable(products.size());
            //Re-run callback
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
        super.onResume();
    }


    @Override
    public void onPause() {
       /* if (task != null)
            task.cancel(true);*/
        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
        }
        super.onPause();
    }

    private void sendRequest() {
       // if (CheckNetworkConnection.isConnectionAvailable(activity)) {
           // task = new RequestImgTask(activity);
           // task.execute(url);
        //TODO to execute fetch request task
        products = new ArrayList<Product>();
        Product p1 = new Product();
        p1.setId(1);
        p1.setName("product1");
        p1.setImageUrl("http://www.gstatic.com/webp/gallery/1.jpg");

        Product p2 = new Product();
        p2.setId(1);
        p2.setName("product1");
        p2.setImageUrl("http://www.gstatic.com/webp/gallery/2.jpg");

        Product p3 = new Product();
        p3.setId(1);
        p3.setName("product1");
        p3.setImageUrl("http://www.gstatic.com/webp/gallery/3.jpg");

        products.add(p1);
        products.add(p2);
        products.add(p3);

        mViewPager.setAdapter(new ImageSlideAdapter(
                activity, products, ImageSliderFragment.this));

        mIndicator.setViewPager(mViewPager);
        imgNameTxt.setText(""
                + ((Product) products.get(mViewPager
                .getCurrentItem())).getName());
        runnable(products.size());
        handler.postDelayed(animateViewPager,
                ANIM_VIEWPAGER_DELAY);

       /* } else {
            message = getResources().getString(R.string.no_internet_connection);
            showAlertDialog(message, true);
        }*/
    }

    public void showAlertDialog(String message, final boolean finish) {
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            activity.finish();
                    }
                });
        alertDialog.show();
    }

    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (products != null) {
                    imgNameTxt.setText(""
                            + ((Product) products.get(mViewPager
                            .getCurrentItem())).getName());
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    //TODO to fetch from server
   /*
    private class RequestImgTask extends AsyncTask<String, Void, List<Product>> {
        private final WeakReference<Activity> activityWeakRef;
        Throwable error;

        public RequestImgTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Product> doInBackground(String... urls) {
            try {
                JSONObject jsonObject = getJsonObject(urls[0]);
                if (jsonObject != null) {
                    boolean status = jsonObject.getBoolean(TagName.TAG_STATUS);

                    if (status) {
                        JSONObject jsonData = jsonObject
                                .getJSONObject(TagName.TAG_DATA);
                        if (jsonData != null) {
                            products = JsonReader.getHome(jsonData);

                        } else {
                            message = jsonObject.getString(TagName.TAG_DATA);
                        }
                    } else {
                        message = jsonObject.getString(TagName.TAG_DATA);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return products;
        }
        */

        /**
         * It returns jsonObject for the specified url.
         *
         * @param url
         * @return JSONObject
         */
      /*  public JSONObject getJsonObject(String url) {
            JSONObject jsonObject = null;
            try {
                jsonObject = GetJSONObject.getJSONObject(url);
            } catch (Exception e) {
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            super.onPostExecute(result);

            if (activityWeakRef != null && !activityWeakRef.get().isFinishing()) {
                if (error != null && error instanceof IOException) {
                    message = getResources().getString(R.string.time_out);
                    showAlertDialog(message, true);
                } else if (error != null) {
                    message = getResources().getString(R.string.error_occured);
                    showAlertDialog(message, true);
                } else {
                    products = result;
                    if (result != null) {
                        if (products != null && products.size() != 0) {

                            mViewPager.setAdapter(new ImageSlideAdapter(
                                    activity, products, HomeFragment.this));

                            mIndicator.setViewPager(mViewPager);
                            imgNameTxt.setText(""
                                    + ((Product) products.get(mViewPager
                                    .getCurrentItem())).getName());
                            runnable(products.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY);
                        } else {
                            imgNameTxt.setText("No Products");
                        }
                    } else {
                    }
                }
            }
        }
    }*/

}