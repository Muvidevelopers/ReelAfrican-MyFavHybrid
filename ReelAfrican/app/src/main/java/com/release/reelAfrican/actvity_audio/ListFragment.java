package com.release.reelAfrican.actvity_audio;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.release.reelAfrican.R;
import com.release.reelAfrican.adapter_audio.ListDataAdaptor;
import com.release.reelAfrican.service.MusicService;
import com.release.reelAfrican.utils.Constants;

import static android.R.attr.name;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements View.OnClickListener {
    private boolean add = false;
    private Paint p = new Paint();
    private View view;
    ListDataAdaptor adapter;
    private RecyclerView my_recycler_view;
    boolean s = true;
    Context context;
    BottomSheetDialog mBottomSheetDialog;
    ContentDetailsOutput songDetailModel;
    boolean responce = false;
    NestedScrollView nestedScrollView;
    String album_name,song_url,song_imageUrl,song_name;


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        Log.v("nihar", "" + responce);
        context = getActivity();
        nestedScrollView = (NestedScrollView) v.findViewById(R.id.option_bar);
        nestedScrollView.setVisibility(View.GONE);
        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(R.layout.sheet_option);
//        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mBottomSheetDialog = null;
//                s= false ;
//            }
//        });

//        if (s == true && responce) {
//            mBottomSheetDialog.show();
//
//        }
//        if (s == false && responce) {
//            mBottomSheetDialog = null;
//            mBottomSheetDialog = new BottomSheetDialog(context);
//            mBottomSheetDialog.setContentView(R.layout.sheet_option);
//            mBottomSheetDialog.show();
//
//
//        }

        my_recycler_view = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        my_recycler_view.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        my_recycler_view.setHasFixedSize(true);
//        adapter = new ListDataAdaptor(getActivity(), dummyData.dummyDataList().this);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
        initSwipe();

        return v;
    }

    public void showOption(ContentDetailsOutput item, boolean isClicked) {
        Log.v("nihar_nihar", "" + item.getName());
        songDetailModel = item;
        responce = isClicked;

//        nestedScrollView.setVisibility(View.VISIBLE);
//        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mBottomSheetDialog = null;
//                s = false;
//            }
//        });
//
//        if (s == true && responce) {
//            mBottomSheetDialog.show();
//
//        }
//        if (s == false && !responce) {
//            mBottomSheetDialog = null;
//            mBottomSheetDialog = new BottomSheetDialog(context);
//            mBottomSheetDialog.setContentView(R.layout.sheet_option);
//            mBottomSheetDialog.show();
//
//
//        }
        album_name=item.getName();
        song_url = item.getMovieUrl();
        song_imageUrl =item.getPoster() ;
        song_name= item.getName();
        Player_State(1);

    }

    public void  Player_State(int funId){

        Intent playerData = new Intent("PLAYER_DETAIL");
        playerData.putExtra("songName",album_name );
        Log.v("nihar3",""+album_name);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(playerData);

      /*  if(!Util.serviceStarted)
        {
            Util.serviceStarted = true;
        }
        else
        {
            Util.serviceStarted = false;
            getContext().stopService(new Intent(getContext(), MusicService.class));
        }*/

        Intent j= new Intent(getContext(), MusicService.class);
        j.putExtra("ALBUM",song_url);
        j.putExtra("ALBUM_ART",song_imageUrl);
        j.putExtra("ALBUM_NAME",name);
        j.putExtra("ALBUM_SONG_NAME",song_name);
        j.putExtra("STATE",funId);
        j.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

        getContext().startService(j);


    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_favorite_border_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(my_recycler_view);

    }

    @Override
    public void onClick(View v) {

    }


    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom();
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

    }
    public class  PlayerAsync extends AsyncTask<Void,Void,Void> {
        Context context;


        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
