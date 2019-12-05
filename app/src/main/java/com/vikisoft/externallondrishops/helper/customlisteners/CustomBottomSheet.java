package com.vikisoft.externallondrishops.helper.customlisteners;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vikisoft.externallondrishops.R;


/**
*Crated by Ashish 04-02-2019
*/
public class CustomBottomSheet extends BottomSheetDialogFragment {


    public CustomBottomSheet() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_bottom_sheet, container, false);
        final TextView v1=v.findViewById(R.id.view1);
        final TextView v2=v.findViewById(R.id.view2);
        final TextView v3=v.findViewById(R.id.view3);
        final TextView v4=v.findViewById(R.id.view4);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* startActivity(new Intent(getActivity(),YourActivity.class));*/
                Toast.makeText(v.getContext(), v1.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* startActivity(new Intent(getActivity(),YourActivity.class));*/
                Toast.makeText(v.getContext(), v2.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* startActivity(new Intent(getActivity(),YourActivity.class));*/
                Toast.makeText(v.getContext(), v3.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* startActivity(new Intent(getActivity(),YourActivity.class));*/
                Toast.makeText(v.getContext(), v4.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        /*Button btn1 = (Button)v.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),YourActivity.class));
            }
        });*/
        return v;
    }
}
