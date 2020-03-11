package com.example.plt.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.plt.Common.Common;
import com.example.plt.Login.LoginActivity;
import com.example.plt.OrderStatus.OrderStatus;
import com.example.plt.R;
import com.example.plt.SizeGuide.SizeGuide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import io.paperdb.Paper;

public class ProfileFragment extends Fragment {

    private Button logout_btn;
    private TextView txt_orderDetails, txt_orderhistory, txt_sizeguide;

    private FirebaseDatabase database;
    private DatabaseReference userTable;
    private ProgressDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Paper.init(getContext());
        logout_btn = v.findViewById(R.id.logout_btn_profile);

        txt_orderhistory = v.findViewById(R.id.txtOrderHistory);
        txt_orderDetails = v.findViewById(R.id.txtOrdersDetails);
        txt_sizeguide = v.findViewById(R.id.txtSizeGuide);


        txt_orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderStatus.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write(Common.USER_KEY, "");
                Paper.book().write(Common.PSWD_KEY, "");
                Log.d("pttt", Paper.book().read(Common.USER_KEY) + " " + Paper.book().read(Common.PSWD_KEY));
                Intent singIn = new Intent(getContext(), LoginActivity.class);
                singIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(singIn);
            }
        });

        txt_sizeguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SizeGuide.class));
            }
        });


        return v;
    }


}