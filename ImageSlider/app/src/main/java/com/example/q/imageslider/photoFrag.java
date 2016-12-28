package com.example.q.imageslider;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import java.util.ArrayList;


/**
 * Created by q on 2016-12-27.
 */

public class photoFrag extends Fragment {

    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private gridViewAdapter adapter;
    private GridView gridView;
    private int columnWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_grid_view, container, false);

        gridView = (GridView) view.findViewById(R.id.grid_view2);
        utils = new Utils(getActivity());

        // Initilizing Grid View
        InitilizeGridLayout();

        // loading all image paths from SD card
        imagePaths = utils.getFilePaths();
        // Gridview adapter
        adapter = new gridViewAdapter(getActivity(), imagePaths,
                columnWidth);

        // setting grid view adapter
        gridView.setAdapter(adapter);
        return view;

    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        Log.d("MKpadding:", padding+"");

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);
        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                230);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

}
